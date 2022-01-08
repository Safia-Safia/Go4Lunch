
package com.safia.go4lunch.model.placeDetailResult;

import android.os.Parcel;
import android.os.Parcelable;

public class Close implements Parcelable {

    private Integer day;
    private String time;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.day);
        dest.writeString(this.time);
    }

    public void readFromParcel(Parcel source) {
        this.day = (Integer) source.readValue(Integer.class.getClassLoader());
        this.time = source.readString();
    }

    public Close() {
    }

    protected Close(Parcel in) {
        this.day = (Integer) in.readValue(Integer.class.getClassLoader());
        this.time = in.readString();
    }

    public static final Parcelable.Creator<Close> CREATOR = new Parcelable.Creator<Close>() {
        @Override
        public Close createFromParcel(Parcel source) {
            return new Close(source);
        }

        @Override
        public Close[] newArray(int size) {
            return new Close[size];
        }
    };
}
