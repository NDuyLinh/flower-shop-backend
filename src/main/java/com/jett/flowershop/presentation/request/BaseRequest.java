package com.jett.flowershop.presentation.request;

import java.time.LocalDateTime;

public class BaseRequest<T> {

    private String requestTrace;
    private LocalDateTime requestDateTime;
    private T requestParameter;

    public BaseRequest() {
    }

    public BaseRequest(String requestTrace, LocalDateTime requestDateTime, T requestParameter) {
        this.requestTrace = requestTrace;
        this.requestDateTime = requestDateTime;
        this.requestParameter = requestParameter;
    }

    public String getRequestTrace() {
        return requestTrace;
    }

    public void setRequestTrace(String requestTrace) {
        this.requestTrace = requestTrace;
    }

    public LocalDateTime getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(LocalDateTime requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public T getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(T requestParameter) {
        this.requestParameter = requestParameter;
    }

    // Convenience method
    public T getData() {
        return requestParameter;
    }
}
