package com.example.demo.domain.listentry.dto;


import com.example.demo.domain.listentry.ListEntry;

public class ListEntryDTOForOutput {

    private String title;

    private String text;

    private String creationDate;

    private int importance;

    private String username;

    public ListEntryDTOForOutput( String title, String text, String creationDate, int importance, String username) {
        this.username = username;
        this.title = title;
        this.text = text;
        this.creationDate = creationDate;
        this.importance = importance;
    }

    public ListEntryDTOForOutput(ListEntry listEntry) {
        this.username = listEntry.getUser().getUsername();
        this.title = listEntry.getTitle();
        this.text = listEntry.getText();
        this.creationDate = listEntry.getCreationDate().toString();
        this.importance = listEntry.getImportance();
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

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
