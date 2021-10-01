package uz.tm.tashman.util;


public interface CONSTANT {
    String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // card default image
    String CARD_DEFAULT_IMAGE_URL = "http://185.74.6.73:8080/card_background_1.png";

    // App Constants
    String DEFAULT_PAGE_NUMBER = "0";
    String DEFAULT_PAGE_SIZE = "10";
    int MAX_PAGE_SIZE = 20;

    boolean IS_PRODUCTION = false;


    Long DIGITAL_PASSWORD_LENGTH = (long) 9;
    Integer QR_EXPIRE_TIMEOUT_MILLIS = 5 * 60 * 1000; //5 minut

}