package kr.elroy.aigoya.exception;

public class AccessDeniedException extends DomainException {
    public AccessDeniedException() {
        super(ErrorCode.ACCESS_DENIED);
    }
}
