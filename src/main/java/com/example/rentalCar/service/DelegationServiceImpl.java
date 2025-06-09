package com.example.rentalCar.service;

import com.example.rentalCar.model.Delegation;
import com.example.rentalCar.repository.DelegationRepository;
import org.springframework.stereotype.Service;

@Service
public class DelegationServiceImpl implements DelegationService {
    private final DelegationRepository delegationRepository;

    public DelegationServiceImpl(DelegationRepository delegationRepository) {
        this.delegationRepository = delegationRepository;
    }

    @Override
    public void saveDelegation(Delegation delegation) {
        delegationRepository.save(delegation);
    }

    @Override
    public Delegation getDelegationById(String delegationId, String operation) {
        return delegationRepository.findById(delegationId, operation);
    }

    @Override
    public void deleteDelegation(String delegationId, String operation) {
        delegationRepository.deleteById(delegationId, operation);
    }
}

