package com.bhasaka.newsportal.core.models;

import com.adobe.cq.dam.cfm.ContentFragment;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class, ContentFragment.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductCardItemModel {


    @ValueMapValue
    private String productExpiry;

    @ValueMapValue
    private String productPrice;

    @ValueMapValue
    private String productImage;

    @ValueMapValue
    private String productColour;

    @ValueMapValue
    private String[] productTags;

    public String getProductExpiry() {
        return productExpiry;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductColour() {
        return productColour;
    }

    public String[] getProductTags() {
        return productTags;
    }

}
