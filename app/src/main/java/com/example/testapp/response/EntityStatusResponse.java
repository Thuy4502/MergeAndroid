package com.example.testapp.response;

public class EntityStatusResponse<T>{
    private T data;
    private int status;
    private String message;
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public EntityStatusResponse(T data, int status, String message) {
        super();
        this.data = data;
        this.status = status;
        this.message = message;
    }
    public EntityStatusResponse() {
        super();
        // TODO Auto-generated constructor stub
    }
}
