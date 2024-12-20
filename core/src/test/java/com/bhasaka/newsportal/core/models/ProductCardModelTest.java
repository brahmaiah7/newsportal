package com.bhasaka.newsportal.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ProductCardModelTest {

    AemContext context = new AemContext();

    ProductCardModel productCardModel;

    public static final String JSON_PATH="/productCardModel.json";

    public static final String CONTENT_PATH="/content/productCard";

    public static final String CF_PATH="/content/dam/newsportal/product/jcr:content/data";

    public static final String CURRENT_RESOURCE="/content/productCard/jcr:content";

    public static final String JSON_PATH_CF="/contentFragment.json";

    @BeforeEach
    void init() {
        context.load().json(JSON_PATH,CONTENT_PATH);
        context.load().json(JSON_PATH_CF, CF_PATH);
        Resource resource = context.currentResource(CURRENT_RESOURCE);
        productCardModel = resource.adaptTo(ProductCardModel.class);
    }

    @Test
    void getters() {
        assertNotNull(productCardModel);
        assertEquals(2,productCardModel.getPopularProducts().size());
        assertEquals("Smartphone XYZ",productCardModel.getProductTitle());
        assertEquals("A high-performance smartphone with excellent features.",productCardModel.getProductDescription());
        assertTrue(productCardModel.isProductStatus());
        assertEquals("Electronics",productCardModel.getProductCategory());
        assertTrue(productCardModel.isLoadProductFromCFPath());
        assertEquals("newsportal/components/productcard",productCardModel.getExportedType());
    }

    @Test
    void popularProducts() {
        assertEquals("white",productCardModel.getPopularProducts().get(1).getProductColour());
        assertEquals("2025-05-15",productCardModel.getPopularProducts().get(1).getProductExpiry());
        assertEquals("499.99",productCardModel.getPopularProducts().get(1).getProductPrice());
        assertEquals("/content/dam/newsportal/images/popular2.jpg",productCardModel.getPopularProducts().get(1).getProductImage());
    }

    @Test
    void productModelList() {

        Resource resource = context.currentResource("/content/dam/newsportal/product/jcr:content/data/master");
        ProductCardItemModel cardModel = resource.adaptTo(ProductCardItemModel.class);
        assertEquals(1,productCardModel.getProductCardItemModelList().size());
        assertEquals("2024-12-31",cardModel.getProductExpiry());
        assertEquals("299.99",cardModel.getProductPrice());
        assertEquals("/content/dam/newsportal/images/product1.jpg",cardModel.getProductImage());
        assertEquals(3,cardModel.getProductTags().length);
        assertEquals("Red",cardModel.getProductColour());
    }
}