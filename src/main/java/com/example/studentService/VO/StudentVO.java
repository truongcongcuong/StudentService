package com.example.studentService.VO;

import com.example.studentService.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentVO {
    private Student student;
    private Faculty faculty;
}
