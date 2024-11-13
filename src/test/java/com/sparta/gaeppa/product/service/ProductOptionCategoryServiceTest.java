package com.sparta.gaeppa.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.product.dto.productOptionCategory.ProductOptionCategoryPutRequestDto;
import com.sparta.gaeppa.product.dto.productOptionCategory.ProductOptionCategoryRequestDto;
import com.sparta.gaeppa.product.dto.productOptionCategory.ProductOptionCategoryResponseDto;
import com.sparta.gaeppa.product.entity.Product;
import com.sparta.gaeppa.product.entity.ProductOptionCategory;
import com.sparta.gaeppa.product.repository.ProductOptionCategoryRepository;
import com.sparta.gaeppa.product.repository.ProductOptionRepository;
import com.sparta.gaeppa.product.repository.ProductRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductOptionCategoryServiceTest {

    @InjectMocks
    ProductOptionCategoryService productOptionCategoryService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductOptionCategoryRepository productOptionCategoryRepository;

    @Mock
    ProductOptionRepository productOptionRepository;

    @Test
    @DisplayName("상품 옵션 카테고리 생성 성공 테스트")
    void createProductOptionCategory() {
        //given
        ProductOptionCategoryRequestDto requestDto = ProductOptionCategoryRequestDto.builder()
                .productId(UUID.randomUUID())
                .productOptionCategoryName("productOptionCategoryName")
                .maxLimits(1)
                .build();

        ProductOptionCategory productOptionCategory = requestDto.toEntity();
        given(productRepository.findById(requestDto.getProductId())).willReturn(
                java.util.Optional.of(Product.builder().build()));
        given(productOptionCategoryRepository.save(any(ProductOptionCategory.class))).willReturn(productOptionCategory);

        //when
        ProductOptionCategoryResponseDto responseDto = productOptionCategoryService.createProductOptionCategory(
                requestDto);

        //then
        assertEquals(requestDto.getProductOptionCategoryName(), responseDto.getProductOptionCategoryName());
        assertEquals(requestDto.getMaxLimits(), responseDto.getMaxLimits());
    }

    @Test
    @DisplayName("상품 옵션 카테고리 생성 실패 테스트 - 상품이 존재하지 않는 경우")
    void createProductOptionCategoryFail() {
        //given
        ProductOptionCategoryRequestDto requestDto = ProductOptionCategoryRequestDto.builder()
                .productId(UUID.randomUUID())
                .productOptionCategoryName("productOptionCategoryName")
                .maxLimits(1)
                .build();

        given(productRepository.findById(requestDto.getProductId())).willReturn(
                java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productOptionCategoryService.createProductOptionCategory(requestDto));
        assertEquals(exception.getMessage(), "상품이 존재하지 않습니다.");
        assertEquals(exception.getStatus(), 404);
    }

    @Test
    @DisplayName("상품 옵션 카테고리 수정 성공 테스트")
    void updateProductOptionCategory() {
        UUID optionCategoryId = UUID.randomUUID();
        ProductOptionCategoryPutRequestDto requestDto = ProductOptionCategoryPutRequestDto.builder()
                .productOptionCategoryName("productOptionCategoryName")
                .maxLimits(1)
                .build();

        ProductOptionCategory productOptionCategory = ProductOptionCategory.builder().build();

        given(productOptionCategoryRepository.findById(optionCategoryId)).willReturn(
                java.util.Optional.of(productOptionCategory));

        //when-then
        productOptionCategoryService.updateProductOptionCategory(optionCategoryId, requestDto);
    }

    @Test
    @DisplayName("상품 옵션 카테고리 수정 실패 테스트 - 상품 옵션 카테고리가 존재하지 않는 경우")
    void updateProductOptionCategoryFail() {
        UUID optionCategoryId = UUID.randomUUID();
        ProductOptionCategoryPutRequestDto requestDto = ProductOptionCategoryPutRequestDto.builder()
                .productOptionCategoryName("productOptionCategoryName")
                .maxLimits(1)
                .build();

        given(productOptionCategoryRepository.findById(optionCategoryId)).willReturn(
                java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productOptionCategoryService.updateProductOptionCategory(optionCategoryId, requestDto));
        assertEquals(exception.getMessage(), "상품 옵션 카테고리가 존재하지 않습니다.");
        assertEquals(exception.getStatus(), 404);
    }

    @Test
    @DisplayName("상품 옵션 카테고리 삭제 성공 테스트")
    void deleteProductOptionCategory() {
        UUID optionCategoryId = UUID.randomUUID();

        ProductOptionCategory productOptionCategory = ProductOptionCategory.builder().build();

        given(productOptionCategoryRepository.findById(optionCategoryId)).willReturn(
                java.util.Optional.of(productOptionCategory));

        given(productOptionRepository.existsByProductOptionCategory(productOptionCategory)).willReturn(false);

        //when-then
        productOptionCategoryService.deleteProductOptionCategory(optionCategoryId);
    }

    @Test
    @DisplayName("상품 옵션 카테고리 삭제 실패 테스트 - 상품 옵션 카테고리가 존재하지 않는 경우")
    void deleteProductOptionCategoryFail() {
        UUID optionCategoryId = UUID.randomUUID();

        given(productOptionCategoryRepository.findById(optionCategoryId)).willReturn(
                java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productOptionCategoryService.deleteProductOptionCategory(optionCategoryId));
        assertEquals(exception.getMessage(), "상품 옵션 카테고리가 존재하지 않습니다.");
        assertEquals(exception.getStatus(), 404);
    }
}