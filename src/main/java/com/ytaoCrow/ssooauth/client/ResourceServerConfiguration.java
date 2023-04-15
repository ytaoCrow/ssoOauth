package com.ytaoCrow.ssooauth.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ResourceServerConfiguration {

    @Bean
    ResourceServerFallbackFactory resourceServerFallbackFactory() {
        return new ResourceServerFallbackFactory();
    }
}
