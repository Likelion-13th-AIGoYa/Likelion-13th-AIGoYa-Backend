package kr.elroy.aigoya.store;

import kr.elroy.aigoya.security.JwtTokenService;
import kr.elroy.aigoya.store.dto.request.CreateStoreRequest;
import kr.elroy.aigoya.store.dto.request.LoginRequest;
import kr.elroy.aigoya.store.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public Store createStore(CreateStoreRequest request) {
        if (storeRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if (storeRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("이미 사용 중인 가게 이름입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Store store = Store.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .build();

        return storeRepository.save(store);
    }

    @Transactional(readOnly = true)
    public Store getStore(Long id) {
        return storeRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게 ID입니다."));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Store store = storeRepository.findByEmail(request.email());
        if (store == null) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }

        if (!passwordEncoder.matches(request.password(), store.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenService.generateToken(store);
        return LoginResponse.of(accessToken, store.getId(), store.getName(), store.getEmail());
    }
}
