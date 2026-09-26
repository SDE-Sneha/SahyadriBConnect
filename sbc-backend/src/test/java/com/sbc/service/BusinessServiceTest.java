package com.sbc.service;

import com.sbc.dto.BusinessRequest;
import com.sbc.dto.BusinessResponse;
import com.sbc.exception.BusinessNotFoundException;
import com.sbc.exception.InvalidBusinessStatusTransitionException;
import com.sbc.model.Business;
import com.sbc.model.BusinessStatus;
import com.sbc.repository.IBusinessRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class BusinessServiceTest {

    private IBusinessRepository businessRepository;
    @BeforeEach
    void setUp() {

        businessRepository = mock(IBusinessRepository.class);

        businessService =
                new BusinessService(businessRepository);
    }

    private BusinessService businessService;

    @Test
    void shouldApprovePendingBusiness() {

        Business business = createBusiness(
                BusinessStatus.PENDING
        );

        when(businessRepository.findById("B100"))
                .thenReturn(Optional.of(business));

        businessService.updateBusinessStatus(
                "B100",
                BusinessStatus.APPROVED
        );

        assertEquals(
                BusinessStatus.APPROVED,
                business.getStatus()
        );
    }

    @Test
    void shouldRejectPendingBusiness() {

        Business business = createBusiness(
                BusinessStatus.PENDING
        );

        when(businessRepository.findById("B100"))
                .thenReturn(Optional.of(business));

        businessService.updateBusinessStatus(
                "B100",
                BusinessStatus.REJECTED
        );

        assertEquals(
                BusinessStatus.REJECTED,
                business.getStatus()
        );
    }

    @Test
    void shouldSuspendApprovedBusiness() {

        Business business = createBusiness(
                BusinessStatus.APPROVED
        );

        when(businessRepository.findById("B100"))
                .thenReturn(Optional.of(business));

        businessService.updateBusinessStatus(
                "B100",
                BusinessStatus.SUSPENDED
        );

        assertEquals(
                BusinessStatus.SUSPENDED,
                business.getStatus()
        );
    }

    @Test
    void shouldApproveSuspendedBusiness() {

        Business business = createBusiness(
                BusinessStatus.SUSPENDED
        );

        when(businessRepository.findById("B100"))
                .thenReturn(Optional.of(business));

        businessService.updateBusinessStatus(
                "B100",
                BusinessStatus.APPROVED
        );

        assertEquals(
                BusinessStatus.APPROVED,
                business.getStatus()
        );
    }

    @Test
    void shouldRejectInvalidPendingToSuspendedTransition() {

        Business business = createBusiness(
                BusinessStatus.PENDING
        );

        when(businessRepository.findById("B100"))
                .thenReturn(Optional.of(business));

        assertThrows(
                InvalidBusinessStatusTransitionException.class,
                () -> businessService.updateBusinessStatus(
                        "B100",
                        BusinessStatus.SUSPENDED
                )
        );

        assertEquals(
                BusinessStatus.PENDING,
                business.getStatus()
        );
    }

    @Test
    void shouldRejectInvalidApprovedToRejectedTransition() {

        Business business = createBusiness(
                BusinessStatus.APPROVED
        );

        when(businessRepository.findById("B100"))
                .thenReturn(Optional.of(business));

        assertThrows(
                InvalidBusinessStatusTransitionException.class,
                () -> businessService.updateBusinessStatus(
                        "B100",
                        BusinessStatus.REJECTED
                )
        );

        assertEquals(
                BusinessStatus.APPROVED,
                business.getStatus()
        );
    }

    @Test
    void shouldRejectInvalidRejectedToApprovedTransition() {

        Business business = createBusiness(
                BusinessStatus.REJECTED
        );

        when(businessRepository.findById("B100"))
                .thenReturn(Optional.of(business));

        assertThrows(
                InvalidBusinessStatusTransitionException.class,
                () -> businessService.updateBusinessStatus(
                        "B100",
                        BusinessStatus.APPROVED
                )
        );

        assertEquals(
                BusinessStatus.REJECTED,
                business.getStatus()
        );
    }

    private Business createBusiness(BusinessStatus status) {

        return new Business(
                "B100",
                "Sahyadri Restaurant",
                "Restaurant",
                "Vegetarian Indian food",
                "Surrey",
                "BC",
                "604-555-2025",
                "hello@sahyadri.example",
                "https://example.com",
                status
        );
    }

    @Test
    void shouldThrowExceptionWhenBusinessNotFound() {

        when(businessRepository.findById("B999"))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessNotFoundException.class,
                () -> businessService.getBusiness("B999")
        );
    }
    @Test
    void shouldCreateBusinessWithGeneratedIdAndPendingStatus() {

        BusinessRequest request = new BusinessRequest();

        request.setName("Sahyadri Restaurant");
        request.setCategory("Restaurant");
        request.setDescription("Vegetarian Indian food");
        request.setCity("Surrey");
        request.setProvince("BC");
        request.setPhone("604-555-2025");
        request.setEmail("hello@sahyadri.example");
        request.setWebsite("https://example.com");

        when(businessRepository.save(any(Business.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Business result =
                businessService.createBusiness(request);

        assertEquals(
                "Sahyadri Restaurant",
                result.getName()
        );

        assertEquals(
                "Restaurant",
                result.getCategory()
        );

        assertEquals(
                "Surrey",
                result.getCity()
        );

        assertEquals(
                "BC",
                result.getProvince()
        );

        assertEquals(
                BusinessStatus.PENDING,
                result.getStatus()
        );

        assertNotNull(result.getId());

        assertEquals(
                36,
                result.getId().length()
        );

        verify(businessRepository)
                .save(any(Business.class));
    }
    @Test
    void shouldUpdateBusiness() {

        // Existing business
        Business existingBusiness = createBusiness(
                BusinessStatus.APPROVED
        );

        when(businessRepository.findById("B100"))
                .thenReturn(Optional.of(existingBusiness));

        // Updated request
        BusinessRequest request = new BusinessRequest();

        request.setName("Updated Sahyadri Restaurant");
        request.setCategory("Restaurant");
        request.setDescription("Updated vegetarian Indian food");
        request.setCity("Burnaby");
        request.setProvince("BC");
        request.setPhone("604-999-2025");
        request.setEmail("updated@sahyadri.example");
        request.setWebsite("https://example.com/updated");

        // Repository returns the updated business
        when(businessRepository.save(any(Business.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Business result =
                businessService.updateBusiness(
                        "B100",
                        request
                );

        // Verify updated fields
        assertEquals(
                "B100",
                result.getId()
        );

        assertEquals(
                "Updated Sahyadri Restaurant",
                result.getName()
        );

        assertEquals(
                "Restaurant",
                result.getCategory()
        );

        assertEquals(
                "Updated vegetarian Indian food",
                result.getDescription()
        );

        assertEquals(
                "Burnaby",
                result.getCity()
        );

        assertEquals(
                "BC",
                result.getProvince()
        );

        assertEquals(
                "604-999-2025",
                result.getPhone()
        );

        assertEquals(
                "updated@sahyadri.example",
                result.getEmail()
        );

        assertEquals(
                "https://example.com/updated",
                result.getWebsite()
        );

        // Important business rule:
        // updateBusiness() must preserve the existing status
        assertEquals(
                BusinessStatus.APPROVED,
                result.getStatus()
        );

        // Verify repository save was called
        verify(businessRepository)
                .save(existingBusiness);
    }
    @Test
    void shouldDeleteBusiness() {

        Business business = createBusiness(
                BusinessStatus.APPROVED
        );

        when(businessRepository.findById("B100"))
                .thenReturn(Optional.of(business));

        businessService.deleteBusiness("B100");

        verify(businessRepository)
                .deleteById("B100");
    }
    @Test
    void shouldThrowExceptionWhenDeletingMissingBusiness() {

        when(businessRepository.findById("B999"))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessNotFoundException.class,
                () -> businessService.deleteBusiness("B999")
        );
    }
    @Test
    void shouldGetAllBusinesses() {

        Business business1 = createBusiness(
                BusinessStatus.APPROVED
        );

        Business business2 = new Business(
                "B200",
                "Marathi Academy",
                "Education",
                "Marathi language classes",
                "Langley",
                "BC",
                "604-555-3000",
                "academy@example.com",
                "https://example.com",
                BusinessStatus.PENDING
        );

        when(businessRepository.findAll())
                .thenReturn(java.util.List.of(
                        business1,
                        business2
                ));

        var result =
                businessService.getAllBusinesses();

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                "B100",
                result.get(0).getId()
        );

        assertEquals(
                "B200",
                result.get(1).getId()
        );

        verify(businessRepository)
                .findAll();
    }
    @Test
    void shouldSearchBusinessesByCategory() {

        Business business = createBusiness(
                BusinessStatus.APPROVED
        );

        when(businessRepository.findByCategory("Restaurant"))
                .thenReturn(java.util.List.of(business));

        var result =
                businessService.searchByCategory("Restaurant");

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "Restaurant",
                result.get(0).getCategory()
        );

        assertEquals(
                "B100",
                result.get(0).getId()
        );

        verify(businessRepository)
                .findByCategory("Restaurant");
    }
    @Test
    void shouldSearchBusinessesByCity() {

        Business business = createBusiness(
                BusinessStatus.APPROVED
        );

        when(businessRepository.findByCity("Surrey"))
                .thenReturn(java.util.List.of(business));

        var result =
                businessService.searchByCity("Surrey");

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "Surrey",
                result.get(0).getCity()
        );

        assertEquals(
                "B100",
                result.get(0).getId()
        );

        verify(businessRepository)
                .findByCity("Surrey");
    }
    @Test
    void shouldConvertBusinessToBusinessResponse() {

        Business business = createBusiness(
                BusinessStatus.APPROVED
        );

        BusinessResponse response =
                businessService.toResponse(business);

        assertNotNull(response);

        assertEquals(
                "B100",
                response.getId()
        );

        assertEquals(
                "Sahyadri Restaurant",
                response.getName()
        );

        assertEquals(
                "Restaurant",
                response.getCategory()
        );

        assertEquals(
                "Vegetarian Indian food",
                response.getDescription()
        );

        assertEquals(
                "Surrey",
                response.getCity()
        );

        assertEquals(
                "BC",
                response.getProvince()
        );

        assertEquals(
                "604-555-2025",
                response.getPhone()
        );

        assertEquals(
                "hello@sahyadri.example",
                response.getEmail()
        );

        assertEquals(
                "https://example.com",
                response.getWebsite()
        );

        assertEquals(
                BusinessStatus.APPROVED,
                response.getStatus()
        );
    }
}