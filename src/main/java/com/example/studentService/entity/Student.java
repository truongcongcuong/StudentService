package com.example.studentService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "student")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name", columnDefinition = "NVARCHAR(255)")
    @NotBlank(message = "full name can't blank")
    private String fullName;

    @Column(name = "year_of_birth")
    private String yearOfBirth;

    @Column(name = "faculty_id")
    @NotNull(message = "faculty Id can't blank")
    private Integer facultyId;
}
