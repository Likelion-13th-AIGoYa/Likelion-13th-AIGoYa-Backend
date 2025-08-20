package kr.elroy.aigoya.store.exception;

import kr.elroy.aigoya.exception.DomainException;
import kr.elroy.aigoya.exception.ErrorCode;

public class EmployeeNotFoundException extends DomainException {
    public EmployeeNotFoundException() {
        super(ErrorCode.EMPLOYEE_NOT_FOUND);
    }
}
