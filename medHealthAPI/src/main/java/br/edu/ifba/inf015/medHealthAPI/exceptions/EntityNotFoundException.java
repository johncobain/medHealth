package br.edu.ifba.inf015.medHealthAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntityNotFoundException extends ResponseStatusException {
    public EntityNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public EntityNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Entity not found with id: " + id);
    }

    public EntityNotFoundException(String name, Long id) {
        super(HttpStatus.NOT_FOUND, name + " not found with id: " + id);
    }


}