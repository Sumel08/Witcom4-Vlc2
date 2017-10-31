package com.upiita.witcom2016.pager.util;

/**
 * Created by olemu on 30/10/2017.
 */

public class Developer {
    private String name;
    private String email;
    private String image;

    public Developer(String name, String email, String image) {
        this.name = name;
        this.email = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
