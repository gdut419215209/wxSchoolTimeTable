package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class course {
    private String courseName;
    private String teacherName;
    private String className;
    private String time;
    private String weekNum;
    private String weekDay;
    private String location;
    private String content;

}
