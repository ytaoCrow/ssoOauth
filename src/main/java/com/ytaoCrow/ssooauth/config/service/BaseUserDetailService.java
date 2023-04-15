package com.ytaoCrow.ssooauth.config.service;
import com.ytaoCrow.ssooauth.client.ResourceServerClient;
import com.ytaoCrow.ssooauth.client.bo.CheckPassWordBO;
import com.ytaoCrow.ssooauth.client.dto.CheckPassWordDTO;
import com.ytaoCrow.ssooauth.entity.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseUserDetailService implements UserDetailsService {
    /**
     *將資源微服務的FeignClient介面注入
     */
    @Autowired
    ResourceServerClient resourceServerClient;
    @Override
    public UserDetails loadUserByUsername(String usernsme) throws UsernameNotFoundException {
        CheckPassWordDTO checkPassWordDTO = CheckPassWordDTO.builder().userName(usernsme).build();
        ResponseResult<CheckPassWordBO> responseResult = resourceServerClient.checkPassWord(checkPassWordDTO);
        CheckPassWordBO checkPassWordBO = responseResult.getData();
        List<GrantedAuthority> authorities = new ArrayList<>();

        //回傳帶有用戶權限訊息的User
        User user = new User(checkPassWordBO.getUserName(),
                checkPassWordBO.getPassWord()+","+checkPassWordBO.getSalt(), true, true, true,true, authorities);
        return user;
    }
}
