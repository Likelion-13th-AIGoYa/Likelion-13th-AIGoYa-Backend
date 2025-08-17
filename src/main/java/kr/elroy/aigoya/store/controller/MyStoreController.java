package kr.elroy.aigoya.store.controller;

import jakarta.validation.Valid;
import kr.elroy.aigoya.store.api.MyStoreApi;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.dto.request.UpdatePasswordRequest;
import kr.elroy.aigoya.store.dto.request.UpdateStoreRequest;
import kr.elroy.aigoya.store.dto.response.StoreResponse;
import kr.elroy.aigoya.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyStoreController implements MyStoreApi {

    private final StoreService storeService;

    @Override
    public StoreResponse getMyStore(@AuthenticationPrincipal(expression = "id") Long storeId) {
        Store store = storeService.getStore(storeId);
        return StoreResponse.of(store);
    }

    @Override
    public StoreResponse updateMyStore(
            @AuthenticationPrincipal(expression = "id") Long storeId,
            @Valid @RequestBody UpdateStoreRequest request
    ) {
        Store updatedStore = storeService.updateStore(storeId, request);
        return StoreResponse.of(updatedStore);
    }

    @Override
    public void deleteMyStore(@AuthenticationPrincipal(expression = "id") Long storeId) {
        storeService.deleteStore(storeId);
    }

    @PutMapping("/v1/stores/me/password")
    public void updatePassword(
            @AuthenticationPrincipal(expression = "id") Long storeId,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        storeService.updatePassword(storeId, request);
    }
}