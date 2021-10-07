package com.example.studentService.VO;

import com.example.studentService.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentVO {
    private Student student;
    private Faculty faculty;
}
