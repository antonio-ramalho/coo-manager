package com.manager.coopafi.purchaseModule.services.interfaces;

import com.manager.coopafi.purchaseModule.dto.inputPurchase.InputPurchaseDto;
import com.manager.coopafi.purchaseModule.dto.inputPurchase.InputPurchaseInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputPurchase.InputPurchaseMinDto;
import java.util.List;

public interface InputPurchaseService {

    List<InputPurchaseMinDto> findAllByStatus();

    InputPurchaseDto findById(Long id);

    InputPurchaseDto processPurchase(InputPurchaseInsertDto dto);

    InputPurchaseDto registerPayment(Long id);

    void cancelPurchase(Long id);
}
