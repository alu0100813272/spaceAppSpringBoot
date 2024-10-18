package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.example.demo.entity.Spacecraft;
import com.example.demo.repository.SpacecraftRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SpacecraftService {

	@Autowired
	private SpacecraftRepository spacecraftRepository;

	// Consultar todas las naves utilizando paginación
	public Page<Spacecraft> findAll(Pageable pageable) {
		return spacecraftRepository.findAll(pageable);
	}

	// Consultar una única nave por ID
	@Cacheable(value = "spacecrafts", key = "#id") // Cachear por id
	public Optional<Spacecraft> findById(Long id) {
		return spacecraftRepository.findById(id);
	}

	// Consultar naves que contienen un valor en su nombre
	public List<Spacecraft> findByNameContaining(String name) {
		return spacecraftRepository.findByNameContaining(name);
	}

	// Crear una nueva nave
	public Spacecraft save(Spacecraft spacecraft) {
		return spacecraftRepository.save(spacecraft);
	}

	// Modificar una nave
	@CacheEvict(value = "spacecrafts", key = "#id") // Elimina el caché cuando se actualiza
	public Optional<Spacecraft> update(Long id, Spacecraft spacecraft) {
		if (spacecraftRepository.existsById(id)) {
			spacecraft.setId(id);
			return Optional.of(spacecraftRepository.save(spacecraft));
		} else {
			return Optional.empty();
		}
	}

	// Eliminar una nave
	public void delete(Long id) {
		spacecraftRepository.deleteById(id);
	}
}