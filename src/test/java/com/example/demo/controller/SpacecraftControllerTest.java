package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.entity.Spacecraft;
import com.example.demo.exception.SpacecraftNotFoundException;
import com.example.demo.service.SpacecraftService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class SpacecraftControllerTest {

	@InjectMocks
	private SpacecraftController spacecraftController;

	@Mock
	private SpacecraftService spacecraftService;

	private MockMvc mockMvc;

	private Spacecraft spacecraft1;
	private Spacecraft spacecraft2;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		spacecraft1 = new Spacecraft();
		spacecraft1.setId(1L);
		spacecraft1.setName("X-Wing");
		spacecraft1.setSeries("Rebel Alliance");

		spacecraft2 = new Spacecraft();
		spacecraft2.setId(2L);
		spacecraft2.setName("TIE Fighter");
		spacecraft2.setSeries("Galactic Empire");

		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(spacecraftController).build();
	}

	@Test
	void testGetAllSpacecrafts() {
		List<Spacecraft> spacecrafts = Arrays.asList(spacecraft1, spacecraft2);
		Page<Spacecraft> page = new PageImpl<>(spacecrafts);
		when(spacecraftService.findAll(any(Pageable.class))).thenReturn(page);

		ResponseEntity<Page<Spacecraft>> response = spacecraftController.getAllSpacecrafts(Pageable.unpaged());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(spacecrafts.size(), response.getBody().getContent().size());
	}

	@Test
    void testGetSpacecraftById() {
        when(spacecraftService.findById(anyLong())).thenReturn(Optional.of(spacecraft1));

        ResponseEntity<Spacecraft> response = spacecraftController.getSpacecraftById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(spacecraft1, response.getBody());
    }

	@Test
    void testGetSpacecraftByIdNotFound() {
        when(spacecraftService.findById(anyLong())).thenReturn(Optional.empty());
        // Verificar que se lanza la excepción esperada
        SpacecraftNotFoundException exception = assertThrows(
                SpacecraftNotFoundException.class,
                () -> spacecraftController.getSpacecraftById(1L)
        );

        // Verificar el mensaje de la excepción
        assertEquals("No se encontró la nave espacial con ID: 1", exception.getMessage());
    }

	@Test
    void testGetSpacecraftByName() {
        when(spacecraftService.findByNameContaining("Wing")).thenReturn(Arrays.asList(spacecraft1));

        ResponseEntity<List<Spacecraft>> response = spacecraftController.getSpacecraftByName("Wing");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(spacecraft1, response.getBody().get(0));
    }

	@Test
    void testCreateSpacecraft() {
        when(spacecraftService.save(any(Spacecraft.class))).thenReturn(spacecraft1);

        ResponseEntity<Spacecraft> response = spacecraftController.createSpacecraft(spacecraft1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(spacecraft1, response.getBody());
    }

	@Test
    void testUpdateSpacecraft() {
        when(spacecraftService.update(anyLong(), any(Spacecraft.class))).thenReturn(Optional.of(spacecraft1));

        ResponseEntity<Spacecraft> response = spacecraftController.updateSpacecraft(1L, spacecraft1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(spacecraft1, response.getBody());
    }

	@Test
    void testUpdateSpacecraftNotFound() {
        when(spacecraftService.update(anyLong(), any(Spacecraft.class))).thenReturn(Optional.empty());

        ResponseEntity<Spacecraft> response = spacecraftController.updateSpacecraft(1L, spacecraft1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

	@Test
	void testDeleteSpacecraft() {
		doNothing().when(spacecraftService).delete(anyLong());

		ResponseEntity<Void> response = spacecraftController.deleteSpacecraft(1L);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(spacecraftService, times(1)).delete(1L);
	}

	@Test
	public void testAspectNegativeId() throws Exception {
		// Verificar que se lanza IllegalArgumentException para ID negativo
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> spacecraftController.getSpacecraftById(-1L));

		// Verificar el mensaje de la excepción
		assertEquals("El ID no puede ser negativo: -1", exception.getMessage());
	}

	@Test
	public void testCaching() throws Exception {
		Spacecraft spacecraft = new Spacecraft();
		spacecraft.setId(1L);
		spacecraft.setName("X-Wing");

		when(spacecraftService.findById(1L)).thenReturn(Optional.of(spacecraft));

		// Primer llamado, debe ir a la base de datos
		mockMvc.perform(get("/api/spacecraft/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("X-Wing"));

		// Segundo llamado, debe obtenerse del caché
		mockMvc.perform(get("/api/spacecraft/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("X-Wing"));
	}
}
