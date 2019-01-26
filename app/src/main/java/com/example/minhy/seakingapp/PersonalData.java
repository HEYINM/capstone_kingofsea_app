package com.example.minhy.seakingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonalData implements Parcelable {
    public String member_date;
    public String member_temp;
    public String member_ph;
    public String sp_date;
    public int member_month;
    public int member_day;
    public int member_hour;
    public float temper_MIN;
    public float temper_MAX;
    public float phMIN;
    public float phMAX;


    public float getTemper_MIN() {
        return temper_MIN;
    }

    public void setTemper_MIN(float temper_MIN) {
        this.temper_MIN = temper_MIN;
    }

    public float getTemper_MAX() {
        return temper_MAX;
    }

    public void setTemper_MAX(float temper_MAX) {
        this.temper_MAX = temper_MAX;
    }

    public float getPhMIN() {
        return phMIN;
    }

    public void setPhMIN(float phMIN) {
        this.phMIN = phMIN;
    }

    public float getPhMAX() {
        return phMAX;
    }

    public void setPhMAX(float phMAX) {
        this.phMAX = phMAX;
    }

    public String getSp_date() {
        return sp_date;
    }

    public void setSp_date(String sp_date) {
        this.sp_date = sp_date;
    }

    public int getMember_hour() {
        return member_hour;
    }

    public void setMember_hour(int member_hour) {
        this.member_hour = member_hour;
    }

    public int getMember_day() {
        return member_day;
    }

    public void setMember_day(int member_day) {
        this.member_day = member_day;
    }

    public String getMember_ph() {
        return member_ph;
    }

    public void setMember_ph(String member_ph) {
        this.member_ph = member_ph;
    }


    public int getMember_month() {
        return member_month;
    }

    public void setMember_month(int member_month) {
        this.member_month = member_month;
    }
    public String getMember_date() {
        return member_date;
    }

    public String getMember_temp() {
        return member_temp;
    }

    public void setMember_date(String member_date) {
        this.member_date = member_date;
    }

    public void setMember_temp(String member_temp) {
        this.member_temp = member_temp;
    }

    public PersonalData(){}

    public PersonalData(Parcel in){
        this.member_date = in.readString();
        this.member_temp = in.readString();
        this.member_ph=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.member_date);
        dest.writeString(this.member_temp);
        dest.writeString(this.member_ph);

    }

    public static final Creator<PersonalData> CREATOR = new Creator<PersonalData>() {
        @Override
        public PersonalData createFromParcel(Parcel in) {
            return new PersonalData(in);
        }

        @Override
        public PersonalData[] newArray(int size) {
            return new PersonalData[size];
        }
    };
}