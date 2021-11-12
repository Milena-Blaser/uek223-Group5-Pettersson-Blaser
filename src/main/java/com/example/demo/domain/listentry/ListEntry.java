package com.example.demo.domain.listentry;

import com.example.demo.domain.appUser.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "list_entry")
@Table
@Getter @Setter
@NoArgsConstructor
public class ListEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "importance")
    private int importance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public ListEntry(UUID id, String title, String text, LocalDate creationDate, int importance, User user) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.creationDate = creationDate;
        this.importance = importance;
        this.user = user;
    }

}