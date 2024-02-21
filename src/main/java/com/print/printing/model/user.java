package com.print.printing.model;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

public class user {
    private String orderId;
    private String date;


    public user(String orderId, String date) {
        this.orderId = orderId;
        this.date = date;

    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

