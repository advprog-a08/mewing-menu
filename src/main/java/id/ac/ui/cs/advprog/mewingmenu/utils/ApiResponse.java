package id.ac.ui.cs.advprog.mewingmenu.utils;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int page;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(boolean success, String message, T data, int page) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.page = page;
    }

    // Getters & Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
}
