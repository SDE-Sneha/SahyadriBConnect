package com.sbc.api;

import com.sbc.dto.BusinessRequest;
import com.sbc.model.Business;
import com.sbc.model.BusinessStatus;
import com.sbc.service.BusinessService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class BusinessControllerTest {

    private BusinessService businessService;
    private BusinessController businessController;

    @BeforeEach
    void setUp() {
        businessService = mock(BusinessService.class);
        businessController = new BusinessController(businessService);
    }

    @Test
    void shouldGetAllBusinesses() {
        Business business = new Business(
                "B001",
                "Sahyadri Sweets",
                "Food",
                "Traditional Maharashtrian sweets",
                "Surrey",
                "BC",
                "604-555-1001",
                "info@sahyadrisweets.example",
                "https://example.com",
                BusinessStatus.APPROVED
        );

        when(businessService.getAllBusinesses())
                .thenReturn(List.of(business));

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.getBusinesses(null, null);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("B001", response.get(0).getId());
        assertEquals(
                "Sahyadri Sweets",
                response.get(0).getName()
        );
    }

    @Test
    void shouldGetBusinessById() {

        Business business = new Business(
                "B001",
                "Sahyadri Sweets",
                "Food",
                "Traditional Maharashtrian sweets",
                "Surrey",
                "BC",
                "604-555-1001",
                "info@sahyadrisweets.example",
                "https://example.com",
                BusinessStatus.APPROVED
        );

        when(businessService.getBusiness("B001"))
                .thenReturn(business);

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.getBusiness("B001");

        assertNotNull(response);
        assertEquals("B001", response.getId());
        assertEquals(
                "Sahyadri Sweets",
                response.getName()
        );
    }

    @Test
    void shouldSearchBusinessesByCategory() {

        Business business = new Business(
                "B001",
                "Sahyadri Sweets",
                "Food",
                "Traditional Maharashtrian sweets",
                "Surrey",
                "BC",
                "604-555-1001",
                "info@sahyadrisweets.example",
                "https://example.com",
                BusinessStatus.APPROVED
        );

        when(businessService.searchByCategory("Food"))
                .thenReturn(List.of(business));

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.getBusinesses("Food", null);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Food", response.get(0).getCategory());
    }
    @Test
    void shouldSearchBusinessesByCity() {

        Business business = new Business(
                "B001",
                "Sahyadri Sweets",
                "Food",
                "Traditional Maharashtrian sweets",
                "Surrey",
                "BC",
                "604-555-1001",
                "info@sahyadrisweets.example",
                "https://example.com",
                BusinessStatus.APPROVED
        );

        when(businessService.searchByCity("Surrey"))
                .thenReturn(List.of(business));

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.getBusinesses(null, "Surrey");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Surrey", response.get(0).getCity());
    }

    @Test
    void shouldCreateBusiness() {

        BusinessRequest request = new BusinessRequest();

        request.setName("Sahyadri Restaurant");
        request.setCategory("Restaurant");
        request.setDescription("Vegetarian Indian food");
        request.setCity("Surrey");
        request.setProvince("BC");
        request.setPhone("604-555-2025");
        request.setEmail("hello@sahyadri.example");
        request.setWebsite("https://example.com");

        Business business = new Business(
                "B100",
                "Sahyadri Restaurant",
                "Restaurant",
                "Vegetarian Indian food",
                "Surrey",
                "BC",
                "604-555-2025",
                "hello@sahyadri.example",
                "https://example.com",
                BusinessStatus.PENDING
        );

        when(businessService.createBusiness(request))
                .thenReturn(business);

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.createBusiness(request);

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
                BusinessStatus.PENDING,
                response.getStatus()
        );
    }

    @Test
    void shouldUpdateBusiness() {

        BusinessRequest request = new BusinessRequest();

        request.setName("Updated Sahyadri Restaurant");
        request.setCategory("Restaurant");
        request.setDescription("Updated vegetarian Indian food");
        request.setCity("Surrey");
        request.setProvince("BC");
        request.setPhone("604-999-2025");
        request.setEmail("updated@sahyadri.example");
        request.setWebsite("https://example.com/updated");

        Business business = new Business(
                "B100",
                "Updated Sahyadri Restaurant",
                "Restaurant",
                "Updated vegetarian Indian food",
                "Surrey",
                "BC",
                "604-999-2025",
                "updated@sahyadri.example",
                "https://example.com/updated",
                BusinessStatus.PENDING
        );

        when(businessService.updateBusiness("B100", request))
                .thenReturn(business);

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.updateBusiness(
                        "B100",
                        request
                );

        assertNotNull(response);

        assertEquals(
                "B100",
                response.getId()
        );

        assertEquals(
                "Updated Sahyadri Restaurant",
                response.getName()
        );

        assertEquals(
                "Surrey",
                response.getCity()
        );

        assertEquals(
                BusinessStatus.PENDING,
                response.getStatus()
        );
    }

    @Test
    void shouldDeleteBusiness() {

        businessController.deleteBusiness("B100");

        verify(businessService)
                .deleteBusiness("B100");
    }

    @Test
    void shouldApprovePendingBusiness() {

        Business business = new Business(
                "B100",
                "Sahyadri Restaurant",
                "Restaurant",
                "Vegetarian Indian food",
                "Surrey",
                "BC",
                "604-555-2025",
                "hello@sahyadri.example",
                "https://example.com",
                BusinessStatus.APPROVED
        );

        when(businessService.updateBusinessStatus(
                "B100",
                BusinessStatus.APPROVED
        )).thenReturn(business);

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.approveBusiness("B100");

        assertNotNull(response);

        assertEquals(
                "B100",
                response.getId()
        );

        assertEquals(
                BusinessStatus.APPROVED,
                response.getStatus()
        );

        verify(businessService)
                .updateBusinessStatus(
                        "B100",
                        BusinessStatus.APPROVED
                );
    }
    @Test
    void shouldRejectPendingBusiness() {

        Business business = new Business(
                "B100",
                "Sahyadri Restaurant",
                "Restaurant",
                "Vegetarian Indian food",
                "Surrey",
                "BC",
                "604-555-2025",
                "hello@sahyadri.example",
                "https://example.com",
                BusinessStatus.REJECTED
        );

        when(businessService.updateBusinessStatus(
                "B100",
                BusinessStatus.REJECTED
        )).thenReturn(business);

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.rejectBusiness("B100");

        assertNotNull(response);

        assertEquals(
                "B100",
                response.getId()
        );

        assertEquals(
                BusinessStatus.REJECTED,
                response.getStatus()
        );

        verify(businessService)
                .updateBusinessStatus(
                        "B100",
                        BusinessStatus.REJECTED
                );
    }
    @Test
    void shouldSuspendApprovedBusiness() {

        Business business = new Business(
                "B100",
                "Sahyadri Restaurant",
                "Restaurant",
                "Vegetarian Indian food",
                "Surrey",
                "BC",
                "604-555-2025",
                "hello@sahyadri.example",
                "https://example.com",
                BusinessStatus.SUSPENDED
        );

        when(businessService.updateBusinessStatus(
                "B100",
                BusinessStatus.SUSPENDED
        )).thenReturn(business);

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.suspendBusiness("B100");

        assertNotNull(response);

        assertEquals(
                "B100",
                response.getId()
        );

        assertEquals(
                BusinessStatus.SUSPENDED,
                response.getStatus()
        );

        verify(businessService)
                .updateBusinessStatus(
                        "B100",
                        BusinessStatus.SUSPENDED
                );
    }
    @Test
    void shouldApproveSuspendedBusiness() {

        Business business = new Business(
                "B100",
                "Sahyadri Restaurant",
                "Restaurant",
                "Vegetarian Indian food",
                "Surrey",
                "BC",
                "604-555-2025",
                "hello@sahyadri.example",
                "https://example.com",
                BusinessStatus.APPROVED
        );

        when(businessService.updateBusinessStatus(
                "B100",
                BusinessStatus.APPROVED
        )).thenReturn(business);

        when(businessService.toResponse(business))
                .thenCallRealMethod();

        var response =
                businessController.approveBusiness("B100");

        assertNotNull(response);

        assertEquals(
                "B100",
                response.getId()
        );

        assertEquals(
                BusinessStatus.APPROVED,
                response.getStatus()
        );

        verify(businessService)
                .updateBusinessStatus(
                        "B100",
                        BusinessStatus.APPROVED
                );
    }
}