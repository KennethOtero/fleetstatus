package com.fleet.status.config;

public class UriConstants
{
    // Pages
    public static final String URI_HOME                 = "/";
    public static final String URI_AIRCRAFT_STATUS      = "/AircraftStatus";
    public static final String URI_HISTORY              = "/History";
    public static final String URI_LOGIN                = "/login";

    // Aircraft endpoints
    public static final String URI_AIRCRAFT             = "/v1/aircraft";
    public static final String URI_CARRIER              = "/v1/carrier";
    public static final String URI_TYPE                 = "/v1/type";

    // Event endpoints
    public static final String URI_EVENTS               = "/v1/events";
    public static final String URI_EVENTS_EVENT_ID      = "/v1/events/{eventId}";
    public static final String URI_OOS_EVENTS           = "/v1/OutOfServiceEvents";
    public static final String URI_EVENT_HISTORY        = "/v1/EventHistory";
    public static final String URI_SHOW_BACK_IN_SERVICE = "/v1/showBackInService/{eventId}";
    public static final String URI_REASON               = "/v1/reason";
    public static final String URI_CSV                  = "/v1/csv";
    public static final String URI_DOWNTIME_REPORT      = "/v1/DownTimeReport";

    // User endpoints
    public static final String URI_AUTH_STATUS          = "/v1/auth/status";
}
