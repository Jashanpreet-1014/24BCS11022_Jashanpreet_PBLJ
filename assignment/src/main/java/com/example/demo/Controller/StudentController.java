package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.StudentServImp;
import com.example.demo.entity.Student;

@RestController
public class StudentController {
	@Autowired
	private final StudentServImp stserv;
	public StudentController(StudentServImp stserv){
		this.stserv = stserv;
	}
	@PostMapping("/addst")
	public ResponseEntity<Student> addStudent(@RequestBody Student st){
		Student res  = stserv.addStudent(st);
		if(res!=null) {
			return ResponseEntity.ok().body(res);
		}
		else {
			return ResponseEntity.notFound().build();
		}
		
	}
	@DeleteMapping("/delst/{id}")
	public ResponseEntity<Boolean>  deleteStudent(@PathVariable int id){
	boolean res = stserv.deleteStudent(id);
		if(res) {
			return  ResponseEntity.ok().body(res);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/getst/{id}")
	public ResponseEntity<Student> getStudent(@PathVariable int id){
		Optional<Student> res  = stserv.getStudent(id);
		if(res.isPresent()) {
			return  ResponseEntity.ok(res.get());
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/getst/")
	public ResponseEntity<List<Student>> getallStudent(){
		List<Student> ls  = stserv.getallStudent();
		return ResponseEntity.ok().body(ls);
	}
	@PutMapping("/updst/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable int id,@RequestBody Student st){
		Student res = stserv.updateStudent(id, st);
		if(res != null) {
			return  ResponseEntity.ok().body(res);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/st/course/{course}")
	public ResponseEntity<List<Student>> getbycourse(@PathVariable String course){
		List<Student> ls  = stserv.getbycourse(course);
		return ResponseEntity.ok(ls);
	}
	
}
