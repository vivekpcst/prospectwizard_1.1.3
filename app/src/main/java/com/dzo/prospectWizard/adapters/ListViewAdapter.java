package com.dzo.prospectWizard.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dzo.prospectWizard.CallAndSms;
import com.dzo.prospectWizard.R;
import com.dzo.prospectWizard.activity.Addperfor;
import com.dzo.prospectWizard.activity.ContactActivity;
import com.dzo.prospectWizard.activity.ContactList;
import com.dzo.prospectWizard.activity.HomeActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewAdapter extends ArrayAdapter<ContactList> {
    Context context;
    LayoutInflater inflater;
    List<ContactList> worldpopulationlist;
    Typeface face;
    private SparseBooleanArray mSelectedItemsIds;
    String userName;
    String te,ph,em,Actions,b,Action,actionprogresh;
    public ListViewAdapter(Context context, int resourceId,
                           List<ContactList> worldpopulationlist, Typeface face) {
        super(context, resourceId, worldpopulationlist);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.worldpopulationlist = worldpopulationlist;
        this.face=face;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {

        TextView  contact_ids ;
        TextView  Name ;
        TextView  Phone ;
        TextView  Email ;
        TextView  Text;
        TextView  sms ;
        TextView  Action;
        TextView  Actio ;
        TextView  firstName;
        TextView  LastName ;

    }
    @Override
    public int getCount() {
        int a = worldpopulationlist.size();
        return worldpopulationlist.size();
    }
    @SuppressLint("ResourceAsColor")
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.home_list_adapter, null);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            userName = preferences.getString("UserName", "");
            System.out.println("UserName : "+userName);

            holder.contact_ids = (TextView) view.findViewById(R.id.contact_id);
            holder.Name = (TextView) view.findViewById(R.id.home_name);
            holder.Phone = (TextView) view.findViewById(R.id.nam);
            holder. Email = (TextView) view.findViewById(R.id.nam1);
            holder.Text = (TextView) view.findViewById(R.id.nam2);
            holder.sms = (TextView) view.findViewById(R.id.sms);
            holder.Action = (TextView) view.findViewById(R.id.action);
            holder.Actio = (TextView) view.findViewById(R.id.actions);
            holder.firstName = (TextView) view.findViewById(R.id.firstName);
            holder.LastName = (TextView) view.findViewById(R.id.LastName);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.contact_ids.setText(worldpopulationlist.get(position).getContactId());
        holder.Phone.setText(worldpopulationlist.get(position).getPhone());
        holder.Email.setText(worldpopulationlist.get(position).getEmail());
        holder.Text.setText(worldpopulationlist.get(position).getText());
        holder. Name.setText(worldpopulationlist.get(position).getFirstName()+" "+worldpopulationlist.get(position).getLastName());
        holder.sms.setText(worldpopulationlist.get(position).getNotes());
        holder.firstName.setText(worldpopulationlist.get(position).getFirstName());
        holder.Actio.setText(worldpopulationlist.get(position).getAction());
        holder.Action.setText(worldpopulationlist.get(position).getAction());
        final String nameMode = (worldpopulationlist.get(position).getpref_mode());
        if(worldpopulationlist.get(position).getAction().isEmpty()){
            holder.Action.setText(nameMode);
        }else {
            holder.Action.setText(nameMode);
            holder.Action.setBackgroundColor(R.color.background_btn);
        }



        ph = holder.Phone.getText().toString();
        em = holder.Email.getText().toString();
        te = holder.Text.getText().toString();
        b =holder.contact_ids.getText().toString();
        Actions = holder.Action.getText().toString();
        actionprogresh =holder. Actio.getText().toString();
        if(worldpopulationlist.get(position).getAction()!=null){
        }
        holder.LastName.setText(worldpopulationlist.get(position).getLastName());

        if (face != null){
            holder.sms.setTypeface(face);
            holder.Phone.setTypeface(face);
            holder.Email.setTypeface(face);
            holder.Text.setTypeface(face);
            holder.Action.setTypeface(face);
            holder.Actio.setTypeface(face);
            holder.firstName.setTypeface(face);
            holder.LastName.setTypeface(face);
            holder.contact_ids.setTypeface(face);
            holder.Name.setTypeface(face);
        }

        String ab=holder.Actio.getText().toString();

        holder.Action.setVisibility(View.GONE);
        holder.Actio.setVisibility(View.GONE);
        holder.Phone.setTag(position);

        if (ab.equals("ADD")){
            holder.Actio.setVisibility(View.VISIBLE);
            //holder.Actio.setText(nameMode);

        }else {
            holder.Action.setVisibility(View.VISIBLE);
            holder.Action.setText(nameMode);
            }

        if (nameMode.equals("TEXT") && ab.equals("ADD")){
            holder.Action.setVisibility(View.GONE);
            holder.Action.setText(nameMode);
            holder.Actio.setVisibility(View.VISIBLE);
           // holder.Actio.setText(nameMode);
        }



        holder.Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.Action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int tagPoi = (int) holder.Phone.getTag();


                if(holder.Action.getText().toString().equalsIgnoreCase("CALL"))
                {

                    ph = worldpopulationlist.get(tagPoi).getPhone();
                    Actions = worldpopulationlist.get(tagPoi).getpref_mode();

                    em = worldpopulationlist.get(tagPoi).getEmail();
                    te = worldpopulationlist.get(tagPoi).getText();
                    b = worldpopulationlist.get(tagPoi).getContactId();

                  //  worldpopulationlist.get(position).getpref_mode();
                    Phone();
                    clearPhone(ph,Actions);
                    call(holder.Phone.toString());
                    Log.e("gdccgcd",ph+Action);
                    Log.e("gdccgcd",holder.Phone.toString());


                }
                else if(holder.Action.getText().toString().equalsIgnoreCase("TEXT"))
                {
                    try {
                        ph = worldpopulationlist.get(tagPoi).getPhone();
                        Actions = worldpopulationlist.get(tagPoi).getpref_mode();
                        em = worldpopulationlist.get(tagPoi).getEmail();
                        te = worldpopulationlist.get(tagPoi).getText();
                        b = worldpopulationlist.get(tagPoi).getContactId();

                        Text();
                        clearPhone(ph,Actions);

                       /* SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(holder.Phone.getText().toString(), null, holder.sms.getText().toString(), null, null);
                       System.out.print(holder.Phone.getText().toString());
                       */

                    /*   SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
                       SharedPreferences.Editor editor=sharedPreferences.edit();
                       editor.putString("call_sms","sms");
                       editor.apply();
                       context.startActivity(new Intent(context, CallAndSms.class));*/


                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(Uri.parse("sms:"+ph));
                        sendIntent.putExtra("sms_body", "Type Here");
                        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(sendIntent);


//                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", ph, null)));
                       /* Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setDataAndType(Uri.parse("smsto:"+ph),"vnd.android-dir/mms-sms");
                        intent.putExtra("sms_body", "Type here");
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        }*/

                     /*   Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(Uri.parse("sms:"));
                        sendIntent.putExtra("sms_body","Type here...");
                        context.startActivity(sendIntent);
*/
                       /* Toast.makeText(context, "SMS Sent!",
                                Toast.LENGTH_LONG).show();
*/


                    }
                    catch (Exception e) {
                        Toast.makeText(context,
                                "No Network Available, Please try again later!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else if(holder.Email.getText().length()!=0)
                {

                    sendEmail(holder.Email.getText().toString());
                }

            }
        });


        holder.Actio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                int tagPoi = (int) holder.Phone.getTag();
                Intent i = new Intent(getContext(), Addperfor.class);
                i.putExtra("contact_id",worldpopulationlist.get(tagPoi).getContactId());
                getContext().startActivity(i);

                /*if(holder.Actio.getText().toString().equalsIgnoreCase("CALL"))
                {
                    Phone();
                    call(holder.Phone.toString());
                }
                else if(holder.Actio.getText().toString().equalsIgnoreCase("TEXT"))
                {
                    try {
                        ph = worldpopulationlist.get(tagPoi).getPhone();
                        Actions = worldpopulationlist.get(tagPoi).getpref_mode();

                        em = worldpopulationlist.get(tagPoi).getEmail();
                        te = worldpopulationlist.get(tagPoi).getText();
                        b = worldpopulationlist.get(tagPoi).getContactId();


                        Text();
                        clearPhone(ph,Actions);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(holder.Phone.getText().toString(), null, holder.sms.getText().toString(), null, null);
                        System.out.print(holder.Phone.getText().toString());
                        Toast.makeText(context, "SMS Sent!",
                                Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        Toast.makeText(context,
                                "No Network Available, Please try again later!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else if(holder.Email.getText().length()!=0)
                {
                    sendEmail(holder.Email.getText().toString());
                }*/
            }
        });


        return view;
    }

    @Override
    public void remove(ContactList object) {
        worldpopulationlist.remove(object);
        notifyDataSetChanged();
    }

    public List<ContactList> getWorldPopulation() {
        return worldpopulationlist;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }


    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


    private void ShowDialog1(final String phone, final String Action) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        alertDialog.setTitle("Confirm Delete...");
        alertDialog.setMessage("Do you want to remove this conversation?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which)
            {
                clearPhone(phone, Action);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }


        });
        alertDialog.show();

    }

    private void clearPhone(String ph,String Action) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String companynURL=preferences.getString("companynURL","");

        String url = companynURL+"/prospect_app_dz/update_records_dz.php?rep_id=" +userName+"&phone=" +ph + "&pref_mode=" + Action;
        Log.e("vinaysss", "" + url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            if (!response.contains("notok")) {
                                Intent i=new Intent(context, HomeActivity.class);
                                context.startActivity(i);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void call(String te) {
        String p = "tel:"+ph;



        /*SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("call_sms","call");
        editor.apply();
        context.startActivity(new Intent(context, CallAndSms.class));
*/



//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ph, null));

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(p));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
       /* Intent i = new Intent(Intent.ACTION_CALL);

        i.setData(Uri.parse(p));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        context.startActivity(i);
*/

    }

    private void sendEmail(String ph) {
        String[] TO = {ph};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Email();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void Text() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String companynURL=preferences.getString("companynURL","");
         String UPDATE_URL = companynURL+"/prospect_app_dz/update_records_dz.php?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("notok")) {

                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("rep_id", userName);
                map.put("phone", ph);
                map.put("pref_mode",Actions);
                Log.e("hgshgdsa",""+ph+Actions+userName);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    private void Phone() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String companynURL=preferences.getString("companynURL","");
        String UPDATE_URL = companynURL+"/prospect_app_dz/update_records_dz.php?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("notok")) {

                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("rep_id", userName);
                map.put("phone", ph+te);
                map.put("pref_mode",Actions);
                Log.e("hgshgdsa",""+b+Actions+userName);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    private void Email() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String companynURL=preferences.getString("companynURL","");
        String UPDATE_URL = companynURL+"/prospect_app_dz/update_records_dz.php?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("notok")) {

                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("rep_id", userName);
                map.put("email", ph);
                map.put("pref_mode",Actions);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void ShowDialog(final String phone, final String Action) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        alertDialog.setTitle("Confirm Delete...");
        alertDialog.setMessage("Do you want to remove this conversation?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which)
            {
                clearallsms(phone,Action);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    private void clearallsms(String em,String Actions) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String companynURL=preferences.getString("companynURL","");
        String url = companynURL+"/prospect_app_dz/update_records_dz.php?rep_id=" +userName+"&phone=" +em + "&pref_mode=" + Actions;
        Log.e("vinaysss", "" + url);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            if (!response.contains("notok")) {
                                Intent i=new Intent(context, HomeActivity.class);
                                context.startActivity(i);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}