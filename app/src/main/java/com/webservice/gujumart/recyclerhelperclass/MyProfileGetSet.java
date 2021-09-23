package com.webservice.gujumart.recyclerhelperclass;

public class MyProfileGetSet {

    String name, mobile, email, pinode, district, full_address, useid, addsid;

    public MyProfileGetSet(String name, String mobile, String email, String pinode, String district, String full_address, String useid, String addsid) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.pinode = pinode;
        this.district = district;
        this.full_address = full_address;
        this.useid = useid;
        this.addsid = addsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPinode() {
        return pinode;
    }

    public void setPinode(String pinode) {
        this.pinode = pinode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getUseid() {
        return useid;
    }

    public void setUseid(String useid) {
        this.useid = useid;
    }

    public String getAddsid() {
        return addsid;
    }

    public void setAddsid(String addsid) {
        this.addsid = addsid;
    }
}
