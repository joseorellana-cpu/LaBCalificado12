package com.tecsup.petclinic.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para el controlador REST.
 * Convierte excepciones de dominio en respuestas HTTP apropiadas.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja OwnerNotFoundException devolviendo 404 Not Found.
     */
    @ExceptionHandler(OwnerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleOwnerNotFound(OwnerNotFoundException ex) {
        log.warn("Owner not found: {}", ex.getMessage());
        return ex.getMessage();
    }

    /**
     * Maneja errores de validación devolviendo 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return ex.getMessage();
    }
}
