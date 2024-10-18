package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Spacecraft;

import java.util.List;

public interface SpacecraftRepository extends JpaRepository<Spacecraft, Long> {
	// Método para encontrar naves por nombre que contenga una subcadena
	List<Spacecraft> findByNameContaining(String name);
}
