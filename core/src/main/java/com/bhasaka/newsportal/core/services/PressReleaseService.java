package com.bhasaka.newsportal.core.services;

import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PressReleaseService
{
    private static final Logger LOG= LoggerFactory.getLogger("PressReleaseService.class");

    @Reference
    ArticleService articleService;

    @Activate
    public void activate()
    {
        LOG.info(articleService.articles());
        LOG.error("Inside Activate Method");
    }

    @Deactivate
    public void deActivate()
    {
        LOG.error("Inside DeActivate Method ");
    }

    @Modified
    public void update()
    {
        LOG.error("Inside Modified method ");
    }
}
