package com.jett.flowershop.presentation.response;

import java.time.LocalDateTime;

public class BaseResponse<T> {

    private LocalDateTime responseDateTime;
    private ResponseStatus responseStatus;
    private T responseData;

    public BaseResponse() {
    }

    public BaseResponse(LocalDateTime responseDateTime, ResponseStatus responseStatus, T responseData) {
        this.responseDateTime = responseDateTime;
        this.responseStatus = responseStatus;
        this.responseData = responseData;
    }

    public LocalDateTime getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(LocalDateTime responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }

    // Convenience factory methods
    public static <T> BaseResponse<T> success(T data, String message) {
        ResponseStatus status = new ResponseStatus("00", message);
        return new BaseResponse<>(LocalDateTime.now(), status, data);
    }

    public static <T> BaseResponse<T> error(String code, String message) {
        ResponseStatus status = new ResponseStatus(code, message);
        return new BaseResponse<>(LocalDateTime.now(), status, null);
    }
}
