package team.haedal.gifticionfunding.security.oauth;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.repository.user.UserRepository;
import team.haedal.gifticionfunding.security.oauth.PrincipalDetails;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }

}

