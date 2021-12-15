package com.example.loginandsignup;

public class fModel {
    String id, R_ID, name, phone, identify;

    public fModel(){

    }

    public fModel(String id, String R_ID, String name, String phone, String identify) {
        this.id = id;
        this.R_ID = R_ID;
        this.name = name;
        this.phone = phone;
        this.identify = identify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getR_ID() {
        return R_ID;
    }

    public void setR_ID(String r_ID) {
        R_ID = r_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
}
