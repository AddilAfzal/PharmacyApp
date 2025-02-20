package com.personal.pharmacy.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.pharmacy.model.Employee;
import com.personal.pharmacy.services.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("employee/")
public class EmployeeController implements CrudController<Employee, Long>{
	
	private final EmployeeService employeeService;
	
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Override
	public ResponseEntity<?> getById(Long id){
		return employeeService.findById(id).isEmpty()		
		? new ResponseEntity<>(HttpStatus.NOT_FOUND)
		: new ResponseEntity<>(employeeService.findById(id).get(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteById(Long id) {
		Optional<Employee> employeeOptional = employeeService.findById(id);
		if (employeeOptional.isEmpty()) {
			log.info("Id not present in database");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Employee employee = employeeOptional.get();
		employeeService.delete(employee);
		return new ResponseEntity<>("Employee Deleted", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> add(Employee employee, BindingResult bindingResult){
		if (bindingResult.hasFieldErrors()) {
			List<String> errorStrings = new ArrayList<>();
			bindingResult.getFieldErrors().forEach(objectError -> {
				errorStrings.add(objectError.getDefaultMessage());
			});
			return new ResponseEntity<>(errorStrings.toString(), HttpStatus.BAD_REQUEST);
		}
		Employee savedEmployee = employeeService.save(employee);
		return new ResponseEntity<>(savedEmployee, HttpStatus.OK);
	}
	
	@PatchMapping("{id}/updatefirstname")
	public ResponseEntity<?> updateEmployeeFirstName(@PathVariable Long id, @RequestBody String firstName){
		Optional<Employee> employeeOptional = employeeService.findById(id);
		if (employeeOptional.isEmpty()) {
			log.info("Id not present in database");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Employee employee = employeeOptional.get();
		employeeService.updateFirstName(employee, firstName);
		return new ResponseEntity<>(employee, HttpStatus.OK);
	}
	
}
