package com.workers.wsusermanagement.config.common;

import com.workers.wsusermanagement.config.props.CommonRequestProperties;
import com.workers.wsusermanagement.config.resttemplate.interceptor.RTTokenInterceptorConfiguration;
import com.workers.wsusermanagement.config.resttemplate.interceptor.RestTemplateClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

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

    @Bean
    @RequestScope
    public CommonRequestProperties getCommonProps() {
        return new CommonRequestProperties();
    }
}
