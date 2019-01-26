package com.example.minhy.seakingapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;

    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView date;
        protected TextView temp;
        protected TextView ph;
        private LinearLayout linearLayout;


        public CustomViewHolder(View view) {
            super(view);
            this.date = (TextView) view.findViewById(R.id.textView_list_date);
            this.temp = (TextView) view.findViewById(R.id.textView_list_temp);
            this.ph = (TextView) view.findViewById(R.id.textView_list_ph);
            linearLayout = (LinearLayout) view.findViewById(R.id.one_line);

        }

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.date.setText(mList.get(position).getMember_date());
        viewholder.temp.setText(mList.get(position).getMember_temp());
        viewholder.ph.setText(mList.get(position).getMember_ph());

        if(Float.parseFloat(mList.get(position).getMember_temp())>mList.get(position).getTemper_MAX()|Float.parseFloat(mList.get(position).getMember_temp())<mList.get(position).getTemper_MIN()){
            viewholder.date.setTextColor(0xAAFF0000);
            viewholder.temp.setTextColor(0xAAFF0000);

            Log.d("이상!" ,mList.get(position).getMember_temp()+ " > " + String.valueOf(mList.get(position).getTemper_MAX()));
            Log.d("이상!" ,mList.get(position).getMember_temp()+ " < " + String.valueOf(mList.get(position).getTemper_MIN()));

            if (Float.parseFloat(mList.get(position).getMember_ph())>mList.get(position).getPhMAX()|Float.parseFloat(mList.get(position).getMember_ph())<mList.get(position).getPhMIN()){
                viewholder.ph.setTextColor(0xAAFF0000);
            }else{
                viewholder.ph.setTextColor(0xAA000000);
            }

        }else{
            viewholder.date.setTextColor(0xAA000000);
            viewholder.temp.setTextColor(0xAA000000);
            if(Float.parseFloat(mList.get(position).getMember_ph())>mList.get(position).getPhMAX()|Float.parseFloat(mList.get(position).getMember_ph())<mList.get(position).getPhMIN()){
                viewholder.date.setTextColor(0xAAFF0000);
                viewholder.ph.setTextColor(0xAAFF0000);
            }else {
                viewholder.ph.setTextColor(0xAA000000);
            }
        }


    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }






}
