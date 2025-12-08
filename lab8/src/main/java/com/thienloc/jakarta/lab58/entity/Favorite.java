package com.thienloc.jakarta.lab58.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Favorite")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;
    
    @Column(name = "LikeDate")
    private Date likeDate;

    @ManyToOne
    @JoinColumn(name = "VideoId")
    private Video video;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;
}
