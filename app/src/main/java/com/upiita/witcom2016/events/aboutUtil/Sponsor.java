package com.upiita.witcom2016.events.aboutUtil;

/**
 * Created by olemu on 29/10/2017.
 */

public class Sponsor {
    private String name;
    private String image;

    public Sponsor(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
