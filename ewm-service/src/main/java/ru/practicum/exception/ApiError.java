package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
    private String errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}