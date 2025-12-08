package com.thienloc.jakarta.lab58.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    private String id;
    private String name;
    private boolean gender;
    private double salary;
}
