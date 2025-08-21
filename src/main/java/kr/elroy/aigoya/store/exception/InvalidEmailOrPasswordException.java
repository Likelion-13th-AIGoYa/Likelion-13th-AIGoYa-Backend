package kr.elroy.aigoya.store.exception;

import kr.elroy.aigoya.exception.DomainException;
import kr.elroy.aigoya.exception.ErrorCode;

public class InvalidEmailOrPasswordException extends DomainException {
    public InvalidEmailOrPasswordException() {
        super(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
    }
}