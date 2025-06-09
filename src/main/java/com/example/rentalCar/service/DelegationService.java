package com.example.rentalCar.service;

import com.example.rentalCar.model.Delegation;

public interface DelegationService {
    void saveDelegation(Delegation delegation);
    Delegation getDelegationById(String delegationId, String operation);
    void deleteDelegation(String delegationId, String operation);
}
