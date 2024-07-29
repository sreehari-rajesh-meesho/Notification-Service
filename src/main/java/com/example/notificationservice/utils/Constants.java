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
}
