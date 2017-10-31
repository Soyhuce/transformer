package fr.soyhuce.transformer;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by mathieuedet on 31/10/2017.
 */

public class SampleUsage {

    private Transformer<User> transformer = new UserTransformer();

    private void getClassicUser(){
        JSONObject userJSONObject = createUserJSONObject(1, "Jobs", "Steve", true);

        try {
            User user = transformer.getItemFromJSONObject(userJSONObject);
            Assert.assertNotNull(user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getListUsers(){
        JSONObject firstUserJSONObject = createUserJSONObject(1, "Jobs", "Steve", true);
        JSONObject secondUserJSONObject = createUserJSONObject(2, "Gates", "Bill", false);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(firstUserJSONObject);
        jsonArray.put(secondUserJSONObject);

        List<User> users = transformer.getItemsFromJSONArray(jsonArray);
        Assert.assertNotNull(users);
        Assert.assertEquals(users.size(), 2);
    }

    private void getUserFromCustomJSON(){
        JSONObject jsonObject = createCustomJSONObjectWithUserInside();
        try {
            transformer.getCustomObject(jsonObject, "user", UserTransformer.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private JSONObject createCustomJSONObjectWithUserInside(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("custom_data", "value");
            JSONObject userJSONObject = createUserJSONObject(3, "Musk", "Elon", false);
            jsonObject.put("user", userJSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createUserJSONObject(int id, String lastname, String firstname, boolean isPremium){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("last_name", lastname);
            jsonObject.put("first_name", firstname);
            jsonObject.put("is_premium", isPremium);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
