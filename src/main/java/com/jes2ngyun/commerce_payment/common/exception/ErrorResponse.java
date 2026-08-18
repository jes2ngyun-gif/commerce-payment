package com.jes2ngyun.commerce_payment.common.exception;

public record ErrorResponse(String code, String message) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }
}

// ErrorResponse - 응답 모양


// record ? : 위의 5줄이 아래와 완전히 같음.
//public class ErrorResponse {
//    private final String code;
//    private final String message;
//    public ErrorResponse(String code, String message) { ... }
//    public String code() { return code; }
//    public String message() { return message; }
//    public boolean equals(Object o) { ... }
//    public int hashCode() { ... }
//    public String toString() { ... }
//}

// 값만 담아 나르는 DTO에는 record가 정답.
// 필드가 전부 final이라 한 번 만들면 아무도 못 바꾼다.