package kr.elroy.aigoya.store.service;

import kr.elroy.aigoya.security.JwtTokenService;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.dto.request.CreateStoreRequest;
import kr.elroy.aigoya.store.dto.request.LoginRequest;
import kr.elroy.aigoya.store.dto.request.UpdatePasswordRequest;
import kr.elroy.aigoya.store.dto.request.UpdateStoreRequest;
import kr.elroy.aigoya.store.dto.response.LoginResponse;
import kr.elroy.aigoya.store.exception.StoreNotFoundException;
import kr.elroy.aigoya.store.exception.EmailAlreadyExistException;
import kr.elroy.aigoya.store.exception.InvalidEmailOrPasswordException;
import kr.elroy.aigoya.store.exception.CurrentPasswordMismatchException;
import kr.elroy.aigoya.store.exception.SamePasswordNotAllowedException;
import kr.elroy.aigoya.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public Store createStore(CreateStoreRequest request) {
        if (storeRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistException();
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
        return storeRepository.findById(id).orElseThrow(StoreNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Store store = storeRepository.findByEmail(request.email())
                .orElseThrow(InvalidEmailOrPasswordException::new);

        if (!passwordEncoder.matches(request.password(), store.getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }

        String accessToken = jwtTokenService.generateToken(store);
        return LoginResponse.of(accessToken, store.getId(), store.getName(), store.getEmail());
    }

    @Transactional
    public Store updateStore(Long storeId, UpdateStoreRequest request) {
        Store store = getStore(storeId);

        if (request.name() != null) {
            store.setName(request.name());
        }
        if (request.phone() != null) {
            store.setPhone(request.phone());
        }
        if (request.address() != null) {
            store.setAddress(request.address());
        }
        if (request.dailyTarget() != null) {
            store.setDailyTarget(request.dailyTarget());
        }

        return store;
    }

    @Transactional
    public void updatePassword(Long storeId, UpdatePasswordRequest request) {
        Store store = getStore(storeId);

        if (!passwordEncoder.matches(request.currentPassword(), store.getPassword())) {
            throw new CurrentPasswordMismatchException();
        }

        if (passwordEncoder.matches(request.newPassword(), store.getPassword())) {
            throw new SamePasswordNotAllowedException();
        }

        store.setPassword(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional
    public void deleteStore(Long id) {
        Store store = getStore(id);
        storeRepository.delete(store);
    }
}
