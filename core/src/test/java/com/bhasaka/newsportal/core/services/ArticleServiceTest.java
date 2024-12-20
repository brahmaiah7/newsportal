package com.bhasaka.newsportal.core.services;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ArticleServiceTest {

    @Mock
    ArticleConfiguration configuration;

    ArticleService articleService=new ArticleService();

    @BeforeEach
    void testCases()
    {
        Mockito.when(configuration.restApi()).thenReturn("https://gorest.co.in/public/v2/posts");
        Mockito.when(configuration.clientId()).thenReturn("19HR1A0536");
        Mockito.when(configuration.enable()).thenReturn(true);
        articleService.activate(configuration);
    }

    @Test
    void lifeCycleEvents()
    {
        articleService.activate(configuration);
        articleService.deActivate(configuration);
        articleService.update(configuration);
    }

    @Test
    void valuesTest()
    {
        assertEquals("19HR1A0536",articleService.getClientId());
    }
}