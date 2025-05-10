package com.assignment.price_grid.payload.Response;

public class CreatePriceGridResponse {

    private boolean msg;

    private int errorCode;

    public CreatePriceGridResponse(boolean msg, int errorCode) {
        this.msg = msg;
        this.errorCode = errorCode;
    }

    public boolean isMsg() {
        return msg;
    }

    public void setMsg(boolean msg) {
        this.msg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
