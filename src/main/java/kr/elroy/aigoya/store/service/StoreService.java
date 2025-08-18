package kr.elroy.aigoya.store.service;

import kr.elroy.aigoya.security.JwtTokenService;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.dto.request.CreateStoreRequest;
import kr.elroy.aigoya.store.dto.request.LoginRequest;
import kr.elroy.aigoya.store.dto.request.UpdatePasswordRequest;
import kr.elroy.aigoya.store.dto.request.UpdateStoreRequest;
import kr.elroy.aigoya.store.dto.response.LoginResponse;
import kr.elroy.aigoya.store.repository.StoreRepository;
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
                .password(encodedPassword)
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .dailyTarget(request.dailyTarget())
                .build();

        return storeRepository.save(store);
    }

    @Transactional(readOnly = true)
    public Store getStore(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게 ID입니다."));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Store store = storeRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.password(), store.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenService.generateToken(store);
        return LoginResponse.of(accessToken, store.getId(), store.getName(), store.getEmail());
    }

    @Transactional
    public Store updateStore(Long storeId, UpdateStoreRequest request) {
        Store store = getStore(storeId);

        store.setName(request.name());
        store.setPhone(request.phone());
        store.setAddress(request.address());
        store.setDailyTarget(request.dailyTarget());

        return store;
    }

    @Transactional
    public void updatePassword(Long storeId, UpdatePasswordRequest request) {
        Store store = getStore(storeId);

        if (!passwordEncoder.matches(request.currentPassword(), store.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (passwordEncoder.matches(request.newPassword(), store.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }

        store.setPassword(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional
    public void deleteStore(Long id) {
        Store store = getStore(id);
        storeRepository.delete(store);
    }
}