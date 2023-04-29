package com.example.projecta.model;

import java.util.ArrayList;

public class District {
    String districtName;
    ArrayList<String> addressList;
    int districtIcon;

    public District(String districtName, ArrayList<String> addressList, int districtIcon) {
        this.districtName = districtName;
        this.addressList = addressList;
        this.districtIcon = districtIcon;
    }

    public String getDistrictName() {
        return districtName;
    }

    public ArrayList<String> getAddressList() {
        return addressList;
    }

    public int getDistrictIcon() {
        return districtIcon;
    }
}
