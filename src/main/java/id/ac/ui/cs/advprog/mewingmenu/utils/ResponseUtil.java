package id.ac.ui.cs.advprog.mewingmenu.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(true, message, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message, int page) {
        ApiResponse<T> response = new ApiResponse<>(true, message, data, page);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(true, message, data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        ApiResponse<T> response = new ApiResponse<>(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(false, message, null);
        return new ResponseEntity<>(response, status);
    }
}
