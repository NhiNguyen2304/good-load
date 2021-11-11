package com.gooddata.rubik.hackathon.goodload.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.joda.time.DateTime;

public class Opportunity {

    private String id;
    private String name;
    private String title;
    private double price;
    private DateTime date;

    public Opportunity() {
    }

    public Opportunity(String id, String name, String title, double price, DateTime date) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.price = price;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
