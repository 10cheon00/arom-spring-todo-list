package com.taekcheonkim.todolist.todo.domain;

import com.taekcheonkim.todolist.todo.dto.TodoFormDto;
import com.taekcheonkim.todolist.user.domain.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(columnDefinition = "BIT(1) DEFAULT b'1'", nullable = false)
    private boolean isDone;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date startDate;
    private Date endDate;
    @ManyToOne(targetEntity = User.class)
    private User author;

    public Todo() {
        this.isDone = false;
        this.startDate = new Date();
        this.title = "";
        this.description = "";
    }

    public Todo(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public Todo(TodoFormDto todoFormDto) {
        this.title = todoFormDto.getTitle();
        this.description = todoFormDto.getDescription();
        this.startDate = todoFormDto.getStartDate();
        this.endDate = todoFormDto.getEndDate();
        this.isDone = todoFormDto.isDone();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return this.author;
    }
}
