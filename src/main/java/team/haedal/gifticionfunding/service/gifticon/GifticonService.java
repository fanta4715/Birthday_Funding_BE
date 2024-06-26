package team.haedal.gifticionfunding.service.gifticon;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import team.haedal.gifticionfunding.dto.common.PagingResponse;
import team.haedal.gifticionfunding.dto.gifticon.request.GifticonPurchaseRequest;
import team.haedal.gifticionfunding.dto.gifticon.response.GifticonDetailResponse;
import team.haedal.gifticionfunding.dto.gifticon.response.GifticonDto;
import team.haedal.gifticionfunding.dto.gifticon.response.UserGifticonDto;
import team.haedal.gifticionfunding.entity.gifticon.Gifticon;
import team.haedal.gifticionfunding.entity.gifticon.UserGifticon;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.entity.user.UserRole;
import team.haedal.gifticionfunding.handler.ex.CustomApiException;
import team.haedal.gifticionfunding.repository.gifticon.GifticonRepository;
import team.haedal.gifticionfunding.repository.user.UserGifticonRepository;
import team.haedal.gifticionfunding.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class GifticonService {
    private final GifticonRepository gifticonRepository;
    private final UserGifticonRepository userGifticonRepository;
    private final UserRepository userRepository;

    public PagingResponse<GifticonDto> getGifticonPaging(int page, int size, String search, String criteria, String sort, String category) {
        Direction direction = Direction.fromString(sort);
        Page<Gifticon> gifticonPage =  gifticonRepository.findAllWithFiltersAndSearch(
                search,
                category,
                PageRequest.of(page, size, Sort.by(direction, criteria))
        );

        return PagingResponse.<GifticonDto>builder()
                .hasNext(gifticonPage.hasNext())
                .data(gifticonPage.map(GifticonDto::from).toList())
                .build();
    }

    public GifticonDetailResponse getGifticonDetail(Long gifticonId) {
        Optional<Gifticon> gifticon = gifticonRepository.findById(gifticonId);
        if (gifticon.isEmpty()) {
            throw new CustomApiException("존재하지 않는 기프티콘입니다.");
        }
        return GifticonDetailResponse.from(gifticon.get());
    }

    public List<UserGifticonDto> purchaseGifticon(Long gifticonId, GifticonPurchaseRequest gifticonPurchaseRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다."));

        Optional<Gifticon> gifticon = gifticonRepository.findById(gifticonId);
        if (gifticon.isEmpty()) {
            throw new CustomApiException("존재하지 않는 기프티콘입니다.");
        }
        List<UserGifticon> userGifticons = IntStream.range(0, gifticonPurchaseRequest.quantity())
                .mapToObj(i -> UserGifticon.builder()
                        .gifticon(gifticon.get())
                        .buyer(user)
                        .owner(user)
                        .expirationDate(LocalDate.now().plusDays(gifticon.get().getExpirationPeriod()))
                        .build())
                .collect(Collectors.toList());

        List<UserGifticon> purchasedGifticons = userGifticonRepository.saveAll(userGifticons);
        return purchasedGifticons.stream()
                .map(UserGifticonDto::from)
                .toList();
    }
}
