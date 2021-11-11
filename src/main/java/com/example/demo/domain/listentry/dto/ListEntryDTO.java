package com.example.demo.domain.listentry.dto;

import com.example.demo.domain.appUser.User;
import com.example.demo.domain.listentry.Importance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;


public class ListEntryDTO {
    private String userID;

    private String title;

    private String text;

    private String creationDate;

    private Importance importance;


    public ListEntryDTO(String userID, String title, String text, String creationDate, Importance importance) {
        this.userID = userID;
        this.title = title;
        this.text = text;
        this.creationDate = creationDate;
        this.importance = importance;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
}
