package com.example.studentService.controller;

import com.example.studentService.VO.StudentVO;
import com.example.studentService.entity.RateLimiterUpdate;
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
    @GetMapping("/retry-and-rate-limiters/{id}")
    public String getStudentWithFacultyRetryAndRateLimiters(@PathVariable("id") Integer id){
        log.info("student controller - get student with faculty");
        for (int i=0;i<5;i++){
            System.out.println(studentService.findStudentByIdWithFaculty(id));
        }
        return "retry-and-rate-limiters";
    }

    @GetMapping("/rate-limiter/{id}")
    public String getStudentByIDRateLimiter(@PathVariable int id){
        log.info("rate-limiter");
        for (int i = 0;i<20;i++)
            System.out.println(studentService.findStudentById(id));
        return "rate-limiter";
    }


    @GetMapping("/rate-limiter-multiple/{id}")
    public String getStudentByIdRateLimiterMultiple(@PathVariable int id){
        log.info("rate-limiter-multiple");
        for (int i = 0; i<42;i++){
            System.out.println(studentService.findStudentByIdRPSRateLimited(id));
        }
        return "rate-limiter-multiple";
    }

    @PostMapping("/rate-limiter-change")
    public String changeLimitsAtRuntime(@RequestBody RateLimiterUpdate rateLimiterUpdate){
        log.info("rate-limiter-change");
        studentService.updateRateLimites(rateLimiterUpdate);
        return "rate-limiter-change";
    }

    @GetMapping("/rate-limiter-fallback/{id}")
    public String getStudentByIDRateLimiterFallback(@PathVariable int id){
        log.info("rate-limiter");
        for (int i = 0;i<5;i++)
            System.out.println(studentService.fallbackExample(id));
        return "rate-limiter";
    }


    @GetMapping("/")
    public String hello(){
        return message;
    }

}
