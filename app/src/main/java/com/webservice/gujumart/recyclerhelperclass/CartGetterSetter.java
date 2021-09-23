package com.webservice.gujumart.recyclerhelperclass;

import java.util.ArrayList;

public class CartGetterSetter {

    String image, title, shortdes, mrpprice;
            int rsprice;
    String productid, skuid, longdesc, variation_selected_itm;
    int qty;
    ArrayList<String> variationid = new ArrayList<>();

    public CartGetterSetter(String image, String title, String shortdes,
                            String mrpprice, int rsprice, String productid,
                            String skuid, String longdesc, String variation_selected_itm, int qty, ArrayList<String> variationid) {
        this.image = image;
        this.title = title;
        this.shortdes = shortdes;
        this.mrpprice = mrpprice;
        this.rsprice = rsprice;
        this.productid = productid;
        this.skuid = skuid;
        this.longdesc = longdesc;
        this.variation_selected_itm = variation_selected_itm;
        this.qty = qty;
        this.variationid = variationid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortdes() {
        return shortdes;
    }

    public void setShortdes(String shortdes) {
        this.shortdes = shortdes;
    }

    public String getMrpprice() {
        return mrpprice;
    }

    public void setMrpprice(String mrpprice) {
        this.mrpprice = mrpprice;
    }

    public int getRsprice() {
        return rsprice;
    }

    public void setRsprice(int rsprice) {
        this.rsprice = rsprice;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

    public String getLongdesc() {
        return longdesc;
    }

    public void setLongdesc(String longdesc) {
        this.longdesc = longdesc;
    }

    public String getVariation_selected_itm() {
        return variation_selected_itm;
    }

    public void setVariation_selected_itm(String variation_selected_itm) {
        this.variation_selected_itm = variation_selected_itm;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public ArrayList<String> getVariationid() {
        return variationid;
    }

    public void setVariationid(ArrayList<String> variationid) {
        this.variationid = variationid;
    }
}
