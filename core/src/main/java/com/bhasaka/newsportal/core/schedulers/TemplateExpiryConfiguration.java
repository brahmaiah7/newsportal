package com.bhasaka.newsportal.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface TemplateExpiryConfiguration
{
    @AttributeDefinition(name ="corn Expression")
    public String cornExpression() default "*/10 * * ? * *";

    @AttributeDefinition(name =" Scheduler Name")
    public String schedulerName() default "template-expiry";

    @AttributeDefinition(name="Enable/disable")
    public boolean enable() default true;
}
