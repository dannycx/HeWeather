package com.dcx.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 市
 */
public class CityBean implements Parcelable {
    public static final Parcelable.Creator<CityBean> CREATOR = new Parcelable.Creator<CityBean>() {
        @Override
        public CityBean createFromParcel(Parcel source) {
            return new CityBean(source);
        }

        @Override
        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };
    private String id;
    private String name;
    private String zipcode;
    private ArrayList<DistrictBean> cityList;

    public CityBean() {
    }

    protected CityBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.zipcode = in.readString();
        this.cityList = new ArrayList<>();
        in.readList(this.cityList, DistrictBean.class.getClassLoader());
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<DistrictBean> getCityList() {
        return cityList;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.zipcode);
        dest.writeList(this.cityList);
    }
}
