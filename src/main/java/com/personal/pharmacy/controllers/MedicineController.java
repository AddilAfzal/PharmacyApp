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

import com.personal.pharmacy.model.Medicine;
import com.personal.pharmacy.services.MedicineService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("medicine/")
public class MedicineController implements CrudController<Medicine, Long>{
	
	private final MedicineService medicineService;
	
	public MedicineController(MedicineService medicineService) {
		this.medicineService = medicineService;
	}

	@Override
	public ResponseEntity<?> getById(Long id){
		return medicineService.findById(id).isEmpty()
		? new ResponseEntity<>(HttpStatus.NOT_FOUND)
		: new ResponseEntity<>(medicineService.findById(id).get(), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> deleteById(Long id){
		Optional<Medicine> medicineOptional = medicineService.findById(id);
		if (medicineOptional.isEmpty()) {
			log.info("Id not present in database");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		medicineService.delete(medicineOptional.get());
		return new ResponseEntity<>("Medicine deleted", HttpStatus.OK);
	}
	 
	@Override
	public ResponseEntity<?> add(Medicine medicine, BindingResult bindingResult){
		if (bindingResult.hasFieldErrors()) {
			List<String> errorStrings = new ArrayList<>();
			bindingResult.getFieldErrors().forEach(objectError -> {
				errorStrings.add(objectError.getDefaultMessage());
				
			});
			return new ResponseEntity<>(errorStrings.toString(), HttpStatus.BAD_REQUEST);
		}
		Medicine savedMedicine = medicineService.save(medicine);
		return new ResponseEntity<>(savedMedicine, HttpStatus.OK);
	}
	
	@PatchMapping("{id}/updatename")
	public ResponseEntity<?> updateMedicineName(@PathVariable Long id, @RequestBody String name){
		Optional<Medicine> medicineOptional = medicineService.findById(id);
		if (medicineOptional.isEmpty()) {
			log.info("Id not present in database");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Medicine medicine = medicineOptional.get();
		medicineService.updateName(medicine, name);
		return new ResponseEntity<>(medicine, HttpStatus.OK);
	}
	
}
