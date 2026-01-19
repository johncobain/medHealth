package br.edu.ifba.inf012.medHealthAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UniqueAttributeAlreadyRegisteredException extends ResponseStatusException {
    public UniqueAttributeAlreadyRegisteredException(String entity, String attribute) {
        super(HttpStatus.CONFLICT, entity + " " + attribute + " already registered");
    }

    public UniqueAttributeAlreadyRegisteredException(String attribute) {
        super(HttpStatus.CONFLICT, attribute + " already registered");
    }
}
