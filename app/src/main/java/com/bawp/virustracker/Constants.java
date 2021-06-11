package com.bawp.virustracker;

public class Constants {
    static final String API_URL = "https://api.covidtracking.com/v1/us/current.json";
    static final String TAG_OUTPUT = "OUTPUT";
    static final String JSON_PROCESSING_WORK_NAME = "JSON_PROCESSING";
    public static final String STRING_URL = "api_url";
    public static final String DATA_OUTPUT = "Data_Output";
    public static final long DELAY_TIME_MILLIS = 3000;

    public static final CharSequence VERBOSE_NOTIFICATION_CHANNEL_NAME =
            "Verbose WorkManager Notifications";
    public static String VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever work starts";
    public static final CharSequence NOTIFICATION_TITLE = "WorkRequest Starting";
    public static final String CHANNEL_ID = "VERBOSE_NOTIFICATION" ;
    public static final int NOTIFICATION_ID = 1;
    public static final String CLEANING = "Cleaning";

    private Constants() {}
}
