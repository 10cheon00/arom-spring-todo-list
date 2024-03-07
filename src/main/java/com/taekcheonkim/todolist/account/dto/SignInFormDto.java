package com.taekcheonkim.todolist.account.dto;

public class SignInFormDto {
    private final String email;
    private final String password;

    public SignInFormDto() {
        this.email = "";
        this.password = "";
    }

    public SignInFormDto(String email, String password) {
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

        SignInFormDto signInFormDto = (SignInFormDto) o;
        return email.equals(signInFormDto.getEmail()) && password.equals(signInFormDto.getPassword());
    }
}
