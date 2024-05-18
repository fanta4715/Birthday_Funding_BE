package team.haedal.gifticionfunding.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.haedal.gifticionfunding.dto.UserDto;
import team.haedal.gifticionfunding.dto.common.PagingResponse;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.handler.ex.CustomApiException;
import team.haedal.gifticionfunding.repository.user.FriendshipRepository;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public PagingResponse<UserDto> getFriends(Long userId, Integer depth, Integer page, Integer size) {
        if (page < 0 || size <= 0) {
            throw new CustomApiException("page는 0이상, size는 1이상이어야 합니다.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("nickname").ascending());

        // 친구 목록 조회
        if (depth == 1) {
            Page<User> frinedPage = friendshipRepository.findFriendsByUserId(userId, pageable);
            return PagingResponse.<UserDto>builder()
                    .hasNext(frinedPage.hasNext())
                    .data(frinedPage.getContent().stream()
                            .map(UserDto::fromEntity)
                            .toList())
                    .build();
        }

        // 친구의 친구까지 목록 조회
        if (depth == 2) {
            Page<User> friendPage = friendshipRepository.findFriendsAndFriendsOfFriends(userId, pageable);
            return PagingResponse.<UserDto>builder()
                    .hasNext(friendPage.hasNext())
                    .data(friendPage.getContent().stream()
                            .map(UserDto::fromEntity)
                            .toList())
                    .build();
        }
        throw new CustomApiException("depth는 1또는 2만 가능합니다.");
    }
}
