package com.taekcheonkim.todolist.account.dto;

public class UserFormDto {
    private String email;
    private String password;
    private String nickname;

    public UserFormDto(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public UserFormDto() {
        this.email = "";
        this.password = "";
        this.nickname = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
