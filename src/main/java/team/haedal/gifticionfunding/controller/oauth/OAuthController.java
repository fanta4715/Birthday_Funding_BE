package team.haedal.gifticionfunding.controller.oauth;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.haedal.gifticionfunding.dto.common.JwtTokensDto;
import team.haedal.gifticionfunding.dto.oauth.KakaoUserInfo;
import team.haedal.gifticionfunding.service.oauth.OAuthKakaoService;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthKakaoService oAuthKakaoService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> loginByKakaoCallback(
            @RequestParam("code") String code
    ) {
        //1. 해당 코드로 카카오에게 토큰을 요청
        String tokenByKakao = oAuthKakaoService.getAccessToken(code);

        //2. 토큰을 받아서 유저 정보를 가져옴
        KakaoUserInfo userInfo = oAuthKakaoService.getUserInfoByAccessToken(tokenByKakao);

        //3. 유저 정보를 토대로 회원가입 or 로그인
        JwtTokensDto jwtDto = oAuthKakaoService.loginOrRegister(userInfo);
        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }
}
