package com.AstronSpringHomework.App.dto;

public class UserEvent {
    private String email;
    private String event;

    public UserEvent() {
    }

    public UserEvent(String email, String event) {
        this.email = email;
        this.event = event;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
