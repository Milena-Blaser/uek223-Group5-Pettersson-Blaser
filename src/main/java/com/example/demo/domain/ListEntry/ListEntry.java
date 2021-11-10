package com.example.demo.domain.ListEntry;

import com.example.demo.domain.appUser.User;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity(name = "list_entry")
@Table
public class ListEntry {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "importance")
    private Importance importance;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;





}
