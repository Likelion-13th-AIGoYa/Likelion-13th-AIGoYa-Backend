package kr.elroy.aigoya.category.exception;

import kr.elroy.aigoya.exception.DomainException;
import kr.elroy.aigoya.exception.ErrorCode;

public class CategoryInUseException extends DomainException {
    public CategoryInUseException() {
        super(ErrorCode.CATEGORY_IN_USE);
    }
}
