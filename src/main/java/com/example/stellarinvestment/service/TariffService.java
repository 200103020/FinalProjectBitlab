package com.example.stellarinvestment.service;

import com.example.stellarinvestment.model.project.Tariff;
import com.example.stellarinvestment.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    public Tariff getTariffById(Integer id) {
        return tariffRepository.findById(id).get();
    }
}
