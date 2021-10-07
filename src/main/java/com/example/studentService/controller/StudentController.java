package com.example.studentService.controller;

import com.example.studentService.VO.StudentVO;
import com.example.studentService.entity.Student;
import com.example.studentService.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Value("${messenger.author}")
    private String message;

    @Value("${messenger}")
    private String messengerFromCloud;

    @PostMapping("/")
    public Student saveStudent(@RequestBody @Valid Student student){
        log.info("student controller - save student api : " + student.toString());
        return studentService.saveStudent(student);
    }

    @GetMapping("/{id}")
    public StudentVO getStudentWithFaculty(@PathVariable("id") Integer id){
        log.info("student controller - get student with faculty");
        return studentService.findStudentByIdWithFaculty(id);
    }


    @GetMapping("/")
    public String hello(){
        return message;
    }


    @GetMapping("/cloud-config")
    public String getMessengerFromCloudConfigServer(){
        return messengerFromCloud;
    }

}
