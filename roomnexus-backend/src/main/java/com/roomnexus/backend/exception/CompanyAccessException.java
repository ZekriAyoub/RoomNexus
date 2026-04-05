package com.roomnexus.backend.exception;

public class CompanyAccessException extends RuntimeException {

    public CompanyAccessException() {
        super("Access denied: resource does not belong to your company");
    }
}