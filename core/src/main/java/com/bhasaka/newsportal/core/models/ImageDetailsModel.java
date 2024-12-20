package com.bhasaka.newsportal.core.models;


import com.adobe.cq.export.json.ExporterConstants;
import com.bhasaka.newsportal.core.services.ArticleService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Date;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "newsportal/components/image-component")
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ImageDetailsModel
{

    @ValueMapValue(injectionStrategy = InjectionStrategy.REQUIRED)
    private String imageTitle;

    @ValueMapValue
    private String imageDesc;


    @ValueMapValue
    private String imageUpload;

    @ValueMapValue
    private Date date;

    private boolean oldImage=false;

    @ValueMapValue(name = "sling:resourceType")
    //@Named("sling:resourceType")
    private String slingResourceType;

    @OSGiService
    ArticleService articleService;

    public void setDate(Date date) {
        this.date = date;
    }

    @PostConstruct
    public void image()
    {

        if(date!=null)
        {
            Date today=new Date();
            if(date.compareTo(today) < 0)
            {
                oldImage=true;
            }
        }
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public String getImageUpload() {
        return imageUpload;
    }

    public Date getDate() {
        return date;
    }

    public boolean isOldImage() {
        return oldImage;
    }
}
