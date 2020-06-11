package org.example.domain;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@NoArgsConstructor
public class Students {

    List<Student> students = new ArrayList<>();

    private Students(List<Student> students) { this.students.addAll(students); }

    public static Students of(List<Student> list) { return new Students(list); }

    // For JAXB (Java bean standard)
    public List<Student> getStudents() { return students; }
}
