package com.taekcheonkim.todolist.user.domain;

import com.taekcheonkim.todolist.user.dto.SignUpFormDto;
import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;

    public User() {
        this("", "", "");
    }

    public User(SignUpFormDto signUpFormDto) {
        this(signUpFormDto.getEmail(), signUpFormDto.getPassword(), signUpFormDto.getNickname());
    }

    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
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