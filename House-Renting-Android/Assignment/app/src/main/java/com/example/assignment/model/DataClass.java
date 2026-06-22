package com.example.assignment.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DataClass implements Parcelable {
    private int dataImage;
    private String dataTitle;
    private String dataDesc;

    public DataClass(int dataImage, String dataTitle, String dataDesc) {
        this.dataImage = dataImage;
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
    }

    protected DataClass(Parcel parcel) {
        dataImage = parcel.readInt();
        dataTitle = parcel.readString();
        dataDesc = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(dataImage);
        parcel.writeString(dataTitle);
        parcel.writeString(dataDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataClass> CREATOR = new Creator<DataClass>() {
        @Override
        public DataClass createFromParcel(Parcel parcel) {
            return new DataClass(parcel);
        }

        @Override
        public DataClass[] newArray(int size) {
            return new DataClass[size];
        }
    };

    public int getDataImage() { return dataImage; }
    public void setDataImage(int dataImage) { this.dataImage = dataImage; }

    public String getDataTitle() { return dataTitle; }
    public void setDataTitle(String dataTitle) { this.dataTitle = dataTitle; }

    public String getDataDesc() { return dataDesc; }
    public void setDataDesc(String dataDesc) { this.dataDesc = dataDesc; }
}
