package com.dzo.prospectWizard.activity;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ParseHOME {
    public static String[] contact_id;
    public static String[] firstname;
    public static String[] lastname;
    public static String[] phone;
    public static String[] email;
    public static String[] text;
    public static String[] notes;
    public static String[] action;
    public static String[] a;
    public static String[] b;
    public static String[] c;

    public static final String JSON_ARRAY = "statement";
    public static final String  NAME= "firstname";
    public static final String  NAMEL= "lastname";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String EMAI = "text";
    public static final String SMS = "notes";

    private JSONArray users = null;

    private String json;

    public ParseHOME(String json){
        this.json = json;
    }


    protected void ParseHOME(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            contact_id = new String[users.length()];
            firstname = new String[users.length()];
            lastname = new String[users.length()];
            phone = new String[users.length()];
            email = new String[users.length()];
            text = new String[users.length()];
            notes = new String[users.length()];
            action = new String[users.length()];

            for(int i=0;i<users.length();i++) {
                JSONObject jo = users.getJSONObject(i);
                contact_id[i] = jo.getString("contact_id");
                firstname[i] = jo.getString("firstname");
                lastname[i] = jo.getString("lastname");
                notes[i] = jo.getString("notes");
                action[i] = jo.getString("action");

                if(jo.has("text"))
                {
                    text[i] = jo.getString("text");
                }
                if(jo.has("email"))
                {
                    email[i]=jo.getString("email");
                }
                if(jo.has("phone"))
                {
                    phone[i]=jo.getString("phone");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}