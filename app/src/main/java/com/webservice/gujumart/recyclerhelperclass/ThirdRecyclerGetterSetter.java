package com.webservice.gujumart.recyclerhelperclass;

public class ThirdRecyclerGetterSetter {


    private String img;
    private String title;
    private String id;

    public ThirdRecyclerGetterSetter(String img, String title, String id) {
        this.img = img;
        this.title = title;
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
