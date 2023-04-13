package com.deador.restshop.converter;

import com.deador.restshop.dto.marker.Convertible;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class DTOConverter {
    private final ModelMapper modelMapper;

    @Autowired
    public DTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T, D extends Convertible> D convertToEntity(T dto, Type entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public <T, D extends Convertible> T convertToDTO(D entity, Type dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }
}
