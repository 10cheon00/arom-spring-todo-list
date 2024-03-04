package com.taekcheonkim.todolist.account.dto;

public class LoginDto {
    private final String email;
    private final String password;

    public LoginDto() {
        this.email = "";
        this.password = "";
    }

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoginDto loginDto = (LoginDto) o;
        return email.equals(loginDto.getEmail()) && password.equals(loginDto.getPassword());
    }
}
