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

    @Retry(name = "basic" , fallbackMethod = "fallbackMethodStudentFaculty")
    public StudentVO findStudentByIdWithFaculty(Integer id){
        log.info("find student by id : " + id + " with faculty");
        return findStudentFacultyMethod(id);
    }


    @Retry(name = "throwingException")
    public StudentVO findStudentByIdWithFacultyThrowExeption(){
        StudentVO studentVO = null;
        Student student = null;
        Faculty faculty = restTemplate.getForObject(url+"exception",Faculty.class);
        studentVO = new StudentVO(student,faculty);
        return studentVO;

    }

    @Retry(name = "exponential")
    public StudentVO findStudentByIdWithFacultyExponential(Integer id){
        log.info("find student faculty exponential");
        return findStudentFacultyMethod(id);
    }

    @Retry(name = "random")
    public StudentVO findStudentByIdWithFacultyRandom(Integer id){
        log.info("find student faculty exponential");
        return findStudentFacultyMethod(id);
    }

    public StudentVO fallbackMethodStudentFaculty(RuntimeException runtimeException){

        log.info("fallback method");
        StudentVO studentVO = StudentVO.builder().
                student(new Student(9999,"fallback Method","9999",9999))
                .faculty(new Faculty(9999,"fallback Method"))
                .build();
        return studentVO;
    }




    public StudentVO findStudentFacultyMethod(int id){
        StudentVO studentVO = null;
        Student student = studentRepository.findById(id).get();
        Faculty faculty = restTemplate.getForObject(url+student.getFacultyId(),Faculty.class);
        studentVO = new StudentVO(student,faculty);
        return studentVO;
    }

}
