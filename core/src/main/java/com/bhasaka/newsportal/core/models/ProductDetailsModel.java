package com.bhasaka.newsportal.core.models;
import com.adobe.cq.wcm.core.components.models.Button;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.Text;
import com.adobe.cq.wcm.core.components.models.Title;
import com.bhasaka.newsportal.core.services.CountryAPIService;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model(
    adaptables = {Resource.class,SlingHttpServletRequest.class},
    resourceType = ProductDetailsModel.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Getter
public class ProductDetailsModel {

    public static final String RESOURCE_TYPE = "newsportal/components/product-details";

    @OSGiService
    private CountryAPIService countryAPIService;

    @ValueMapValue
    private List<String> tags;

    @ValueMapValue
    private String category;


    @SlingObject
    private Resource resource;

    @Inject
    @ChildResource(name = "name")
    private Image image;


    private Text text;


    private Title title;


    private Button button;

    @PostConstruct
    public void init() {
        if (resource != null) {
            image = resource.getChild("image").adaptTo(Image.class);
            title=resource.getChild("title").adaptTo(Title.class);
            countryAPIService.fetchAndCreateCountryNodes();
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public String getImage() {
        return image.getFileReference().toString();
    }

    public String getText() {
        return text.getText();
    }

    public String getTitle() {
        return title.getText();
    }

    public String getButtonText() {
        return button.getText();
    }

    public String getButtonLink() {
        return button.getButtonLink().toString();
    }

    public String getCategory() {
        return category;
    }
}
