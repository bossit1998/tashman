package uz.tm.tashman.util;

public interface CONSTANT {
    boolean IS_PRODUCTION = false;

    String DEV_SERVER_URL = "www.someFucingUrlHere/";
    String PROD_SERVER_URL = "";

    String BASE_URL = IS_PRODUCTION ? PROD_SERVER_URL : DEV_SERVER_URL;

    String LOCAL_DATE_TIME_FORMAT = "dd.MM.YYYY HH:mm:ss";

    String USER_DEFAULT_IMAGE_URL = "default_user_image.png";
    String PRODUCT_DEFAULT_IMAGE_URL = "default_product_image.png";
    String CARD_DEFAULT_IMAGE_URL = "card_background_1.png";
    String CONTENT = "/content";
    String ROOT = "/root";
    String DATA_FOLDER = "/data";
    String PRODUCTS_FOLDER = "/products";
    String THUMBNAILS_FOLDER = "/thumbnails";

    // App Constants
    Integer DEFAULT_PAGE_NUMBER = 0;
    Integer DEFAULT_PAGE_SIZE = 10;
    Integer MAX_PAGE_SIZE = 20;
    String DEFAULT_OTP = "1234";

    Integer QR_EXPIRE_TIMEOUT_MILLIS = 5 * 60 * 1000; //5 minut
}