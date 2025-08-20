package kr.elroy.aigoya.store.exception;

import kr.elroy.aigoya.exception.DomainException;
import kr.elroy.aigoya.exception.ErrorCode;

public class StoreNotFoundException extends DomainException {
    public StoreNotFoundException() {
        super(ErrorCode.STORE_NOT_FOUND);
    }
}
