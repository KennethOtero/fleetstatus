package com.fleet.status.config;

public class UriConstants
{
    // Endpoint versions
    private static final String V1                        = "/v1";

    // Pages
    public static final String URI_HOME                   = "/";
    public static final String URI_AIRCRAFT_STATUS        = "/AircraftStatus";
    public static final String URI_HISTORY                = "/History";
    public static final String URI_LOGIN                  = "/login";

    // Aircraft endpoints
    public static final String URI_AIRCRAFT               = V1 + "/aircraft";
    public static final String URI_CARRIER                = V1 + "/carrier";
    public static final String URI_TYPE                   = V1 + "/type";

    // Event endpoints
    public static final String URI_EVENTS                 = V1 + "/events";
    public static final String URI_EVENTS_EVENT_ID        = V1 + "/events/{eventId}";
    public static final String URI_OOS_EVENTS             = V1 + "/OutOfServiceEvents";
    public static final String URI_EVENT_HISTORY          = V1 + "/EventHistory";
    public static final String URI_SHOW_BACK_IN_SERVICE   = V1 + "/showBackInService/{eventId}";
    public static final String URI_REASON                 = V1 + "/reason";
    public static final String URI_CSV                    = V1 + "/csv";
    public static final String URI_DOWNTIME_REPORT        = V1 + "/DownTimeReport";

    // User endpoints
    public static final String URI_AUTH_STATUS            = V1 + "/auth/status";
    public static final String URI_CALENDER_EVENT_HISTORY = V1 + "/CalenderEventHistory";
}
