package com.example.rentalCar.unitTests;

import com.example.rentalCar.model.Delegation;
import com.example.rentalCar.repository.DelegationRepository;
import com.example.rentalCar.service.DelegationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DelegationServiceTest {

    @Mock
    private DelegationRepository delegationRepository;

    @InjectMocks
    private DelegationServiceImpl delegationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveDelegation() {
        Delegation delegation = new Delegation();
        delegation.setDelegationId("del001");

        delegationService.saveDelegation(delegation);

        verify(delegationRepository, times(1)).save(delegation);
    }

    @Test
    void getDelegationById() {
        String delegationId = "del001";
        String operation = "delegation#info";
        Delegation expectedDelegation = new Delegation();
        when(delegationRepository.findById(delegationId, operation)).thenReturn(expectedDelegation);

        Delegation result = delegationService.getDelegationById(delegationId, operation);

        assertEquals(expectedDelegation, result);
        verify(delegationRepository, times(1)).findById(delegationId, operation);
    }

    @Test
    void deleteDelegation() {
        String delegationId = "del001";
        String operation = "delegation#info";

        delegationService.deleteDelegation(delegationId, operation);

        verify(delegationRepository, times(1)).deleteById(delegationId, operation);
    }
}

