package kr.elroy.aigoya.store.controller;

import kr.elroy.aigoya.store.api.MyStoreApi;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.dto.request.UpdatePasswordRequest;
import kr.elroy.aigoya.store.dto.request.UpdateStoreRequest;
import kr.elroy.aigoya.store.dto.response.StoreResponse;
import kr.elroy.aigoya.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyStoreController implements MyStoreApi {

    private final StoreService storeService;

    @Override
    public StoreResponse getMyStore(Long storeId) {
        Store store = storeService.getStore(storeId);
        return StoreResponse.of(store);
    }

    @Override
    public StoreResponse updateMyStore(
            Long storeId,
            UpdateStoreRequest request
    ) {
        Store updatedStore = storeService.updateStore(storeId, request);
        return StoreResponse.of(updatedStore);
    }

    @Override
    public void deleteMyStore(Long storeId) {
        storeService.deleteStore(storeId);
    }

    @Override
    public void updatePassword(Long storeId, UpdatePasswordRequest request) {
        storeService.updatePassword(storeId, request);
    }
}