package kr.elroy.aigoya.store;

import jakarta.validation.Valid;
import kr.elroy.aigoya.store.dto.CreateStoreRequest;
import kr.elroy.aigoya.store.dto.LoginRequest;
import kr.elroy.aigoya.store.dto.LoginResponse;
import kr.elroy.aigoya.store.dto.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController implements StoreApi {
    private final StoreService storeService;

    @Override
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