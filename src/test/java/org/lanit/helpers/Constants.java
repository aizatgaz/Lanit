package org.lanit.helpers;

public final class Constants {

    public static final String BASE_URL = "https://petstore.swagger.io/v2";

    public static final String INVENTORY_URL = "/store/inventory";

    public static final String ORDER_URL = "/store/order";

    public static final String ORDER_URL_WITH_PARAM = "/store/order/{orderId}";

    public static final String ORDER_SCHEMA_PATH = "schemas/orderSchema.json";

    public static final String MESSAGE_SCHEMA_PATH = "schemas/messageSchema.json";

    public static final String STATUS_TYPE_ERROR = "error";

    public static final String STATUS_TYPE_UNKNOWN = "unknown";

    public static final String NO_DATA = "No data";

    public static final String ORDER_NOT_FOUND = "Order not found";

    public static final String BAD_INPUT = "bad input";

    public static final Long NON_EXISTING_ID = -1L;

    public static final String NUMBER_FORMAT_EXCEPTION = "java.lang.NumberFormatException";

    private Constants() {}
}
