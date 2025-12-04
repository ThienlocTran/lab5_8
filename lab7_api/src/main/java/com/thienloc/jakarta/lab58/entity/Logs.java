package com.thienloc.jakarta.lab58.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Logs")
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Url", columnDefinition = "NVARCHAR(MAX)")
    private String url;

    @Column(name = "Time")
    private LocalDateTime accessTime;

    @Column(name = "Username", length = 50)
    private String username;

    public Logs() {}

    public Logs(String url, LocalDateTime accessTime, String username) {
        this.url = url;
        this.accessTime = accessTime;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
