package com.example.demo.controller;

import com.example.demo.entity.Spacecraft;
import com.example.demo.exception.SpacecraftNotFoundException;
import com.example.demo.service.SpacecraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/spacecraft")
public class SpacecraftController {

	@Autowired
	private SpacecraftService spacecraftService;

	// Consultar una única nave por id
	@GetMapping("/{id}")
	public ResponseEntity<Spacecraft> getSpacecraftById(@PathVariable Long id) {
		return spacecraftService.findById(id).map(spacecraft -> ResponseEntity.ok(spacecraft))
				.orElseThrow(() -> new SpacecraftNotFoundException(id)); // Lanzar excepción si no se encuentra
	}
	
	// Consultar todas las naves utilizando paginación
	@GetMapping
	public ResponseEntity<Page<Spacecraft>> getAllSpacecrafts(Pageable pageable) {
		Page<Spacecraft> spacecrafts = spacecraftService.findAll(pageable);
		return new ResponseEntity<>(spacecrafts, HttpStatus.OK);
	}

	// Consultar todas las naves que contienen en su nombre el valor de un parámetro
	@GetMapping("/search")
	public ResponseEntity<List<Spacecraft>> getSpacecraftByName(@RequestParam String name) {
		List<Spacecraft> spacecrafts = spacecraftService.findByNameContaining(name);
		return new ResponseEntity<>(spacecrafts, HttpStatus.OK);
	}

	// Crear una nueva nave
	@PostMapping
	public ResponseEntity<Spacecraft> createSpacecraft(@RequestBody Spacecraft spacecraft) {
		Spacecraft createdSpacecraft = spacecraftService.save(spacecraft);
		return new ResponseEntity<>(createdSpacecraft, HttpStatus.CREATED);
	}

	// Modificar una nave
	@PutMapping("/{id}")
	public ResponseEntity<Spacecraft> updateSpacecraft(@PathVariable Long id, @RequestBody Spacecraft spacecraft) {
		Optional<Spacecraft> updatedSpacecraft = spacecraftService.update(id, spacecraft);
		return updatedSpacecraft.map(s -> new ResponseEntity<>(s, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Eliminar una nave
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSpacecraft(@PathVariable Long id) {
		spacecraftService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
