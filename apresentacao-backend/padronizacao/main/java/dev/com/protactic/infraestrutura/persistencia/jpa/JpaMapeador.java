package dev.com.protactic.infraestrutura.persistencia.jpa;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component 
public class JpaMapeador {

    private final ModelMapper modelMapper;


    public JpaMapeador(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    
    public <T> T map(Object source, Class<T> destinationType) {
        if (source == null) {
            return null;
        }
        return modelMapper.map(source, destinationType);
    }
}