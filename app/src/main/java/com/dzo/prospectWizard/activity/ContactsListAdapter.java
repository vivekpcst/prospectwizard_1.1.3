package com.dzo.prospectWizard.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dzo.prospectWizard.R;

import java.util.ArrayList;


public class ContactsListAdapter extends BaseAdapter {

    Context context;
    ContactsList contactsList,filteredContactsList,selectedContactsList;
    String filterContactName;
    String Font;
    Typeface face;
    SharedPreferences.Editor editor;
    private Intent blackListIntent;
    ContactsListAdapter(Context context, ContactsList contactsList,String fontName,Typeface faceName){

        super();
        this.context = context;
        this.contactsList = contactsList;
        this.filteredContactsList=new ContactsList();
        this.selectedContactsList = new ContactsList();
        this.filterContactName = "";
        this.face = faceName;
        this.Font = fontName;
    }

    public void filter(String filterContactName){



        filteredContactsList.contactArrayList.clear();

        if(filterContactName.isEmpty() || filterContactName.length()<1){
            filteredContactsList.contactArrayList.addAll(contactsList.contactArrayList);
            this.filterContactName = "";

        }
        else {
            this.filterContactName = filterContactName.toLowerCase().trim();
            for (int i = 0; i < contactsList.contactArrayList.size(); i++) {

                if (contactsList.contactArrayList.get(i).name.toLowerCase().contains(filterContactName))
                    filteredContactsList.addContact(contactsList.contactArrayList.get(i));
            }
        }
        notifyDataSetChanged();

    }

    public void addContacts(ArrayList<Contact> contacts){
        this.contactsList.contactArrayList.addAll(contacts);
        this.filter(this.filterContactName);

    }

    @Override
    public int getCount() {
        return filteredContactsList.getCount();
    }

    @Override
    public Contact getItem(int position) {
        return filteredContactsList.contactArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(this.getItem(position).id);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);

        final ViewHolder viewHolder;

        if(convertView==null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            convertView = inflater.inflate(R.layout.contact_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.chkContact = (CheckBox) convertView.findViewById(R.id.chk_contact);
            viewHolder.chkContact.setButtonDrawable(R.drawable.checkbox_up);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        editor = sharedPreferences.edit();

        viewHolder.chkContact.setText(this.filteredContactsList.contactArrayList.get(position).toString());
        if (face != null){
            viewHolder.chkContact.setTypeface(face);
        }
        viewHolder.chkContact.setTextColor(Color.WHITE);
        viewHolder.chkContact.setId(Integer.parseInt(this.filteredContactsList.contactArrayList.get(position).id));
        viewHolder.chkContact.setChecked(alreadySelected(filteredContactsList.contactArrayList.get(position)));
        viewHolder.chkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Contact contact = filteredContactsList.getContact(buttonView.getId());
                if(contact!=null && isChecked && !alreadySelected(contact)){
                    viewHolder.chkContact.setButtonDrawable(R.drawable.checkbox_selected);
                    selectedContactsList.addContact(contact);
                    blackListIntent = new Intent("com.jason.floating.notification.appPickerFragment.ListViewAdapter");
                    blackListIntent.putExtra("packageName", filteredContactsList.contactArrayList.get(position).name);
                }
                else if(contact!=null && !isChecked){
                    viewHolder.chkContact.setButtonDrawable(R.drawable.checkbox_up);
                   selectedContactsList.removeContact(contact);
                }

                context.sendBroadcast(blackListIntent);
                editor.putBoolean("CheckValue" + position, isChecked);
                editor.commit();
            }

        });

        return convertView;
    }


    public boolean alreadySelected(Contact contact)
    {
        if(this.selectedContactsList.getContact(Integer.parseInt(contact.id))!=null)
            return true;

        return false;
    }
    public static class ViewHolder{

        CheckBox chkContact;
    }
}
