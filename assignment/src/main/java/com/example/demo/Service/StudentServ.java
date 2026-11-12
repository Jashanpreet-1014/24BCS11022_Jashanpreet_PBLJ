package com.example.demo.Service;

import com.example.demo.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentServ {
	public Student addStudent(Student st);
	public List<Student> getallStudent();
	public Optional<Student> getStudent(int id);
	public Student updateStudent(int id,Student st);
	public boolean deleteStudent(int id);
	public List<Student> getbycourse(String course);
}
