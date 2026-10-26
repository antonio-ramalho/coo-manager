package com.manager.coopafi.purchaseModule.services.interfaces;

import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductDto;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductMinDto;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InputProductService {

    Page<InputProductMinDto> findAllByPage(Pageable pageable, String searchTerm);

    InputProductDto findById(Long id);

    InputProductDto insert(InputProductInsertDto dto);

    InputProductDto update(Long id, InputProductUpdateDto dto);

    void delete(Long id);

    void activate(Long id);
}
