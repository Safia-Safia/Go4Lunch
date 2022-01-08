
package com.safia.go4lunch.model.placeDetailResult;

import android.os.Parcel;
import android.os.Parcelable;

public class Period implements Parcelable {

    public Close close;
    public Open open;

    public Close getClose() {
        return close;
    }

    public void setClose(Close close) {
        this.close = close;
    }

    public Open getOpen() {
        return open;
    }

    public void setOpen(Open open) {
        this.open = open;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.close, flags);
        dest.writeParcelable(this.open, flags);
    }

    public void readFromParcel(Parcel source) {
        this.close = source.readParcelable(Close.class.getClassLoader());
        this.open = source.readParcelable(Open.class.getClassLoader());
    }

    public Period() {
    }

    protected Period(Parcel in) {
        this.close = in.readParcelable(Close.class.getClassLoader());
        this.open = in.readParcelable(Open.class.getClassLoader());
    }

    public static final Parcelable.Creator<Period> CREATOR = new Parcelable.Creator<Period>() {
        @Override
        public Period createFromParcel(Parcel source) {
            return new Period(source);
        }

        @Override
        public Period[] newArray(int size) {
            return new Period[size];
        }
    };
}
