package com.upiita.witcom2016.speaker;

/**
 * Created by edgarhz on 10/01/16.
 */
public class Speaker {
    private String image;
    private String name;
    private String from;
    private String conference;

    public Speaker(String image, String name, String from, String conference) {
        this.image = image;
        this.name = name;
        this.from = from;
        this.conference = conference;
    }

    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public String getImage() { return image; }

    public String getConference() { return conference; }
}
