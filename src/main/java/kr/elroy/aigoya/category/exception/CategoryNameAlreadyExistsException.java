package kr.elroy.aigoya.category.exception;

import kr.elroy.aigoya.exception.DomainException;
import kr.elroy.aigoya.exception.ErrorCode;

public class CategoryNameAlreadyExistsException extends DomainException {
    public CategoryNameAlreadyExistsException() {
        super(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS);
    }
}
