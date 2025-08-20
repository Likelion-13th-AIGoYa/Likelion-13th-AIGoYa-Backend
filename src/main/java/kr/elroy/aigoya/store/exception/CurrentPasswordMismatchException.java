package kr.elroy.aigoya.store.exception;

import kr.elroy.aigoya.exception.DomainException;
import kr.elroy.aigoya.exception.ErrorCode;

public class CurrentPasswordMismatchException extends DomainException {
    public CurrentPasswordMismatchException() {
        super(ErrorCode.CURRENT_PASSWORD_MISMATCH);
    }
}