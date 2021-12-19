
package com.safia.go4lunch.model.placeDetailResult;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OpeningHours implements Parcelable {

    private Boolean openNow;
    private List<Period> periods = null;
    private List<String> weekdayText = null;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.openNow);
        dest.writeList(this.periods);
        dest.writeStringList(this.weekdayText);
    }

    public void readFromParcel(Parcel source) {
        this.openNow = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.periods = new ArrayList<Period>();
        source.readList(this.periods, Period.class.getClassLoader());
        this.weekdayText = source.createStringArrayList();
    }

    public OpeningHours() {
    }

    protected OpeningHours(Parcel in) {
        this.openNow = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.periods = new ArrayList<Period>();
        in.readList(this.periods, Period.class.getClassLoader());
        this.weekdayText = in.createStringArrayList();
    }

    public static final Parcelable.Creator<OpeningHours> CREATOR = new Parcelable.Creator<OpeningHours>() {
        @Override
        public OpeningHours createFromParcel(Parcel source) {
            return new OpeningHours(source);
        }

        @Override
        public OpeningHours[] newArray(int size) {
            return new OpeningHours[size];
        }
    };
}
