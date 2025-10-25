package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.model.IVA;
import com.proyecto.ecommerce.repository.IIvaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IvaService implements IIvaService{
    @Autowired
    private IIvaRepository ivaRepository;

    @Override
    public List<IVA> obtenerIva() {
        return ivaRepository.findAll();
    }
}
