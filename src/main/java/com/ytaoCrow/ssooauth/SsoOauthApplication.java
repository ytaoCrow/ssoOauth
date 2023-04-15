package com.ytaoCrow.ssooauth;

import com.ytaoCrow.ssooauth.client.ResourceServerClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.SessionAttributes;

@SpringBootApplication
@EnableDiscoveryClient
@SessionAttributes("authorizationRequest")
@EnableFeignClients(basePackageClasses = ResourceServerClient.class)
public class SsoOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoOauthApplication.class, args);
    }

}
