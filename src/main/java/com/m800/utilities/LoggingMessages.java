package com.m800.utilities;

/**
 * Created by Waqar Malik on 7/4/2017.
 */
public class LoggingMessages {
    public static String MESSAGE_NOT_RECOGNIZED = "FileScanner can not recognize the received message";
    public static String SCAN_MESSAGE_RECEIVED = "FileScanner has received message: '{}'";
    public static String FILE_SCANNER_FILE_DETECTED = "File scanner has detected file: {}.";
    public static String PARSE_MESSAGE_RECEIVED = "FileParser has received message: '{}'";
    public static String PARSE_MESSAGE_ERROR =  "Message {} - Parsing file name:  {} has caused error: {}";
    public static String PARSE_MESSAGE_COMPLETION_ERROR = "Error on parse completion {}";
    public static String START_OF_FILE_RECEIVED = "Aggregator has received event: '{}'";
    public static String LINE_RECEIVED = "Aggregator has received event: {}";
    public static String EVENT_NOT_RECEIVED = "Aggregator can not recognize this event {}";
    public static String END_OF_FILE_RECEIVED ="Aggregator received end-of-file event: {}";
    public static String EVENT_RECEIVED ="Aggregator received event: '{}'";

    public static String READ_FAILED = "Read failed {}";

}
