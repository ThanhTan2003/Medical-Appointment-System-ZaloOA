package com.programmingtechie.zalo_oa_service.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonUtils {

    static final Gson gson = new Gson();

    public static String toJsonString(Object obj) {
        return gson.toJson(obj);
    }

    public static JsonElement toJsonElement(Object obj) {
        return gson.toJsonTree(obj);
    }
}
