package com.ytaoCrow.ssooauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {

    /**
     *
     * JDBC 資料來源依賴
     */
    @Autowired
    private DataSource dataSource;
    /**
     * 授權認證管理介面
     */

    AuthenticationManager authenticationManager;

    /**
     * 建構方法
     *
     * @param authenticationConfiguration
     * @throws Exception
     */
    public AuthServerConfiguration(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 使用JDBC資料庫方式實現客戶端訊息管理
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)throws Exception{
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    /**
     * 授權認證服務器 相關服務端點配置
     *
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints){

        //配置TokenService參數
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(getJdbcTokenStore());

        //支援Token刷新
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);

        //accessToken有效時間 設置為30天
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));

        //refreshToken有效時間 設置為15天
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(15));
        tokenServices.setClientDetailsService(getJdbcClientDetailsService());

        //管理資料庫授權訊息
        endpoints.authenticationManager(this.authenticationManager).accessTokenConverter(jwtAccessTokenConverter())
                .tokenStore(getJdbcTokenStore()).tokenServices(tokenServices)
                .authorizationCodeServices(getJdbcAuthorizationCodeServices()).approvalStore(getJdbcApprovalStore());
    }
    /**
     * 安全約束設定
     *
     * @param security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        security.tokenKeyAccess("permitAll()").checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                .allowFormAuthenticationForClients();
    }

    /**
     * 資料庫管理金鑰實例
     * @return
     */
    @Bean
    public JdbcTokenStore getJdbcTokenStore(){
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 管理授權碼訊息
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices getJdbcAuthorizationCodeServices(){
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 管理客戶端資料庫訊息
     *
     * @return
     */
    @Bean
    public ClientDetailsService getJdbcClientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }
    /**
     * 管理資料庫用戶授權確認紀錄
     *
     * @return
     */
    @Bean
    public ApprovalStore getJdbcApprovalStore(){
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * accessToken發佈管理(使用非對稱加密來對Token進行加密)
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        //導入認證資料

        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"),
                "mypass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
        return converter;
    }


}
