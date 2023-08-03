
package org.hswebframework.web.oauth2.core;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
public enum ErrorType {
    ILLEGAL_CODE(1001), 
    ILLEGAL_ACCESS_TOKEN(1002), 
    ILLEGAL_CLIENT_ID(1003),
    ILLEGAL_CLIENT_SECRET(1004),
    ILLEGAL_GRANT_TYPE(1005), 
    ILLEGAL_RESPONSE_TYPE(1006),
    ILLEGAL_AUTHORIZATION(1007),
    ILLEGAL_REFRESH_TOKEN(1008),
    ILLEGAL_REDIRECT_URI(1009), 
    ILLEGAL_SCOPE(1010), 
    ILLEGAL_USERNAME(1011), 
    ILLEGAL_PASSWORD(1012), 
    SCOPE_OUT_OF_RANGE(2010), 
    UNAUTHORIZED_CLIENT(4010), 
    EXPIRED_TOKEN(4011), 
    INVALID_TOKEN(4012), 
    UNSUPPORTED_GRANT_TYPE(4013), 
    UNSUPPORTED_RESPONSE_TYPE(4014), 
    EXPIRED_CODE(4015), 
    EXPIRED_REFRESH_TOKEN(4020), 
    CLIENT_DISABLED(4016),
    CLIENT_NOT_EXIST(4040),
    USER_NOT_EXIST(4041),
    STATE_ERROR(4042), 
    ACCESS_DENIED(503), 
    OTHER(5001), 
    PARSE_RESPONSE_ERROR(5002),
    SERVICE_ERROR(5003); 
    private final String message;
    private final int    code;
    static final Map<Integer, ErrorType> codeMapping = Arrays.stream(ErrorType.values())
            .collect(Collectors.toMap(ErrorType::code, type -> type));
    ErrorType(int code) {
        this.code = code;
        message = this.name().toLowerCase();
    }
    ErrorType(int code, String message) {
        this.message = message;
        this.code = code;
    }
    public String message() {
        if (message == null) {
            return this.name();
        }
        return message;
    }
    public int code() {
        return code;
    }
    public <T> T throwThis(Function<ErrorType, ? extends RuntimeException> errorTypeFunction) {
        throw errorTypeFunction.apply(this);
    }
    public <T> T throwThis(BiFunction<ErrorType, String, ? extends RuntimeException> errorTypeFunction, String message) {
        throw errorTypeFunction.apply(this, message);
    }
    public static Optional<ErrorType> fromCode(int code) {
        return Optional.ofNullable(codeMapping.get(code));
    }
}
