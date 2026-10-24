package com.manager.coopafi.purchaseModule.services;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.infrastructure.exceptions.InsufficientStockException;
import com.manager.coopafi.infrastructure.exceptions.ResourceNotFoundException;
import com.manager.coopafi.infrastructure.valueObjects.ExpirationDate;
import com.manager.coopafi.infrastructure.valueObjects.Ncm;
import com.manager.coopafi.infrastructure.valueObjects.Price;
import com.manager.coopafi.infrastructure.valueObjects.Quantity;
import com.manager.coopafi.produtcModule.entities.InputProduct;
import com.manager.coopafi.produtcModule.enums.MeasureUnit;
import com.manager.coopafi.produtcModule.repositories.InputProductRepository;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchTableDto;
import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import com.manager.coopafi.purchaseModule.repositories.InputBatchRepository;
import com.manager.coopafi.purchaseModule.services.batchValidators.BatchExpirationDateValidator;
import com.manager.coopafi.purchaseModule.services.batchValidators.BatchQuantityValidator;
import com.manager.coopafi.purchaseModule.services.batchValidators.BatchValidationStrategy;
import com.manager.coopafi.purchaseModule.services.impl.InputBatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InputBatchServiceTest {

    @Mock
    private InputBatchRepository batchRepository;

    @Mock
    private InputProductRepository productRepository;

    @Spy
    private List<BatchValidationStrategy> validationStrategies = new ArrayList<>();

    @InjectMocks
    private InputBatchServiceImpl batchService;

    private InputProduct testProduct;
    private InputBatch testBatch;

    @BeforeEach
    void setUp() {
        validationStrategies.clear();
        validationStrategies.add(new BatchExpirationDateValidator());
        validationStrategies.add(new BatchQuantityValidator());
        ReflectionTestUtils.setField(batchService, "validationStrategies", validationStrategies);

        testProduct = new InputProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("31010000"),
                "Adubo Orgânico",
                new Price(new BigDecimal("50.00")),
                "ADU-001");
        ReflectionTestUtils.setField(testProduct, "id", 1L);

        Quantity initialQty = new Quantity(new BigDecimal("100.00"));
        testBatch = new InputBatch(initialQty, testProduct, initialQty,
                new ExpirationDate(LocalDate.now().plusMonths(6)));
        ReflectionTestUtils.setField(testBatch, "id", 10L);
    }

    @Test
    @DisplayName("Deve inserir lote com sucesso quando produto for ativo e dados válidos")
    void shouldInsertBatchSuccessfully() {
        InputBatchInsertDto dto = new InputBatchInsertDto(
                1L,
                50.0,
                LocalDate.now().plusMonths(3));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(testProduct));
        when(batchRepository.save(any(InputBatch.class))).thenAnswer(invocation -> {
            InputBatch b = invocation.getArgument(0);
            ReflectionTestUtils.setField(b, "id", 99L);
            return b;
        });

        InputBatchDto result = batchService.insert(dto);

        assertThat(result).isNotNull();
        assertThat(result.productName()).isEqualTo("Adubo Orgânico");
        assertThat(result.currentQuantity()).isEqualTo(50.0);
        verify(batchRepository, times(1)).save(any(InputBatch.class));
    }

    @Test
    @DisplayName("Deve lançar DomainException ao tentar cadastrar lote com data de validade no passado")
    void shouldThrowExceptionWhenExpirationDateIsInThePast() {
        InputBatchInsertDto dto = new InputBatchInsertDto(
                1L,
                20.0,
                LocalDate.now().minusDays(1));

        assertThatThrownBy(() -> batchService.insert(dto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("A data de validade não pode ser anterior à data atual.");

        verify(batchRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar DomainException ao tentar cadastrar lote com quantidade menor ou igual a zero")
    void shouldThrowExceptionWhenQuantityIsZeroOrNegative() {
        InputBatchInsertDto dto = new InputBatchInsertDto(
                1L,
                0.0,
                LocalDate.now().plusMonths(2));

        assertThatThrownBy(() -> batchService.insert(dto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("A quantidade inicial do lote deve ser superior a zero.");

        verify(batchRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve desativar lote logicamente ao chamar delete")
    void shouldDeactivateBatchLogically() {
        when(batchRepository.findById(10L)).thenReturn(Optional.of(testBatch));

        batchService.delete(10L);

        assertThat(testBatch.getProductStatus()).isEqualTo(ProductInventoryStatus.INACTIVE);
        verify(batchRepository).save(testBatch);
    }

    @Test
    @DisplayName("Deve diminuir estoque e atualizar status para LOW_STOCK quando saldo atingir o limite")
    void shouldDecreaseStockAndUpdateStatusToLowStock() {
        testBatch.decreaseQuantity(new Quantity(new BigDecimal("92.00")));

        assertThat(testBatch.getCurrentQuantity().getAmount()).isEqualByComparingTo(new BigDecimal("8.00"));
        assertThat(testBatch.getProductStatus()).isEqualTo(ProductInventoryStatus.LOW_STOCK);
    }

    @Test
    @DisplayName("Deve atualizar status para OUT_OF_STOCK quando quantidade for zerada")
    void shouldUpdateStatusToOutOfStockWhenZeroed() {
        testBatch.decreaseQuantity(new Quantity(new BigDecimal("100.00")));

        assertThat(testBatch.getCurrentQuantity().getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(testBatch.getProductStatus()).isEqualTo(ProductInventoryStatus.OUT_OF_STOCK);
    }

    @Test
    @DisplayName("Deve lançar DomainException ao tentar baixar quantidade maior que o estoque atual")
    void shouldThrowExceptionWhenDecreasingMoreThanCurrentStock() {
        assertThatThrownBy(() -> testBatch.decreaseQuantity(new Quantity(new BigDecimal("150.00"))))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Existem apenas");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar lote por ID inexistente")
    void shouldThrowResourceNotFoundExceptionWhenBatchNotFound() {
        when(batchRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> batchService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Lote não encontrado");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao inserir lote para produto inexistente ou inativo")
    void shouldThrowResourceNotFoundExceptionWhenProductNotFoundOnInsert() {
        InputBatchInsertDto dto = new InputBatchInsertDto(999L, 10.0, LocalDate.now().plusMonths(2));
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> batchService.insert(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não cadastrado ou inativo");
    }

    @Test
    @DisplayName("Deve retornar página de lotes formatados para tabela")
    void shouldReturnPagedBatchesForTable() {
        Page<InputBatch> page = new PageImpl<>(List.of(testBatch), PageRequest.of(0, 10), 1);
        when(batchRepository.searchBatches(eq("Adubo"), any())).thenReturn(page);

        Page<InputBatchTableDto> result = batchService.searchBatches(0, 10, "Adubo");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().productName()).isEqualTo("Adubo Orgânico");
        assertThat(result.getContent().getFirst().statusDescription()).isEqualTo("Em Estoque");
    }
}
