package kr.elroy.aigoya.analytics.exception;

import kr.elroy.aigoya.exception.DomainException;
import kr.elroy.aigoya.exception.ErrorCode;

public class InvalidAnalysisParameterException extends DomainException {
    public InvalidAnalysisParameterException() {
        super(ErrorCode.INVALID_ANALYSIS_PARAMETER);
    }
}
