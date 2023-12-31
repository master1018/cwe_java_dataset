package org.airsonic.player.security;
import org.airsonic.player.service.JWTSecurityService;
import org.airsonic.player.service.SecurityService;
import org.airsonic.player.service.SettingsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.security.SecureRandom;
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class GlobalSecurityConfig extends GlobalAuthenticationConfigurerAdapter {
    private static Logger logger = LoggerFactory.getLogger(GlobalSecurityConfig.class);
    static final String FAILURE_URL = "/login?error=1";
    private static final String key;
    static {
      byte[] array = new byte[32];
      new SecureRandom().nextBytes(array);
      key = new String(array);
    }
    @Autowired
    private SecurityService securityService;
    @Autowired
    private CsrfSecurityRequestMatcher csrfSecurityRequestMatcher;
    @Autowired
    SettingsService settingsService;
    @Autowired
    CustomUserDetailsContextMapper customUserDetailsContextMapper;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        if (settingsService.isLdapEnabled()) {
            auth.ldapAuthentication()
                    .contextSource()
                        .managerDn(settingsService.getLdapManagerDn())
                        .managerPassword(settingsService.getLdapManagerPassword())
                        .url(settingsService.getLdapUrl())
                    .and()
                    .userSearchFilter(settingsService.getLdapSearchFilter())
                    .userDetailsContextMapper(customUserDetailsContextMapper);
        }
        auth.userDetailsService(securityService);
        String jwtKey = settingsService.getJWTKey();
        if(StringUtils.isBlank(jwtKey)) {
            logger.warn("Generating new jwt key");
            jwtKey = JWTSecurityService.generateKey();
            settingsService.setJWTKey(jwtKey);
            settingsService.save();
        }
        auth.authenticationProvider(new JWTAuthenticationProvider(jwtKey));
    }
    @Configuration
    @Order(1)
    public class ExtSecurityConfiguration extends WebSecurityConfigurerAdapter {
        public ExtSecurityConfiguration() {
            super(true);
        }
        @Bean(name = "jwtAuthenticationFilter")
        public JWTRequestParameterProcessingFilter jwtAuthFilter() throws Exception {
            return new JWTRequestParameterProcessingFilter(authenticationManager(), FAILURE_URL);
        }
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http = http.addFilter(new WebAsyncManagerIntegrationFilter());
            http = http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
            http
                    .antMatcher("/ext/**")
                    .csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher).and()
                    .headers().frameOptions().sameOrigin().and()
                    .authorizeRequests()
                    .antMatchers("/ext/stream/**", "/ext/coverArt*", "/ext/share/**", "/ext/hls/**")
                    .hasAnyRole("TEMP", "USER").and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .exceptionHandling().and()
                    .securityContext().and()
                    .requestCache().and()
                    .anonymous().and()
                    .servletApi();
        }
    }
    @Configuration
    @Order(2)
    public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            RESTRequestParameterProcessingFilter restAuthenticationFilter = new RESTRequestParameterProcessingFilter();
            restAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
            restAuthenticationFilter.setSecurityService(securityService);
            restAuthenticationFilter.setEventPublisher(eventPublisher);
            http = http.addFilterBefore(restAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            http
                    .csrf()
                    .requireCsrfProtectionMatcher(csrfSecurityRequestMatcher)
                    .and().headers()
                    .frameOptions()
                    .sameOrigin()
                    .and().authorizeRequests()
                    .antMatchers("/recover*", "/accessDenied*",
                            "/style/**", "/icons/**", "/flash/**", "/script/**",
                            "/sonos/**", "/crossdomain.xml", "/login", "/error")
                    .permitAll()
                    .antMatchers("/personalSettings*", "/passwordSettings*",
                            "/playerSettings*", "/shareSettings*", "/passwordSettings*")
                    .hasRole("SETTINGS")
                    .antMatchers("/generalSettings*", "/advancedSettings*", "/userSettings*",
                            "/musicFolderSettings*", "/databaseSettings*", "/transcodeSettings*", "/rest/startScan*")
                    .hasRole("ADMIN")
                    .antMatchers("/deletePlaylist*", "/savePlaylist*", "/db*")
                    .hasRole("PLAYLIST")
                    .antMatchers("/download*")
                    .hasRole("DOWNLOAD")
                    .antMatchers("/upload*")
                    .hasRole("UPLOAD")
                    .antMatchers("/createShare*")
                    .hasRole("SHARE")
                    .antMatchers("/changeCoverArt*", "/editTags*")
                    .hasRole("COVERART")
                    .antMatchers("/setMusicFileInfo*")
                    .hasRole("COMMENT")
                    .antMatchers("/podcastReceiverAdmin*")
                    .hasRole("PODCAST")
                    .antMatchers("/**")
                    .hasRole("USER")
                    .anyRequest().authenticated()
                    .and().formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/index", true)
                    .failureUrl(FAILURE_URL)
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).logoutSuccessUrl(
                    "/login?logout")
                    .and().rememberMe().key(key);
        }
    }
}
