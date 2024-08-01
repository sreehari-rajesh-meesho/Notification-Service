package com.example.notificationservice.utils;

public class Constants {

    // Success Codes
    public static final long INGESTION_SUCCESSFUL = 0;
    public static final long UPDATE_SUCCESSFUL = 0;

    public static final long DATABASE_ERROR = -1;
    // Failure Codes
    public static final long PHONE_NUMBER_MANDATORY = -2;
    public static final long MESSAGE_WITH_ID_NOT_FOUND = -3;
    public static final long INVALID_REQUEST = -5;

    public static final long NUMBER_BLACKLISTED = -4;

    public static final long BLACKLISTED_SUCCESSFULLY = 0;
    public static final long WHITELISTED_SUCCESSFULLY = 1;
    public static final String API_URL = "https://api.imiconnect.in/resources/v1/messaging";
    public static final String API_KEY = "c0c49ebf-ca44-11e9-9e4e-025282c394f2";

}
