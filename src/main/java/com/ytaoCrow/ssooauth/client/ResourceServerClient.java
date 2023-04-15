package com.ytaoCrow.ssooauth.client;

import com.ytaoCrow.ssooauth.client.bo.CheckPassWordBO;
import com.ytaoCrow.ssooauth.client.dto.CheckPassWordDTO;
import com.ytaoCrow.ssooauth.entity.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "sso-resourceserver", configuration = ResourceServerConfiguration.class, fallbackFactory = ResourceServerFallbackFactory.class)
public interface ResourceServerClient {

    /**
     * 登录密码验证接口
     *
     * @param checkPassWordDTO
     * @return
     */
    @PostMapping("/auth/checkPassWord")
    public ResponseResult<CheckPassWordBO> checkPassWord(CheckPassWordDTO checkPassWordDTO);
}
