package com.example.demo.exception;

public class SpacecraftNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SpacecraftNotFoundException(Long id) {
		super("No se encontró la nave espacial con ID: " + id);
	}

	public SpacecraftNotFoundException(String message) {
		super(message);
	}
}
