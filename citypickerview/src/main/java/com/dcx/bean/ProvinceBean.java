package com.dcx.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 省
 */
public class ProvinceBean implements Parcelable {

    public static final Parcelable.Creator<ProvinceBean> CREATOR = new Parcelable.Creator<ProvinceBean>() {
        @Override
        public ProvinceBean createFromParcel(Parcel source) {
            return new ProvinceBean(source);
        }

        @Override
        public ProvinceBean[] newArray(int size) {
            return new ProvinceBean[size];
        }
    };
    private String id;
    private String name;
    private String zipcode;
    private ArrayList<CityBean> cityList;

    public ProvinceBean() {
    }

    protected ProvinceBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.zipcode = in.readString();
        this.cityList = new ArrayList<>();
        in.readList(this.cityList, CityBean.class.getClassLoader());
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

    public ArrayList<CityBean> getCityList() {
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
