package com.manager.coopafi.purchaseModule.services;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.infrastructure.exceptions.ResourceNotFoundException;
import com.manager.coopafi.infrastructure.valueObjects.ExpirationDate;
import com.manager.coopafi.infrastructure.valueObjects.Ncm;
import com.manager.coopafi.infrastructure.valueObjects.Price;
import com.manager.coopafi.infrastructure.valueObjects.Quantity;
import com.manager.coopafi.personModule.entities.Person;
import com.manager.coopafi.personModule.repositories.PersonRepository;
import com.manager.coopafi.produtcModule.entities.InputProduct;
import com.manager.coopafi.produtcModule.enums.MeasureUnit;
import com.manager.coopafi.purchaseModule.dto.inputPurchase.InputPurchaseDto;
import com.manager.coopafi.purchaseModule.dto.inputPurchase.InputPurchaseInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputPurchaseItem.InputPurchaseItemInsertDto;
import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.entities.InputPurchase;
import com.manager.coopafi.purchaseModule.enums.PaymentStatus;
import com.manager.coopafi.purchaseModule.repositories.InputBatchRepository;
import com.manager.coopafi.purchaseModule.repositories.InputPurchaseRepository;
import com.manager.coopafi.purchaseModule.services.impl.InputPurchaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InputPurchaseServiceTest {

    @Mock
    private InputPurchaseRepository purchaseRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private InputBatchRepository batchRepository;

    @InjectMocks
    private InputPurchaseServiceImpl purchaseService;

    private Person mockPerson;
    private InputProduct testProduct;
    private InputBatch testBatch;

    @BeforeEach
    void setUp() {
        mockPerson = mock(Person.class);
        ReflectionTestUtils.setField(mockPerson, "id", 1L);

        testProduct = new InputProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("31010000"),
                "Adubo Bio",
                new Price(new BigDecimal("20.00")),
                "PROD-10");
        ReflectionTestUtils.setField(testProduct, "id", 100L);

        Quantity initialQty = new Quantity(new BigDecimal("50.00"));
        testBatch = new InputBatch(initialQty, testProduct, initialQty,
                new ExpirationDate(LocalDate.now().plusMonths(4)));
        ReflectionTestUtils.setField(testBatch, "id", 200L);
    }

    @Test
    @DisplayName("Deve processar compra com sucesso, deduzindo do estoque do lote e calculando valor total")
    void shouldProcessPurchaseSuccessfully() {
        InputPurchaseItemInsertDto itemDto = new InputPurchaseItemInsertDto(200L, 5.0);
        InputPurchaseInsertDto insertDto = new InputPurchaseInsertDto(1L, List.of(itemDto));

        when(personRepository.findById(1L)).thenReturn(Optional.of(mockPerson));
        when(batchRepository.findById(200L)).thenReturn(Optional.of(testBatch));
        when(purchaseRepository.save(any(InputPurchase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InputPurchaseDto result = purchaseService.processPurchase(insertDto);

        assertThat(result).isNotNull();
        assertThat(testBatch.getCurrentQuantity().getAmount()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.totalValue()).isEqualTo(100.0);
        verify(purchaseRepository, times(1)).save(any(InputPurchase.class));
    }

    @Test
    @DisplayName("Deve lançar DomainException ao tentar processar compra sem nenhum item")
    void shouldThrowExceptionWhenItemsListIsEmpty() {
        InputPurchaseInsertDto emptyDto = new InputPurchaseInsertDto(1L, List.of());

        assertThatThrownBy(() -> purchaseService.processPurchase(emptyDto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("É necessário que existe pelo menos um item.");

        verify(purchaseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve cancelar compra pendente e estornar o estoque para o lote")
    void shouldCancelPendingPurchaseAndRestoreStock() {
        InputPurchase purchase = new InputPurchase(mockPerson);
        ReflectionTestUtils.setField(purchase, "id", 10L);

        Quantity qty = new Quantity(new BigDecimal("10.00"));
        com.manager.coopafi.purchaseModule.entities.InputPurchaseItem item = new com.manager.coopafi.purchaseModule.entities.InputPurchaseItem(
                qty, testProduct.getProductPrice(), testBatch);
        purchase.addItem(item);

        assertThat(testBatch.getCurrentQuantity().getAmount()).isEqualByComparingTo(new BigDecimal("40.00"));

        when(purchaseRepository.findById(10L)).thenReturn(Optional.of(purchase));

        purchaseService.cancelPurchase(10L);

        assertThat(purchase.getStatus()).isEqualTo(PaymentStatus.CANCELED);
        assertThat(testBatch.getCurrentQuantity().getAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
        verify(purchaseRepository).save(purchase);
    }

    @Test
    @DisplayName("Deve lançar DomainException ao tentar cancelar uma compra que já foi paga")
    void shouldThrowExceptionWhenCancellingPaidPurchase() {
        InputPurchase purchase = new InputPurchase(mockPerson);
        purchase.markAsPaid();
        ReflectionTestUtils.setField(purchase, "id", 15L);

        when(purchaseRepository.findById(15L)).thenReturn(Optional.of(purchase));

        assertThatThrownBy(() -> purchaseService.cancelPurchase(15L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Não é possível alterar uma compra cancelada ou paga.");

        verify(purchaseRepository, never()).save(purchase);
    }

    @Test
    @DisplayName("Deve registrar pagamento com sucesso alterando status para PAID")
    void shouldRegisterPaymentSuccessfully() {
        InputPurchase purchase = new InputPurchase(mockPerson);
        ReflectionTestUtils.setField(purchase, "id", 20L);

        when(purchaseRepository.findById(20L)).thenReturn(Optional.of(purchase));
        when(purchaseRepository.save(any(InputPurchase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InputPurchaseDto result = purchaseService.registerPayment(20L);

        assertThat(result.paymentStatus()).isEqualTo(PaymentStatus.PAID.getDescription());
        verify(purchaseRepository).save(purchase);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar processar compra para cliente inexistente")
    void shouldThrowResourceNotFoundExceptionWhenFarmerNotFound() {
        InputPurchaseItemInsertDto itemDto = new InputPurchaseItemInsertDto(200L, 2.0);
        InputPurchaseInsertDto dto = new InputPurchaseInsertDto(999L, List.of(itemDto));

        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseService.processPurchase(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar processar compra com lote inexistente")
    void shouldThrowResourceNotFoundExceptionWhenBatchNotFound() {
        InputPurchaseItemInsertDto itemDto = new InputPurchaseItemInsertDto(999L, 2.0);
        InputPurchaseInsertDto dto = new InputPurchaseInsertDto(1L, List.of(itemDto));

        when(personRepository.findById(1L)).thenReturn(Optional.of(mockPerson));
        when(batchRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseService.processPurchase(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Lote de produto não encontrado");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao registrar pagamento para venda inexistente")
    void shouldThrowResourceNotFoundExceptionWhenPurchaseNotFoundOnPayment() {
        when(purchaseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseService.registerPayment(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Venda não encontrada");
    }
}
