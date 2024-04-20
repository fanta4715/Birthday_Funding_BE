package team.haedal.gifticionfunding.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.haedal.gifticionfunding.interceptor.UserIdArgumentResolver;
import team.haedal.gifticionfunding.interceptor.UserIdInterceptor;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserIdArgumentResolver userIdArgumentResolver;
    private final UserIdInterceptor userIdInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(this.userIdArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(userIdInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/oauth2/code/kakao",
                        "/api/auth/register",
                        "/oauth/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**");
    }
}
