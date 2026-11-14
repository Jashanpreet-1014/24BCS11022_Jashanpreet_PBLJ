package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.StudentRepository;
import com.example.demo.entity.Student;
@Service
public class StudentServImp implements StudentServ{
	@Autowired
	private StudentRepository strepo ;
	@Override
	public Student addStudent(Student st) {
		Student st1 = strepo.save(st);
		return st1;
	}

	@Override
	public List<Student> getallStudent() {
		
		List<Student> ls = strepo.findAll();
		return ls;
	}

	@Override
	public Optional<Student> getStudent(int id) {
		
		Optional<Student> st1 = strepo.findById(id);
		return st1;
	}

	@Override
	public Student updateStudent(int id,Student st) {
		Student st1 = strepo.save(st);
		return st1;
	}

	@Override
	public boolean deleteStudent(int id) {
		if(strepo.existsById(id)) {
			strepo.deleteById(id);
			return true;
		}
		return false;
	}
	@Override
	public List<Student> getbycourse(String course) {
		List<Student> ls = strepo.findByCourse(course);
		return ls;
	}

}
