package com.example.testapp.response;

import java.util.List;

public class CommonResponse<T> {
    private List<T> data;
    private int status;
    private String message;

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
    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }
    public CommonResponse(List<T> data, int status, String message) {
        super();
        this.data = data;
        this.status = status;
        this.message = message;
    }
    public CommonResponse() {
        super();
        // TODO Auto-generated constructor stub
    }

}
