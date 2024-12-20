package com.bhasaka.newsportal.core.listeners;

import com.bhasaka.newsportal.core.services.NPUtilService;
import com.day.cq.replication.ReplicationAction;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = EventHandler.class,
            property = {
                    EventConstants.EVENT_TOPIC+"="+ ReplicationAction.EVENT_TOPIC,
                    EventConstants.EVENT_FILTER+"= (& (type=ACTIVATE) (paths=/content/newsportal/us/en/templates/*))"
            },
            immediate = true
)
public class ActivationHandler implements EventHandler {

    private static final Logger LOG= LoggerFactory.getLogger(ActivationHandler.class);

    @Reference
    NPUtilService npUtilService;

    @Override
    public void handleEvent(Event event) {
        String[] paths = (String[])event.getProperty("paths");
        ResourceResolver resolver = npUtilService.getResourceResolver();

        for(String path:paths)
        {
            Resource contentResource = resolver.getResource(path+"/jcr:content");
            assert contentResource != null;
            ModifiableValueMap MVP=contentResource.adaptTo(ModifiableValueMap.class);
            assert MVP != null;
            MVP.put("pageActivated",true);
        }

        try {
            resolver.commit();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }

        LOG.info("Event Handler created Successfully");
    }
}
