package com.dzo.prospectWizard.activity;

/**
 * Created by Vinay Kumar Singh on 2/18/2018.
 */

public class ContactList {
    private String contactId;
    private String firstName;
    private String lastName;
    private String action;
    private String phone;
    private String text;
    private String email;
    private String notes;
    private String pref_mode;

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setPhone(String phone) { this.phone = phone; }

    public void setText(String text) {
        this.text = text;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setpref_mode(String pref_mode) {
        this.pref_mode = pref_mode;
    }

    public String getContactId() {
        return contactId;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getText() {
        return text;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNotes() {
        return notes;
    }

    public String getAction() {
        return action;
    }

    public String getpref_mode() {
        return pref_mode;
    }

}
