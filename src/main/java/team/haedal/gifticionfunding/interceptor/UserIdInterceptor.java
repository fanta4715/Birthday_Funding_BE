package team.haedal.gifticionfunding.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import team.haedal.gifticionfunding.security.oauth.PrincipalDetails;

@Slf4j
@Component
public class UserIdInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //request객체에 UserId 값 추가
        request.setAttribute("userId", principalDetails.getId());
        log.info("userId: {}", principalDetails.getId());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
