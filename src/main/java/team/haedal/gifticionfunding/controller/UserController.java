package team.haedal.gifticionfunding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.haedal.gifticionfunding.dto.common.ResponseDto;
import team.haedal.gifticionfunding.dto.user.request.UserJoinRequest;
import team.haedal.gifticionfunding.dto.user.response.UserJoinResponse;
import team.haedal.gifticionfunding.service.user.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserJoinResponse userJoinResponse = userService.join(userJoinRequest);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "회원가입 성공", userJoinResponse),
                HttpStatus.CREATED
        );
    }
}
