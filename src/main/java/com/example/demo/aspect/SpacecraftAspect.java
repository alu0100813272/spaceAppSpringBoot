package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SpacecraftAspect {

	private static final Logger logger = LoggerFactory.getLogger(SpacecraftAspect.class);

	@Pointcut("execution(* com.example.demo.controller.SpacecraftController.getSpacecraftById(..)) && args(id)")
	public void getSpacecraftByIdPointcut(Long id) {
	}

	// Método que se ejecuta después del método de controlador
	// Aspecto de Logging: Se registrará un aviso cuando se pida una nave con un ID
	// negativo.
	@AfterReturning(pointcut = "getSpacecraftByIdPointcut(id)", returning = "result")
	public void logNegativeId(JoinPoint joinPoint, Long id) {
		if (id < 0) {
			logger.warn("Se ha solicitado una nave con un ID negativo: {}", id);
		}
	}
}
