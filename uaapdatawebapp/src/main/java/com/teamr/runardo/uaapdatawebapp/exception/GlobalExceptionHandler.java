package com.teamr.runardo.uaapdatawebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGlobalException(Exception exception, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + exception.getMessage());
        return "error";
    }

    // Handle 404 errors (Page not found)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", "Page not found (404): " + ex.getMessage());
        return "error"; // You can have a separate error404.html for 404 if needed
    }

    @ModelAttribute("defaultErrorMessage")
    public String defaultErrorMessage() {
        return "Something went wrong! Please try again later.";
    }
}
