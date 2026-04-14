package com.pandolar.leadmanagement.constants;

public final class ErrorMessages {

    private ErrorMessages() {
    }

    public static final String LEAD_NOT_FOUND = "Lead not found with id: ";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found with id: ";
    public static final String LEAD_ALREADY_CONVERTED = "Lead with id %s has already been converted";
    public static final String CANNOT_SET_CONVERTED = "Cannot set status to CONVERTED directly. Use the conversion endpoint.";
    public static final String DUPLICATE_RECORD = "A record with the given details already exists";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
    public static final String VALIDATION_FAILED = "Validation failed";
}
