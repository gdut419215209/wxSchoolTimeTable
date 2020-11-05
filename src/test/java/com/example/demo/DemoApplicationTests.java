package com.example.demo;

import com.example.demo.pojo.course;
import com.example.demo.pojo.originalCourse;
import com.example.demo.service.userService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private userService userService;

    @Test
    void contextLoads() {
        List<List<originalCourse>> originalCourse = userService.getOriginalCourseList("3118005421", "q652494798");
        List<List<course>> responseCourseList = userService.getResponseCourseList(originalCourse);
        System.out.println(responseCourseList);
    }

}
