package com.dzo.prospectWizard.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dzo.prospectWizard.R;


public class CustomHome extends ArrayAdapter<String> {

    private String[] contact_id;
    private String[] firstname;
    private String[] lastname;
    private String[] text;
    private String[] email;
    private String[] phone;
    private String[] notes;
    private Activity context;
    private String[] action;
    Typeface FontName;

    public CustomHome(Activity context, String[] contact_id, String[] firstname, String[] lastname, String[] action, String[] text, String[] email, String[] phone, String[] notes, Typeface faceName) {
        super(context, R.layout.home_list_adapter, contact_id);
        this.context = context;
        this.contact_id = contact_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.action = action;
        this.text = text;
        this.email = email;
        this.phone = phone;
        this.notes = notes;
        this.FontName = faceName;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.home_list_adapter, null, true);


        TextView contact_ids = (TextView) listViewItem.findViewById(R.id.contact_id);
        TextView Name = (TextView) listViewItem.findViewById(R.id.home_name);
        TextView Phone = (TextView) listViewItem.findViewById(R.id.nam);
        TextView Email = (TextView) listViewItem.findViewById(R.id.nam1);
        TextView Text = (TextView) listViewItem.findViewById(R.id.nam2);
        TextView sms = (TextView) listViewItem.findViewById(R.id.sms);
        TextView Action = (TextView) listViewItem.findViewById(R.id.action);
        TextView Actio = (TextView) listViewItem.findViewById(R.id.actions);
        TextView firstName = (TextView) listViewItem.findViewById(R.id.firstName);
        TextView LastName = (TextView) listViewItem.findViewById(R.id.LastName);

        contact_ids.setText(contact_id[position]);
        Phone.setText(phone[position]);
        Email.setText(email[position]);
        Text.setText(text[position]);
        Name.setText(firstname[position] + " " + lastname[position]);
        sms.setText(notes[position]);
        firstName.setText(firstname[position]);
        Actio.setText(action[position]);

        if (action[position] != null) {
            Action.setVisibility(View.VISIBLE);
            Actio.setVisibility(View.VISIBLE);
        }
        LastName.setText(lastname[position]);

        String ab = Actio.getText().toString();
        if (ab.length() != 0) {
            Action.setVisibility(View.GONE);
            Actio.setVisibility(View.VISIBLE);
        }


        String str1 = Email.getText().toString();
        String str2 = Text.getText().toString();

        if (str1.length() > 1) {
            Action.setText("TEXT");
        } else if (str2.length() > 1) {
            Action.setText("CALL");
        } else {
            Action.setText("Email");

        }

        if (FontName != null) {
            sms.setTypeface(FontName);
            Phone.setTypeface(FontName);
            Email.setTypeface(FontName);
            Text.setTypeface(FontName);
            Action.setTypeface(FontName);
            Actio.setTypeface(FontName);
            firstName.setTypeface(FontName);
            LastName.setTypeface(FontName);
            contact_ids.setTypeface(FontName);
            Name.setTypeface(FontName);
        }


        return listViewItem;
    }
}