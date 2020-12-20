package com.personal.pharmacy.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.personal.pharmacy.model.Prescription;
import com.personal.pharmacy.repository.PrescriptionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

	private final PrescriptionRepository prescriptionRepository;
	
	@Override
	public Prescription save(Prescription prescription) {
		return prescriptionRepository.save(prescription);
	}

	@Override
	public void delete(Prescription prescription) {
		prescriptionRepository.delete(prescription);
	}

	@Override
	public List<Prescription> findAll() {
		return (List<Prescription>) prescriptionRepository.findAll();
	}

	@Override
	public Prescription findById(Long id) {
		
		Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(id);
		if (!prescriptionOptional.isPresent()) {
			throw new RuntimeException("No medicine with given id");
		}
		return prescriptionOptional.get();
	}

}