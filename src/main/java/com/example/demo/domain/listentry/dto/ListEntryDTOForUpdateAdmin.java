package com.example.demo.domain.listentry.dto;

import com.example.demo.domain.listentry.Importance;

public class ListEntryDTOForUpdateAdmin {

    private String id;

    private String username;

    private String title;

    private String text;

    private String creationDate;

    private Importance importance;

    public ListEntryDTOForUpdateAdmin(String id, String username, String title, String text,
                                      String creationDate, Importance importance) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.text = text;
        this.creationDate = creationDate;
        this.importance = importance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
