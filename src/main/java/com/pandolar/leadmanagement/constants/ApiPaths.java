package com.pandolar.leadmanagement.constants;

public final class ApiPaths {

    private ApiPaths() {
    }

    public static final String BASE_API = "/api";

    // Common path segments
    public static final String ID = "/{id}";

    // Lead endpoints
    public static final String LEADS = BASE_API + "/leads";
    public static final String LEAD_CONVERT = "/{id}/convert";

    // Customer endpoints
    public static final String CUSTOMERS = BASE_API + "/customers";
}
