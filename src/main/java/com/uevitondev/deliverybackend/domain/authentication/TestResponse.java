package com.uevitondev.deliverybackend.domain.authentication;

public class TestResponse {
    private String text;

    public TestResponse(){

    }

    public TestResponse(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
