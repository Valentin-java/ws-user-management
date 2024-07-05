package com.workers.wsusermanagement.config.common;

import com.workers.wsusermanagement.config.resttemplate.interceptor.RTTokenInterceptorConfiguration;
import com.workers.wsusermanagement.config.resttemplate.interceptor.RestTemplateClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplateClient getInterceptedRestTemplate(RTTokenInterceptorConfiguration restClientInterceptor) {
        return new RestTemplateClient(restClientInterceptor);
    }
}
