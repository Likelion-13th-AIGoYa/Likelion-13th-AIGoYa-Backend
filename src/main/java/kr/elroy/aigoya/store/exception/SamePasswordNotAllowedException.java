package kr.elroy.aigoya.store.exception;

import kr.elroy.aigoya.exception.DomainException;
import kr.elroy.aigoya.exception.ErrorCode;

public class SamePasswordNotAllowedException extends DomainException {
    public SamePasswordNotAllowedException() {
        super(ErrorCode.SAME_PASSWORD_NOT_ALLOWED);
    }
}