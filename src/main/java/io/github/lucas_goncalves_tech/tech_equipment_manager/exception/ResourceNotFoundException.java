package io.github.lucas_goncalves_tech.tech_equipment_manager.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message){
        super(message);
    }
}
