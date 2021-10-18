package com.example.studentService.service;

import com.example.studentService.VO.Faculty;
import com.example.studentService.VO.StudentVO;
import com.example.studentService.entity.Student;
import com.example.studentService.repo.StudentRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${url.faculty}")
    private String url;

    public Student saveStudent(Student student){
        log.info("save student : ", student.toString());
        return studentRepository.save(student);
    }

    @Retry(name = "basic")
    public StudentVO findStudentByIdWithFaculty(Integer id){
        log.info("find student by id : " + id + " with faculty");
        StudentVO studentVO = null;
        Student student = studentRepository.findById(id).get();
        Faculty faculty = restTemplate.getForObject(url+student.getFacultyId(),Faculty.class);
        studentVO = new StudentVO(student,faculty);
        return studentVO;

    }

}
