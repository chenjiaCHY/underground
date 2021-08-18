package com.ntschy.underground.utils;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtils {

    private JsonUtils() {}

    private static final Gson DEFAULT_BUILDER = new Gson();

    /**
     * 将对象转换成一个json格式的字符串
     * @param object
     * @return
     */
    public static String toJsonStr(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    /**
     * 判断字符串是否是合法的json格式的字符串
     * @param jsonStr
     * @return
     */
    public static boolean isLegalJsonStr(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            log.error("{}不是一个合法的json格式的字符串", jsonStr);
            return false;
        }

        if (jsonElement == null) {
            return false;
        }

        return jsonElement.isJsonObject();
    }

    /**
     * 将字符串转换成指定的java对象
     * @param jsonStr
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T formatter(String jsonStr, Class<T> type) {
        if (!isLegalJsonStr(jsonStr)) {
            return null;
        }
        return DEFAULT_BUILDER.fromJson(jsonStr, type);
    }

    /**
     * 将一个对象转换成另一个对象
     * @param obj
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T formatter(Object obj, Class<T> type) {
        return formatter(toJsonStr(obj), type);
    }

    /**
     * 将字符串转换成Json对象
     * @param str
     * @return
     */
    public static JsonObject parseObject(String str) {
        if (!isLegalJsonStr(str)) {
            return null;
        }

        return new JsonParser().parse(str).getAsJsonObject();
    }
}
