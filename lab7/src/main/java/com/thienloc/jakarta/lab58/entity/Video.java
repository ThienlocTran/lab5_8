package com.thienloc.jakarta.lab58.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Video")
public class Video {
    @Id
    @Column(name = "Id")
    private String id;
    @Column(name = "Title")
    private String title;
    @Column(name = "Poster")
    private String poster;
    @Column(name = "Views")
    private int views;
    @Column(name = "Description")
    private String description;
    @Column(name = "Active")
    private boolean active;

    @OneToMany(mappedBy = "video")
    private List<Favorite> favorites;
}
