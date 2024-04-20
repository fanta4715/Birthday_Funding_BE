package team.haedal.gifticionfunding.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import team.haedal.gifticionfunding.entity.user.UserRole;
import team.haedal.gifticionfunding.security.jwt.JwtAuthorizationFilter;
import team.haedal.gifticionfunding.security.jwt.JwtProvider;
import team.haedal.gifticionfunding.util.CustomResponseUtil;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager, jwtProvider));
            super.configure(builder);
        }

        public HttpSecurity build(){
            return getBuilder();
        }
    }

    @Bean
    public SecurityFilterChain filterChain(

            HttpSecurity http) throws Exception {
        http.headers(h -> h.frameOptions(f -> f.sameOrigin()));
        http.csrf(cf->cf.disable());

        http.sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.formLogin(f->f.disable());

        http.httpBasic(hb->hb.disable());

        http.with(new CustomSecurityFilterManager(), c-> c.build());

        // 인증 실패
        http.exceptionHandling(e-> e.authenticationEntryPoint((request, response, authException) -> {
            CustomResponseUtil.fail(response, "로그인을 진행해 주세요", HttpStatus.UNAUTHORIZED);
        }));

        http.exceptionHandling(e-> e.accessDeniedHandler((request, response, accessDeniedException) -> {
            CustomResponseUtil.fail(response, "권한이 없습니다", HttpStatus.FORBIDDEN);
        }));

        http.authorizeHttpRequests(c->
                c.requestMatchers("/api/s/**").authenticated()
                        .requestMatchers("/api/admin/**").hasRole(UserRole.ROLE_USER.getName())
                        .requestMatchers("/api/user/join").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().authenticated()
        );

        return http.build();
    }
}
