package com.sbc.repository;

import com.sbc.model.Business;
import com.sbc.model.BusinessStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryBusinessRepositoryTest {

    private InMemoryBusinessRepository businessRepository;

    @BeforeEach
    void setUp() {
        businessRepository = new InMemoryBusinessRepository();
    }

    @Test
    void shouldFindBusinessById() {

        Optional<Business> result =
                businessRepository.findById("B001");

        assertTrue(result.isPresent());

        Business business = result.get();

        assertEquals("B001", business.getId());
        assertEquals("Sahyadri Sweets", business.getName());
        assertEquals("Food", business.getCategory());
        assertEquals("Surrey", business.getCity());
        assertEquals(BusinessStatus.APPROVED, business.getStatus());
    }

    @Test
    void shouldReturnAllBusinesses() {

        List<Business> businesses =
                businessRepository.findAll();

        assertEquals(5, businesses.size());
    }

    @Test
    void shouldFindBusinessesByCategory() {

        List<Business> businesses =
                businessRepository.findByCategory("Food");

        assertEquals(2, businesses.size());

        assertTrue(
                businesses.stream()
                        .allMatch(business ->
                                business.getCategory()
                                        .equalsIgnoreCase("Food"))
        );
    }

    @Test
    void shouldFindBusinessesByCity() {

        List<Business> businesses =
                businessRepository.findByCity("Surrey");

        assertEquals(2, businesses.size());

        assertTrue(
                businesses.stream()
                        .allMatch(business ->
                                business.getCity()
                                        .equalsIgnoreCase("Surrey"))
        );
    }

    @Test
    void shouldSaveBusiness() {

        Business business = new Business(
                "B100",
                "Test Business",
                "Technology",
                "Test description",
                "Langley",
                "BC",
                "604-555-9999",
                "test@example.com",
                "https://example.com",
                BusinessStatus.PENDING
        );

        Business result =
                businessRepository.save(business);

        assertEquals(business, result);

        Optional<Business> savedBusiness =
                businessRepository.findById("B100");

        assertTrue(savedBusiness.isPresent());
        assertEquals(
                "Test Business",
                savedBusiness.get().getName()
        );
    }

    @Test
    void shouldDeleteBusinessById() {

        assertTrue(
                businessRepository.findById("B001").isPresent()
        );

        businessRepository.deleteById("B001");

        assertFalse(
                businessRepository.findById("B001").isPresent()
        );
    }

    @Test
    void shouldResetRepository() {

        businessRepository.deleteById("B001");
        businessRepository.deleteById("B002");

        assertEquals(
                3,
                businessRepository.findAll().size()
        );

        businessRepository.reset();

        assertEquals(
                5,
                businessRepository.findAll().size()
        );

        assertTrue(
                businessRepository.findById("B001").isPresent()
        );

        assertEquals(
                "Sahyadri Sweets",
                businessRepository
                        .findById("B001")
                        .get()
                        .getName()
        );
    }
}
