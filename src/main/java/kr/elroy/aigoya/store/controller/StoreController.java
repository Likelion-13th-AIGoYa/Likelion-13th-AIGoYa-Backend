package kr.elroy.aigoya.store.controller;

import jakarta.validation.Valid;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.service.StoreService;
import kr.elroy.aigoya.store.api.StoreApi;
import kr.elroy.aigoya.store.dto.request.CreateStoreRequest;
import kr.elroy.aigoya.store.dto.request.LoginRequest;
import kr.elroy.aigoya.store.dto.response.LoginResponse;
import kr.elroy.aigoya.store.dto.response.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController implements StoreApi {
    private final StoreService storeService;

    @Override
    @GetMapping("/{id:[0-9]+}")
    public StoreResponse getStore(Long id) {
        Store store = storeService.getStore(id);
        return StoreResponse.of(store);
    }

    @Override
    public StoreResponse createStore(@Valid @RequestBody CreateStoreRequest request) {
        Store store = storeService.createStore(request);
        return StoreResponse.of(store);
    }

    @Override
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return storeService.login(request);
    }
}