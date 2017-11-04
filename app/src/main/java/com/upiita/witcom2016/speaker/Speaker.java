package com.upiita.witcom2016.speaker;

/**
 * Created by edgarhz on 10/01/16.
 */
public class Speaker {
    private String image;
    private String name;
    private String from;
    private String conference;
    private String surname;
    private String birthdate;
    private String photo;
    private String resume;
    private String email;
    private String phone;
    private String provenance;

    private boolean expanded;

    public Speaker(String image, String name, String from, String conference, String surname, String birthdate, String photo, String resume, String email, String phone, String provenance) {
        this.image = image;
        this.name = name;
        this.from = from;
        this.conference = conference;
        this.surname = surname;
        this.birthdate = birthdate;
        this.photo = photo;
        this.resume = resume;
        this.email = email;
        this.phone = phone;
        this.provenance = provenance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
