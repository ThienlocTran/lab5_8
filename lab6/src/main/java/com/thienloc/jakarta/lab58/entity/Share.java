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
@Table(name = "Share")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;
    
    @Column(name = "Emails")
    private String emails;
    
    @Column(name = "ShareDate")
    private Date shareDate;
    
    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "VideoId")
    private Video video;
}
