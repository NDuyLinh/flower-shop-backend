package com.jett.flowershop.presentation.response;

/**
 * Response status object.
 *
 * Fields:
 * - responseCode
 * - responseMessage
 */
public class ResponseStatus {

    private String responseCode;
    private String responseMessage;

    public ResponseStatus() {
    }

    public ResponseStatus(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
