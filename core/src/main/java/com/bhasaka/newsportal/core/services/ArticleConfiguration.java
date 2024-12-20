package com.bhasaka.newsportal.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ArticleConfiguration
{
    @AttributeDefinition(name = "Article service Rest API")
    public String restApi() default "https://gorest.co.in/public/v2/posts";

    @AttributeDefinition(name = "Enable/Disable Article Service Rest API")
    public boolean enable() default true;

    @AttributeDefinition(name = "clientId")
    public String clientId() default "19HR1A0536";
}
