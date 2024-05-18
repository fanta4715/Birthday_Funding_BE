package team.haedal.gifticionfunding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.haedal.gifticionfunding.annotation.UserId;
import team.haedal.gifticionfunding.dto.UserDto;
import team.haedal.gifticionfunding.dto.common.PagingResponse;
import team.haedal.gifticionfunding.dto.common.ResponseDto;
import team.haedal.gifticionfunding.service.FriendshipService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    @GetMapping("/v1/profile/friends")
    public ResponseEntity<?> getFriends(
            @UserId Long userId,
            @RequestParam(value = "depth", defaultValue = "1") Integer depth,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        PagingResponse<UserDto> userDtoPage = friendshipService.getFriends(userId, depth, page, size);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "친구 목록 조회 성공", userDtoPage),
                HttpStatus.OK
        );
    }

}
