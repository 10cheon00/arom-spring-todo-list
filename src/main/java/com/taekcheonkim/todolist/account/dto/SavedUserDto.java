package com.taekcheonkim.todolist.account.dto;

public class SavedUserDto {
    private String email;
    private String nickname;

    public SavedUserDto() {
        this("", "");
    }

    public SavedUserDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
