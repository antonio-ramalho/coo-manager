package com.manager.coomanager.purchaseModule.services.interfaces;

import com.manager.coomanager.purchaseModule.dto.inputPurchase.InputPurchaseDto;
import com.manager.coomanager.purchaseModule.dto.inputPurchase.InputPurchaseInsertDto;
import com.manager.coomanager.purchaseModule.dto.inputPurchase.InputPurchaseMinDto;
import com.manager.coomanager.purchaseModule.dto.inputPurchase.InputPurchaseTableDto;
import com.manager.coomanager.purchaseModule.dto.inputPurchase.RecentSaleDto;
import org.springframework.data.domain.Page;
import java.util.List;

public interface InputPurchaseService {

    List<InputPurchaseMinDto> findAllByStatus();

    Page<InputPurchaseTableDto> searchPurchases(int page, int size, String searchTerm);

    List<RecentSaleDto> findRecentPurchases();

    InputPurchaseDto findById(Long id);

    InputPurchaseDto processPurchase(InputPurchaseInsertDto dto);

    InputPurchaseDto registerPayment(Long id);

    void cancelPurchase(Long id);
}
