package com.vedasole.ekartecommercebackend.service.serviceImpl;

import com.vedasole.ekartecommercebackend.entity.Category;
import com.vedasole.ekartecommercebackend.entity.Product;
import com.vedasole.ekartecommercebackend.exception.ResourceNotFoundException;
import com.vedasole.ekartecommercebackend.payload.ProductDto;
import com.vedasole.ekartecommercebackend.repository.CategoryRepo;
import com.vedasole.ekartecommercebackend.repository.ProductRepo;
import com.vedasole.ekartecommercebackend.service.service_impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setProductId(100L);
        product.setName("Smartphone");
        product.setDesc("Latest model smartphone");
        product.setPrice(699.99);
        product.setQtyInStock(50);
        product.setCategory(category);

        productDto = new ProductDto();
        productDto.setProductId(100L);
        productDto.setName("Smartphone");
        productDto.setDesc("Latest model smartphone");
        productDto.setPrice(699.99);
        productDto.setQtyInStock(50);
        productDto.setCategoryId(1L);
    }

    @Test
    void testGetProductById_WhenProductExists_ShouldReturnProductDto() {
        // Arrange
        Long productId = 100L;
        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductDto.class)).thenReturn(productDto);

        // Act
        ProductDto result = productService.getProductById(productId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("Smartphone");
        verify(productRepo, times(1)).findById(productId);
        verify(modelMapper, times(1)).map(product, ProductDto.class);
    }

    @Test
    void testGetProductById_WhenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        Long productId = 999L;
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(productId));
        verify(productRepo, times(1)).findById(productId);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void testCreateProduct_WhenCategoryExists_ShouldReturnCreatedProductDto() {
        // Arrange
        when(modelMapper.map(productDto, Product.class)).thenReturn(product);
        when(categoryRepo.findById(productDto.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepo.save(any(Product.class))).thenReturn(product);
        when(modelMapper.map(product, ProductDto.class)).thenReturn(productDto);

        // Act
        ProductDto result = productService.createProduct(productDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Smartphone");
        verify(categoryRepo, times(1)).findById(productDto.getCategoryId());
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProduct_WhenCategoryDoesNotExist_ShouldThrowException() {
        // Arrange
        when(modelMapper.map(productDto, Product.class)).thenReturn(product);
        when(categoryRepo.findById(productDto.getCategoryId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(productDto));
        verify(categoryRepo, times(1)).findById(productDto.getCategoryId());
        verify(productRepo, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_ShouldCallDeleteById() {
        // Arrange
        Long productId = 100L;

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepo, times(1)).deleteById(productId);
    }
}
