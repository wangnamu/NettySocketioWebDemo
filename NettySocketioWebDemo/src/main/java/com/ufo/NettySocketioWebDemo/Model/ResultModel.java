package com.ufo.NettySocketioWebDemo.Model;

public class ResultModel {
    private boolean IsSuccess;
    private Object Data;
    private String ErrorMessage;

    public ResultModel() {

    }

    public ResultModel(boolean isSuccess, Object data, String errorMessage) {
        IsSuccess = isSuccess;
        Data = data;
        ErrorMessage = errorMessage;
    }

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        IsSuccess = isSuccess;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }
}
