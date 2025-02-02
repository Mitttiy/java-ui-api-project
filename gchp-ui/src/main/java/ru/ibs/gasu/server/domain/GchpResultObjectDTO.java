package ru.ibs.gasu.server.domain;

public class GchpResultObjectDTO {
    private boolean success;
    private Object result;
    private String msg = "";
    private String errorMsg = "";
    private int total;

    public GchpResultObjectDTO() {
    }

    public GchpResultObjectDTO(boolean success) {
        this.success = success;
    }

    public GchpResultObjectDTO(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public GchpResultObjectDTO(boolean success, String msg, String errorMsg, Object object) {
        this.success = success;
        this.msg = msg;
        this.errorMsg = errorMsg;
        this.result = object;
    }

    public GchpResultObjectDTO(boolean success, String msg, Object object) {
        this.success = success;
        this.msg = msg;
        this.result = object;
    }

    public GchpResultObjectDTO(Object object) {
        this.success = true;
        this.result = object;
    }

    public GchpResultObjectDTO(Object object, int total) {
        this.success = true;
        this.result = object;
        this.total = total;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
