package dev.com.protactic.infraestrutura.persistencia.jpa;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component // 1. Define esta classe como um "Bean" do Spring
public class JpaMapeador {

    private final ModelMapper modelMapper;

    // 2. Injeção de Dependência: O Spring entrega o ModelMapper
    //    que criamos na classe ModelMapperConfig
    public JpaMapeador(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converte um objeto de origem para um tipo de destino.
     * Ex: mapeador.map(clube, ClubeJPA.class);
     * Ex: mapeador.map(clubeJPA, Clube.class);
     */
    public <T> T map(Object source, Class<T> destinationType) {
        if (source == null) {
            return null;
        }
        // 3. Usa a biblioteca ModelMapper para fazer a mágica
        return modelMapper.map(source, destinationType);
    }
}