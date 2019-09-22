package com.dzo.prospectWizard.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dzo.prospectWizard.R;


public class EditTrackerFragment extends DialogFragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    Button btnDialogUpdate, btnDialogCancel;
    EditText etDialogname,etDialogstartingMile,etDialogEndingMile,etDialogDesc;
    String name,stMile,eMile,desc,Font;
    Typeface face;
    public EditTrackerFragment() {
        // Required empty public constructor
    }
    /*public static EditTrackerFragment newInstance(String tName, String tSMile, String tEMile, String tDesc) {
        EditTrackerFragment f = new EditTrackerFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("tName", tName);
        args.putString("tSMile", tSMile);
        args.putString("tEMile", tEMile);
        args.putString("tDesc", tDesc);

        f.setArguments(args);

        return f;
    }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name=getArguments().getString("tName");
            stMile=getArguments().getString("tSMile");
            eMile=getArguments().getString("tEMile");
            desc=getArguments().getString("tDesc");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Font = sharedPreferences.getString("item", null);
        face = null;
        if (Font != null) {
            face = Typeface.createFromAsset(getContext().getAssets(), Font);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_tracker, container, true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int Color1 = sharedPreferences.getInt("Color", -7403255);
        ConstraintLayout managelayout=view.findViewById(R.id.managelayout);
        if(managelayout != null) {
            managelayout.setBackgroundColor(Color1);
        }
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(false);


        btnDialogUpdate=view.findViewById(R.id.btnDialogUpdate);
        btnDialogCancel=view.findViewById(R.id.btnDialogCancel);
        etDialogname=view.findViewById(R.id.etDialogName);

        etDialogstartingMile=view.findViewById(R.id.etDialogStartingMile);
        etDialogEndingMile=view.findViewById(R.id.etDialogEndingMile);
        etDialogDesc=view.findViewById(R.id.etDialogDesc);
        btnDialogCancel.setOnClickListener(this);
        btnDialogUpdate.setOnClickListener(this);

        if( face != null){
            btnDialogUpdate.setTypeface(face);
            btnDialogCancel.setTypeface(face);
            etDialogname.setTypeface(face);
            etDialogstartingMile.setTypeface(face);
            etDialogEndingMile.setTypeface(face);
            etDialogDesc.setTypeface(face);
        }


        etDialogname.setText(name);
        etDialogstartingMile.setText(stMile);
        etDialogEndingMile.setText(eMile);
        etDialogDesc.setText(desc);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void updateTrackerItem() {
        name=etDialogname.getText().toString();
        stMile=etDialogstartingMile.getText().toString();
        eMile=etDialogEndingMile.getText().toString();
        desc=etDialogDesc.getText().toString();
        if (mListener != null) {
            mListener.onFragmentInteraction(name,stMile,eMile,desc);
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDialogUpdate:
                updateTrackerItem();
                break;
            case R.id.btnDialogCancel:
                getDialog().dismiss();
                break;
        }
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String name,String stMile,String eMile, String desc);
    }
}
