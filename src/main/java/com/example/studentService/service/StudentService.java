package com.example.studentService.service;

import com.example.studentService.VO.Faculty;
import com.example.studentService.VO.StudentVO;
import com.example.studentService.entity.RateLimiterUpdate;
import com.example.studentService.entity.Student;
import com.example.studentService.repo.StudentRepository;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@Slf4j
public class StudentService{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${url.faculty}")
    private String url;

    @Autowired
    private RPMRateLimited rpmRateLimited;

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public Student saveStudent(Student student){
        log.info("save student : ", student.toString());
        return studentRepository.save(student);
    }

    @RateLimiter(name = "basicRateLimiter")
    public Student findStudentById(int id){
        log.info(LocalDateTime.now().format(formatter));
        return studentRepository.findById(id).get();
    }

    @RateLimiter(name = "multipleRateLimiters_rps_limiter")
    public Student findStudentByIdRPSRateLimited(int id){
        return rpmRateLimited.findStudentByIdRPMRateLimited(id,studentRepository);
    }

    public void updateRateLimites(RateLimiterUpdate rateLimiterUpdate){
        io.github.resilience4j.ratelimiter.RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(rateLimiterUpdate.getRateLimiterName());
        rateLimiter.changeLimitForPeriod(rateLimiterUpdate.getNewLimitForPeriod());
        rateLimiter.changeTimeoutDuration(rateLimiterUpdate.getNewTimeoutDuration());
        log.info("Update-Rate_Limiters");
    }

    @Retry(name = "retryAndRateLimitExample")
    @RateLimiter(name = "retryAndRateLimitExample")
    public StudentVO findStudentByIdWithFaculty(Integer id){
        log.info("find student by id : " + id + " with faculty");
        log.info(LocalDateTime.now().format(formatter));
        StudentVO studentVO = null;
        Student student = studentRepository.findById(id).get();
        Faculty faculty = restTemplate.getForObject(url+student.getFacultyId(),Faculty.class);
        studentVO = new StudentVO(student,faculty);
        return studentVO;

    }

    @RateLimiter(name = "fallbackExample", fallbackMethod = "fallbackExampleMethod")
    public Student fallbackExample(int id){
        log.info(LocalDateTime.now().format(formatter));
        return studentRepository.findById(id).get();
    }


    private Student fallbackExampleMethod(RequestNotPermitted requestNotPermitted){
        return Student.builder()
                .id(9999)
                .fullName("Fallback Example")
                .yearOfBirth(LocalDateTime.now().format(formatter))
                .facultyId(9999)
                .build();
    }


    @Component
    class RPMRateLimited{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
        @RateLimiter(name = "multipleRateLimiters_rpm_limiter")
        public Student findStudentByIdRPMRateLimited(int id, StudentRepository studentRepository){
            log.info(LocalDateTime.now().format(formatter) + " RPM " + id);
            return studentRepository.findById(id).get();
        }
    }

}
