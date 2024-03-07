package com.taekcheonkim.todolist.todo.dto;

import java.util.Date;

public class TodoFormDto {
    private Long id;
    private String title;
    private String description;
    private boolean isDone;
    private Date endDate;
    private Date startDate;

    public TodoFormDto(Long savedTodoId, String title, String newDescription, Boolean o, Object startDate, Object endDate) {
        this("", "");
    }

    public TodoFormDto(String title, String description) {
        this("", "", null);
    }

    public TodoFormDto(String title, String description, Date endDate) {
        this.title = title;
        this.description = description;
        this.endDate = endDate;
    }

    public TodoFormDto(Long id, String title, String description, boolean isDone, Date startDate, Date endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
