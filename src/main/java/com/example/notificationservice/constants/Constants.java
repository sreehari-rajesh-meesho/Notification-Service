package com.example.notificationservice.constants;

import org.apache.kafka.common.protocol.types.Field;

public class Constants {

    // Success Codes
    public static final int INGESTION_SUCCESSFUL = 0;
    public static final int UPDATE_SUCCESSFUL = 0;

    // Failure Codes
    public static final int PHONE_NUMBER_MANDATORY = -1;
    public static final int MESSAGE_WITH_ID_NOT_FOUND = -2;

    public static final String BLACK_LISTED_NUMBER = "BlackListedNumber";
    public static final Boolean EXISTS = true;
}
