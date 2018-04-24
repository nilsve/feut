package com.feut.shared.models;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Model {
    static Gson gson = new Gson();

    /*
        De serialize & deserialize functies gebruiken intern JSON. Misschien niet heel efficient, maar we krijgen wel gratis alle dingen van GSON.
     */

    public static <M extends Model> Model Deserialize(Map<String, String> queryResult, Class<M> returnType) {
        // Query resultaten kunnen leeg zijn
        if (queryResult == null) {
            return null;
        }

        JSONObject object = new JSONObject();

        for (String key : queryResult.keySet()) {
            // Van snake case naar camel case, bvb `gebruiker_id` => `gebruikerId`
            String formattedKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
            object.put(formattedKey, queryResult.get(key));
        }

        return (Model)gson.fromJson(object.toString(), returnType);
    }

    public Map<String, String> Serialize() {
        JsonObject json = (JsonObject)gson.toJsonTree(this);
        Map<String, String> result = new HashMap<>();

        for (String key : json.keySet()) {
            // Van camel case naar snake case, bvb `gebruikerId` => `gebruiker_id`
            String formattedKey = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
            result.put(formattedKey, json.get(key).getAsString());
        }

        return result;
    }

}
