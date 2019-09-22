package com.dzo.prospectWizard.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dzo.prospectWizard.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Vivek kumar on 8/9/2019 at 5:37 PM.
 * Mobile 7982591863
 * E-mail vivekpcst.kumar@gmail.com
 */
public class TrackerListAdapter extends RecyclerView.Adapter<TrackerListAdapter.TrackerHolder> {
    Context context;
    ArrayList<String> eid, names, description, start_mileage, end_mileage, expense_amount;
    OnActionTrakingListener actionTrakingListener;
    Typeface face;
    String Font;
    public TrackerListAdapter(Context context, ArrayList<String> eid, ArrayList<String> names, ArrayList<String> description, ArrayList<String> start_mileage, ArrayList<String> end_mileage, ArrayList<String> expense_amount) {
        this.context=context;
        this.eid=eid;
        this.names=names;
        this.description=description;
        this.start_mileage=start_mileage;
        this.end_mileage=end_mileage;
        this.expense_amount=expense_amount;
        actionTrakingListener= (OnActionTrakingListener) context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Font = sharedPreferences.getString("item", null);
        face = null;
        if (Font != null) {
            face = Typeface.createFromAsset(context.getAssets(), Font);
        }

    }

    @NonNull
    @Override
    public TrackerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new TrackerHolder(LayoutInflater.from(context).inflate(R.layout.traker_list_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TrackerHolder trackerHolder, int i) {
        trackerHolder.tvTitle.setText(names.get(i));
        trackerHolder.tvDescription.setText(description.get(i));
        if(face != null){
            trackerHolder.tvTitle.setTypeface(face);
            trackerHolder.tvDescription.setTypeface(face);
        }
        trackerHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            actionTrakingListener.onTrackerEditItem(eid.get(trackerHolder.getAdapterPosition()));
            }
        });
        trackerHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            actionTrakingListener.onTrackerItemDelete(eid.get(trackerHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class TrackerHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDescription;
        ImageButton btnEdit,btnDelete;
        public TrackerHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnEdit=itemView.findViewById(R.id.btnEdit);

        }
    }

    interface OnActionTrakingListener{
        void onTrackerItemDelete(String eid);
        void onTrackerEditItem(String eid);
    }
}
