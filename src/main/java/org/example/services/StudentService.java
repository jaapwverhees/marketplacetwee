package org.example.services;

import org.example.dao.StudentDao;
import org.example.domain.Student;
import org.example.domain.Students;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Stateless
public class StudentService {

    @Inject
    private StudentDao studentDao;

    List<Student> students = new ArrayList<>();

    public Student add(Student student) {
        return studentDao.insert(student);
    }

    public Student get(int id) { return studentDao.get(id); }

    public Students getAll() {
        return Students.of(students);
    }

    public Student remove(int id) {
        return students.remove(id);
    }

    private List<Student> filterStudentsBy(String lastname) {
        return students.stream()
                .filter(s -> lastname.toUpperCase().contains(s.getLastname().toUpperCase()))
                .collect(toList());
    }

    public Students find(String lastname) {
        return Students.of(filterStudentsBy(lastname));
    }
}
