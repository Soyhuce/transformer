package fr.soyhuce.transformer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mathieuedet on 31/10/2017.
 */

public class UserTransformer extends Transformer<User> {

    @Override
    public User getItemFromJSONObject(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.setId(jsonObject.getInt("id"));
        user.setLastname(jsonObject.getString("last_name"));

        //!\\ firstname => optional --> getOptionalString()
        user.setFirstname(getOptionalString(jsonObject, "first_name"));

        user.setPremium(jsonObject.getBoolean("is_premium"));
        return user;
    }
}
