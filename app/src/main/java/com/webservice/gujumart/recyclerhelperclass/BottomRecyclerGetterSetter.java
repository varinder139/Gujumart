package com.webservice.gujumart.recyclerhelperclass;

public class BottomRecyclerGetterSetter {

    String image, title, mrpprice, price;

    public BottomRecyclerGetterSetter(String image, String title, String mrpprice, String price) {
        this.image = image;
        this.title = title;
        this.mrpprice = mrpprice;
        this.price = price;
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

    public String getMrpprice() {
        return mrpprice;
    }

    public void setMrpprice(String mrpprice) {
        this.mrpprice = mrpprice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
