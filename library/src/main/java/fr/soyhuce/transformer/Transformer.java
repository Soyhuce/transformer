package fr.soyhuce.transformer;

/**
 * Created by SoyHuCe on 31/10/2017.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class Transformer<T> {

    private static final String TAG = Transformer.class.getName();

    /***
     * Permet de récupérer une liste d'item de type 'T' à partir d'un JSONArray
     * @param jsonArray JSONArray contenant la liste d'objets à récupérer
     * @return Liste d'item de type 'T'
     */
    public List<T> getItemsFromJSONArray(JSONArray jsonArray){
        List<T> items = new ArrayList<>();
        for(int i = 0 ; i < jsonArray.length() ; i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                T item = getItemFromJSONObject(jsonObject);
                items.add(item);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return items;
    }

    /***
     * Permet de récupérer un objet de type 'T' à partir d'un objet JSON
     * @param jsonObject JSONObject contenant l'objet à récupérer
     * @return Un objet de type 'T' construit à partir du JSON
     * @throws JSONException JSONException lancée lorsque l'objet JSONObject ne correspont pas et qu'une erreur se produit au moment de le parser
     */
    @SuppressWarnings("unchecked")
    public abstract T getItemFromJSONObject(JSONObject jsonObject) throws JSONException;

    /***
     * Permet d'obtenir une valeur de type String dans un JSON à partir d'une clé
     * @param jsonObject JSONObject contenant la valeur à récupérer
     * @param key Clé correspondant à la valeur à récupérer
     * @return La valeur correspondant à la clé ou null si celle-ci n'existe pas dans le JSON
     * @throws JSONException JSONException lancée lorsque l'objet JSONObject ne correspont pas et qu'une erreur se produit au moment de le parser
     */
    protected String getOptionalString(JSONObject jsonObject, String key) throws JSONException {
        return jsonObject.has(key) && !jsonObject.isNull(key) && !jsonObject.getString(key).equals("") ? jsonObject.getString(key) : null;
    }

    /***
     * Permet d'obtenir une valeur de type int dans un JSON à partir d'une clé
     * @param jsonObject JSONObject contenant la valeur à récupérer
     * @param key Clé correspondant à la valeur à récupérer
     * @return La valeur correspondant à la clé ou -1 si celle-ci n'existe pas dans le JSON
     * @throws JSONException JSONException lancée lorsque l'objet JSONObject ne correspont pas et qu'une erreur se produit au moment de le parser
     */
    protected int getOptionalInt(JSONObject jsonObject, String key) throws JSONException {
        return getOptionalInt(jsonObject, key, -1);
    }

    /***
     * Permet d'obtenir une valeur de type String dans un JSON à partir d'une clé
     * @param jsonObject JSONObject contenant la valeur à récupérer
     * @param key Clé correspondant à la valeur à récupérer
     * @param defaultValue Valeur par défaut à retourner si la valeur n'est pas présente dans le JSON ou si celle-ci est null
     * @return La valeur correspondant à la clé ou la valeur par défaut si celle-ci n'existe pas dans le JSON
     * @throws JSONException JSONException lancée lorsque l'objet JSONObject ne correspont pas et qu'une erreur se produit au moment de le parser
     */
    protected int getOptionalInt(JSONObject jsonObject, String key, int defaultValue) throws JSONException {
        return jsonObject.has(key) && !jsonObject.isNull(key) ? jsonObject.getInt(key) : defaultValue;
    }

    /***
     * Permet d'obtenir une valeur de type Boolean dans un JSON à partir d'une clé
     * @param jsonObject JSONObject contenant la valeur à récupérer
     * @param key Clé correspondant à la valeur à récupérer
     * @return La valeur correspondant à la clé ou false si celle-ci n'existe pas dans le JSON
     * @throws JSONException JSONException lancée lorsque l'objet JSONObject ne correspont pas et qu'une erreur se produit au moment de le parser
     */
    protected boolean getOptionalBoolean(JSONObject jsonObject, String key) throws JSONException{
        return getOptionalBoolean(jsonObject, key, false);
    }

    /***
     * Permet d'obtenir une valeur de type boolean dans un JSON à partir d'une clé
     * @param jsonObject JSONObject contenant la valeur à récupérer
     * @param key Clé correspondant à la valeur à récupérer
     * @param defaultValue Valeur par défaut à retourner si la valeur n'est pas présente dans le JSON ou si celle-ci est null
     * @return La valeur correspondant à la clé ou la valeur par défaut si celle-ci n'existe pas dans le JSON
     * @throws JSONException JSONException lancée lorsque l'objet JSONObject ne correspont pas et qu'une erreur se produit au moment de le parser
     */
    protected boolean getOptionalBoolean(JSONObject jsonObject, String key, boolean defaultValue) throws JSONException{
        return jsonObject.has(key) && !jsonObject.isNull(key) ? jsonObject.getBoolean(key) : defaultValue;
    }

    /***
     * Permet d'obtenir une valeur de type custom dans un JSON à partir d'une clé
     * @param jsonObject JSONObject contenant la valeur à récupérer
     * @param key Clé correspondant à la valeur à récupérer
     * @param transformerClass Class du transformer correspondant au type d'objet à parser (ex: EventsTransformer.class), cette classe doit obligatoirement hériter de Transformer<T>
     * @return La valeur correspondant à la clé ou la valeur par défaut si celle-ci n'existe pas dans le JSON
     * @throws JSONException JSONException lancée lorsque l'objet JSONObject ne correspont pas et qu'une erreur se produit au moment de le parser
     */
    protected Object getCustomObject(JSONObject jsonObject, String key, Class<? extends Transformer> transformerClass) throws JSONException{
        try {
            if(jsonObject.has(key) && !jsonObject.isNull(key)){
                return transformerClass.newInstance().getItemFromJSONObject(jsonObject.getJSONObject(key));
            }
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IllegalAccessException e){
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    /***
     * Permet d'obtenir un tableau de valeurs de types custom dans un JSON à partir d'une clé
     * @param jsonObject JSONObject contenant la valeur à récupérer
     * @param key Clé correspondant à la valeur à récupérer
     * @param transformerClass Class du transformer correspondant au type d'objet à parser (ex: EventsTransformer.class), cette classe doit obligatoirement hériter de Transformer<T>
     * @return La valeur correspondant à la clé ou la valeur par défaut si celle-ci n'existe pas dans le JSON
     * @throws JSONException JSONException lancée lorsque l'objet JSONObject ne correspont pas et qu'une erreur se produit au moment de le parser
     */
    protected List getCustomArrayObject(JSONObject jsonObject, String key, Class<? extends Transformer> transformerClass) throws JSONException {
        try {
            if(jsonObject.has(key) && !jsonObject.isNull(key)){

                return transformerClass.newInstance().getItemsFromJSONArray(jsonObject.getJSONArray(key));

            }
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IllegalAccessException e){
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    /***
     * Permet d'obtenir une valeur de type URL dans un JSON à partir d'une clé
     * @param jsonObject JSONObject contenant la valeur à récupérer
     * @param key Clé correspondant à la valeur à récupérer
     * @return La valeur correspondant à la clé ou null si celle-ci n'existe pas dans le JSON ou qu'il ne s'agisse pas d'une URL valide
     * @throws JSONException JSONException lancée lorsque l'objet JSONObject ne correspont pas et qu'une erreur se produit au moment de le parser
     */
    protected @Nullable URL getOptionalURL(@NonNull JSONObject jsonObject, @NonNull String key) throws JSONException {
        if(jsonObject.has(key) && !jsonObject.isNull(key)){
            try{
                return URI.create(jsonObject.getString(key)).toURL();
            }catch(MalformedURLException e){
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }
        return null;
    }
}