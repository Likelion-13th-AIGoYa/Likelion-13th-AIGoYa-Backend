package kr.elroy.aigoya.store.exception;

import kr.elroy.aigoya.exception.DomainException;
import kr.elroy.aigoya.exception.ErrorCode;

public class NotificationNotFoundException extends DomainException {
    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
}
