package com.ytaoCrow.ssooauth.config;

import com.ytaoCrow.ssooauth.config.provider.UserNameAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 處理授權授權用戶資訊 Service 類別
     */
    @Autowired
    UserDetailsService baseUserDetailsService;

    /**
     * 安全路徑過濾
     *
     * @param web
     * @throws Exception
     */

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/fonts/**", "/icon/**", "/images/**", "/favicon.ico");
    }

    /**
     * 開放部分授權認證入口服務的訪問限制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/check_token").and().authorizeRequests()
                .anyRequest().authenticated().and().formLogin().loginPage("/login").failureUrl("/login-error")
                .permitAll();
        http.csrf().disable();
    }

    /**
     * 管理認證配置
     *
     * @param auth
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * 授權使用者資訊的資料庫提供者物件配置。
     *
     * @return
     */
    @Bean
    public AbstractUserDetailsAuthenticationProvider daoAuthenticationProvider() {
        UserNameAuthenticationProvider authProvider = new UserNameAuthenticationProvider();
        // 設置 userDetailsService。
        authProvider.setUserDetailsService(baseUserDetailsService);
        // 禁止隱藏使用者未找到異常。
        authProvider.setHideUserNotFoundExceptions(false);
        // 使用 BCrypt 進行密碼的雜湊。
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(6));
        return authProvider;
    }
}
