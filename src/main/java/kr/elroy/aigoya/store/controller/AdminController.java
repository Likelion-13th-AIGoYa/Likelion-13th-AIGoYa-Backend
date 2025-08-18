package kr.elroy.aigoya.store.controller;

import kr.elroy.aigoya.store.api.AdminApi;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.dto.response.StoreResponse;
import kr.elroy.aigoya.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final StoreService storeService;

    @Override
    public StoreResponse getStore(Long id) {
        Store store = storeService.getStore(id);

        return StoreResponse.of(store);
    }
}