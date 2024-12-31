package org.example.user.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.user.filter.jwtAuthenticationFilter;
import org.example.user.handler.AccessDenied;
import org.example.user.handler.AuthenticationEntryHandler;
import org.example.user.mapper.CommonMapper;
import org.example.user.service.Impl.UserService;
import org.example.user.utils.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private CommonMapper commonMapper;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserService userService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/users/login").permitAll()
                                .requestMatchers("/users/logout").permitAll()
                                .requestMatchers("/users/getImage").permitAll()
//                        .requestMatchers("/good/getGoodsList").permitAll()
                                .anyRequest()
                                .access((authenticationSupplier, requestAuthorizationContext) -> {
                                    //手动注入mapper
                                    if (commonMapper == null) {
                                        commonMapper = SpringUtils.getBean(CommonMapper.class);
                                    }
                                    // 当前用户的权限信息 比如角色
                                    Collection<? extends GrantedAuthority> authorities = authenticationSupplier.get().getAuthorities();
                                    // 我们可以获取原始request对象
                                    HttpServletRequest request = requestAuthorizationContext.getRequest();
                                    //获取访问路径并截取有效部分
                                    String url = String.valueOf(request.getRequestURL());
                                    url = url.substring(url.substring(0, url.lastIndexOf("/")).lastIndexOf("/"));
                                    System.out.println(url);
                                    //获取路径对应的权限信息
                                    List<String> roles = commonMapper.getRoles(url);
                                    boolean isGranted = false;
                                    //判断，若角色权限符合其中一个就设isGranted为true
                                    for (String role : roles) {
                                        for (GrantedAuthority authority : authorities) {
                                            if (authority.getAuthority().equals(role)) {
                                                isGranted = true;
                                                break;
                                            }
                                        }
                                    }
                                    return new AuthorizationDecision(isGranted);
                                })
                )
                .csrf(csrf -> csrf.disable())
                .httpBasic(withDefaults());
        http
                .addFilterBefore(new jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new AuthenticationEntryHandler())
                        .accessDeniedHandler(new AccessDenied()));
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*"); // 这个得加上，一些复杂的请求方式会带有header，不加上跨域会失效。
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("*");
        corsConfiguration.addAllowedOriginPattern("http://localhost:5173");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


}
