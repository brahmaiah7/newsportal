package com.bhasaka.newsportal.core.schedulers;

import com.bhasaka.newsportal.core.services.NPUtilService;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.Iterator;

@Component(service = Runnable.class,
        immediate = true
)
@Designate(ocd = TemplateExpiryConfiguration.class)
public class TemplatePagesExpiryScheduler implements Runnable{

    private static final Logger LOG= LoggerFactory.getLogger(TemplatePagesExpiryScheduler.class);

    @Reference
    NPUtilService npUtilService;

    @Reference
    Replicator replicator;

    @Reference
    Scheduler scheduler;


    @Activate
    @Modified
    public void activate(TemplateExpiryConfiguration config) {
        if(config.enable())
        {
            ScheduleOptions expr = scheduler.EXPR(config.cornExpression());
            expr.name(config.schedulerName());
            expr.canRunConcurrently(false);
            scheduler.schedule(this,expr);
        }
        else {
                scheduler.unschedule(config.schedulerName());
        }
    }


    @Override
    public void run() {
        LOG.info("Inside TemplatePagesExpiryScheduler");
        ResourceResolver resolver = npUtilService.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Page templateExpiry = pageManager.getPage("/content/newsportal/us/en/templates");

        if(templateExpiry != null)
        {
            Iterator<Page> pages = templateExpiry.listChildren();
            while (pages.hasNext())
            {
                Page page = pages.next();
                ValueMap pageProperties = page.getProperties();
                Date articleExpiry = pageProperties.get("articleExpiry", Date.class);
                Date today=new Date();

                if(articleExpiry != null && articleExpiry.compareTo(today) < 0)
                {
                  //  replicator.replicate(resolver.adaptTo(Session.class),);
                }
            }

        }
    }
}
