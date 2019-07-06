package com.real.name.issue.entity;

public class FaceResult<T> {

    //0成功, 1失败
    private int result;

    private Boolean success;

    private T data;

    private String msg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FaceResult() {
    }

    public FaceResult(int result, Boolean success, T data, String msg) {
        this.result = result;
        this.success = success;
        this.data = data;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "FaceResult{" +
                "result=" + result +
                ", success=" + success +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
