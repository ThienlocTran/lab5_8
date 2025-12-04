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
@Table(name = "[User]") // Using square brackets because 'User' is a reserved keyword in SQL Server
public class User {
    @Id
    @Column(name = "id_User")
    private String id;
    
    @Column(name = "fullName")
    private String fullName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password_User")
    private String password;
    
    @Column(name = "Admin_User", nullable = false)
    private boolean admin;

    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites;
}
