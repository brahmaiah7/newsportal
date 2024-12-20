package com.bhasaka.newsportal.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ImageDetailsModelTest {


    AemContext context=new AemContext();


    ImageDetailsModel imageDetailsModel;

    @BeforeEach
    public void testCases()
    {
        context.addModelsForClasses(ImageDetailsModel.class);
       context.load().json("/imagedetails.json", "/content");
        Resource json = context.currentResource("/content/image-details");
        imageDetailsModel = json.adaptTo(ImageDetailsModel.class);
        /*
        Map<String,Object> properties=new HashMap<>();
        properties.put("imageTitle","Ms Dhoni");
        properties.put("imageDesc","Captain of India in all formats and won all the icc trophies as captain and best finisher in the world");
        properties.put("imageUpload","/content/dam/newsportal/msd.jpeg");

        Resource resource = context.create().resource("/content/newsportal/imagecomponent", properties);
        imageDetailsModel= resource.adaptTo(ImageDetailsModel.class);*/
    }

    @Test
    void imageDetails()
    {
        assertEquals("Ms Dhoni",imageDetailsModel.getImageTitle());
        assertEquals("Captain of India",imageDetailsModel.getImageDesc());
    }

    @Test
    void jsonTestCases()
    {
        assertEquals("Ms Dhoni",imageDetailsModel.getImageTitle());
        Date currentDate=new Date();
        imageDetailsModel.setDate( new Date(currentDate.getTime() - (1000 * 60 * 60 * 24)));
        imageDetailsModel.image();
        boolean oldImage = imageDetailsModel.isOldImage();
        assertTrue(oldImage);
        assertNull(imageDetailsModel.getImageUpload());
    }
}