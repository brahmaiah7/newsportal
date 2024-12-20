package com.bhasaka.newsportal.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.adobe.cq.export.json.ComponentExporter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = ProductCardModel.RESOURCE_TYPE,
        adapters = {ProductCardModel.class,ComponentExporter.class}
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ProductCardModel implements ComponentExporter {

    public static final String RESOURCE_TYPE = "newsportal/components/productcard";
    public static final String CF_JCR_PATH = "/jcr:content/data/master";

    @SlingObject
    private ResourceResolver resourceResolver;

    @ChildResource
    List<ProductCardItemModel> productCardItemModelList;

    @ChildResource
    List<ProductCardItemModel> popularProducts;

    @ValueMapValue
    private String productTitle;

    @ValueMapValue
    private String productDescription;

    @ValueMapValue
    private boolean productStatus;

    @ValueMapValue
    private String productCategory;

    @ValueMapValue
    private boolean loadProductFromCFPath;

    @ValueMapValue
    private String[]  cfPaths;

    @PostConstruct
    List<ProductCardItemModel> init() {
        if (loadProductFromCFPath && ArrayUtils.isNotEmpty(cfPaths)) {
            productCardItemModelList =new ArrayList<>();
            for (String path : cfPaths) {
                Resource resource = resourceResolver.getResource(path +  CF_JCR_PATH);
                if(resource !=null) {
                    ProductCardItemModel productCardItemModel = resource.adaptTo(ProductCardItemModel.class);
                    if (productCardItemModel != null) {
                        productCardItemModelList.add(productCardItemModel);
                    }
                }
            }
        }
        return productCardItemModelList;
    }

    public boolean isLoadProductFromCFPath() {
        return loadProductFromCFPath;
    }


    public String getProductTitle() {
        return productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public boolean isProductStatus() {
        return productStatus;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public List<ProductCardItemModel> getProductCardItemModelList() {
        return productCardItemModelList;
    }

    public List<ProductCardItemModel> getPopularProducts() {
        return popularProducts;
    }


    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}