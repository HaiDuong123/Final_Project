package com.example.final_project.data.model;

import java.util.Date;

public class Account {

    private String username;
    private String password;
    private String email;

    private Integer finalScore;
    private String level;
    private String lastTestTime;
    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
    public Integer getFinalScore() {
        return finalScore;
    }

    public String getLevel() {
        return level;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getLastTestTime() {
        return lastTestTime;
    }
}