package kr.elroy.aigoya.store.controller;

import kr.elroy.aigoya.store.api.StoreApi;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.dto.request.CreateStoreRequest;
import kr.elroy.aigoya.store.dto.request.LoginRequest;
import kr.elroy.aigoya.store.dto.response.LoginResponse;
import kr.elroy.aigoya.store.dto.response.StoreResponse;
import kr.elroy.aigoya.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController implements StoreApi {
    private final StoreService storeService;

    @Override
    public StoreResponse createStore(CreateStoreRequest request) {
        Store store = storeService.createStore(request);
        return StoreResponse.of(store);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return storeService.login(request);
    }
}