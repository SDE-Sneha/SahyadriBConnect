package com.sbc.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;
import com.sbc.repository.InMemoryBusinessRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@SpringBootTest
class BusinessControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private InMemoryBusinessRepository businessRepository;

    @BeforeEach
    void setUp() {
        businessRepository.reset();
        mockMvc = webAppContextSetup(
                webApplicationContext
        ).build();
    }

    @Test
    void shouldGetAllBusinesses() throws Exception {
        mockMvc.perform(
                get("/api/businesses")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void shouldGetBusinessById() throws Exception {

        mockMvc.perform(
                        get("/api/businesses/B001")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("B001"))
                .andExpect(jsonPath("$.name").value("Sahyadri Sweets"))
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.city").value("Surrey"))
                .andExpect(jsonPath("$.province").value("BC"))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldReturn404WhenBusinessDoesNotExist() throws Exception {

        mockMvc.perform(
                        get("/api/businesses/B999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value("Business not found: B999")
                );
    }


    @Test
    void shouldCreateBusiness() throws Exception {

        String requestJson = """
            {
                "name": "Sahyadri Indian Restaurant",
                "category": "Restaurant",
                "description": "Vegetarian Indian food",
                "city": "Surrey",
                "province": "BC",
                "phone": "604-555-2025",
                "email": "hello@sahyadri.example",
                "website": "https://example.com"
            }
            """;

        mockMvc.perform(
                                post("/api/businesses")
                                .contentType(
                                        org.springframework.http.MediaType.APPLICATION_JSON
                                )
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name")
                        .value("Sahyadri Indian Restaurant"))
                .andExpect(jsonPath("$.category").value("Restaurant"))
                .andExpect(jsonPath("$.city").value("Surrey"))
                .andExpect(jsonPath("$.province").value("BC"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }


    @Test
    void shouldReturn400WhenBusinessNameIsMissing() throws Exception {

        String requestJson = """
            {
                "name": "",
                "category": "Restaurant",
                "description": "Vegetarian Indian food",
                "city": "Surrey",
                "province": "BC",
                "email": "hello@sahyadri.example"
            }
            """;

        mockMvc.perform(
                        post("/api/businesses")
                                .contentType(
                                        org.springframework.http.MediaType.APPLICATION_JSON
                                )
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(
                        jsonPath("$.errors.name")
                                .value("Business name is required")
                );
    }

    @Test
    void shouldUpdateBusiness() throws Exception {

        String requestJson = """
            {
                "name": "Updated Sahyadri Sweets",
                "category": "Food",
                "description": "Updated Maharashtrian sweets",
                "city": "Burnaby",
                "province": "BC",
                "phone": "604-999-2025",
                "email": "updated@sahyadri.example",
                "website": "https://example.com/updated"
            }
            """;

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/businesses/B001")
                                .contentType(
                                        org.springframework.http.MediaType.APPLICATION_JSON
                                )
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("B001"))
                .andExpect(jsonPath("$.name")
                        .value("Updated Sahyadri Sweets"))
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.description")
                        .value("Updated Maharashtrian sweets"))
                .andExpect(jsonPath("$.city").value("Burnaby"))
                .andExpect(jsonPath("$.province").value("BC"))
                .andExpect(jsonPath("$.phone").value("604-999-2025"))
                .andExpect(jsonPath("$.email")
                        .value("updated@sahyadri.example"))
                .andExpect(jsonPath("$.website")
                        .value("https://example.com/updated"))

                // Important: updating details must preserve status
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldReturn404WhenUpdatingBusinessDoesNotExist()
            throws Exception {

        String requestJson = """
            {
                "name": "Nonexistent Business",
                "category": "Food",
                "city": "Surrey",
                "province": "BC"
            }
            """;

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/businesses/B999")
                                .contentType(
                                        org.springframework.http.MediaType.APPLICATION_JSON
                                )
                                .content(requestJson)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value("Business not found: B999")
                );
    }

    @Test
    void shouldDeleteBusiness() throws Exception {

        mockMvc.perform(
                        delete("/api/businesses/B001")
                )
                .andExpect(status().isNoContent());

        mockMvc.perform(
                        get("/api/businesses/B001")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value("Business not found: B001")
                );
    }


    @Test
    void shouldReturn404WhenDeletingBusinessDoesNotExist()
            throws Exception {

        mockMvc.perform(
                        delete("/api/businesses/B999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Business Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value("Business not found: B999")
                );
    }
    @Test
    void shouldApprovePendingBusiness() throws Exception {

        mockMvc.perform(
                        put("/api/businesses/B003/approve")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("B003"))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
    @Test
    void shouldRejectPendingBusiness() throws Exception {

        mockMvc.perform(
                        put("/api/businesses/B003/reject")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("B003"))
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }
    @Test
    void shouldSuspendApprovedBusiness() throws Exception {

        mockMvc.perform(
                        put("/api/businesses/B001/suspend")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("B001"))
                .andExpect(jsonPath("$.status").value("SUSPENDED"));
    }
    @Test
    void shouldApproveSuspendedBusiness() throws Exception {

        mockMvc.perform(
                        put("/api/businesses/B001/suspend")
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        put("/api/businesses/B001/approve")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("B001"))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
    @Test
    void shouldReturn400ForInvalidPendingToSuspendedTransition()
            throws Exception {

        mockMvc.perform(
                        put("/api/businesses/B003/suspend")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.status").value(400)
                )
                .andExpect(
                        jsonPath("$.error")
                                .value("Invalid Business Status Transition")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Invalid business status transition: "
                                                + "PENDING -> SUSPENDED"
                                )
                );
    }
    @Test
    void shouldReturn400ForInvalidApprovedToRejectedTransition()
            throws Exception {

        mockMvc.perform(
                        put("/api/businesses/B001/reject")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.status").value(400)
                )
                .andExpect(
                        jsonPath("$.error")
                                .value("Invalid Business Status Transition")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Invalid business status transition: "
                                                + "APPROVED -> REJECTED"
                                )
                );
    }

    @Test
    void shouldReturn404WhenApprovingBusinessDoesNotExist()
            throws Exception {

        mockMvc.perform(
                        put("/api/businesses/B999/approve")
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status").value(404)
                )
                .andExpect(
                        jsonPath("$.error")
                                .value("Business Not Found")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Business not found: B999")
                );
    }
    @Test
    void shouldReturn400WhenCategoryIsMissing() throws Exception {

        String requestJson = """
            {
                "name": "Sahyadri Restaurant",
                "category": "",
                "description": "Vegetarian Indian food",
                "city": "Surrey",
                "province": "BC",
                "email": "hello@sahyadri.example"
            }
            """;

        mockMvc.perform(
                        post("/api/businesses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.errors.category")
                        .value("Category is required"));
    }
    @Test
    void shouldReturn400WhenCityIsMissing() throws Exception {

        String requestJson = """
            {
                "name": "Sahyadri Restaurant",
                "category": "Restaurant",
                "description": "Vegetarian Indian food",
                "city": "",
                "province": "BC",
                "email": "hello@sahyadri.example"
            }
            """;

        mockMvc.perform(
                        post("/api/businesses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.city")
                        .value("City is required"));
    }
    @Test
    void shouldReturn400WhenProvinceIsMissing() throws Exception {

        String requestJson = """
            {
                "name": "Sahyadri Restaurant",
                "category": "Restaurant",
                "description": "Vegetarian Indian food",
                "city": "Surrey",
                "province": "",
                "email": "hello@sahyadri.example"
            }
            """;

        mockMvc.perform(
                        post("/api/businesses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.province")
                        .value("Province is required"));
    }
    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {

        String requestJson = """
            {
                "name": "Sahyadri Restaurant",
                "category": "Restaurant",
                "description": "Vegetarian Indian food",
                "city": "Surrey",
                "province": "BC",
                "email": "invalid-email"
            }
            """;

        mockMvc.perform(
                        post("/api/businesses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.errors.email")
                        .value("Email must be valid"));
    }

    @Test
    void shouldReturn400ForInvalidRejectedToApprovedTransition()
            throws Exception {

        // B003 starts as PENDING.
        // First reject it.
        mockMvc.perform(
                        put("/api/businesses/B003/reject")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));

        // Now try to approve the REJECTED business.
        // This transition should NOT be allowed.
        mockMvc.perform(
                        put("/api/businesses/B003/approve")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.status").value(400)
                )
                .andExpect(
                        jsonPath("$.error")
                                .value("Invalid Business Status Transition")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Invalid business status transition: "
                                                + "REJECTED -> APPROVED"
                                )
                );
    }
    @Test
    void shouldReturn400WhenUpdatingBusinessNameIsMissing()
            throws Exception {

        String requestJson = """
        {
            "name": "",
            "category": "Food",
            "description": "Updated description",
            "city": "Surrey",
            "province": "BC",
            "email": "updated@sahyadri.example"
        }
        """;

        mockMvc.perform(
                        put("/api/businesses/B001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.errors.name")
                        .value("Business name is required"));
    }
    @Test
    void shouldReturn400WhenUpdatingCategoryIsMissing()
            throws Exception {

        String requestJson = """
        {
            "name": "Updated Sahyadri Sweets",
            "category": "",
            "description": "Updated description",
            "city": "Surrey",
            "province": "BC",
            "email": "updated@sahyadri.example"
        }
        """;

        mockMvc.perform(
                        put("/api/businesses/B001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.category")
                        .value("Category is required"));
    }
    @Test
    void shouldReturn400WhenUpdatingCityIsMissing()
            throws Exception {

        String requestJson = """
        {
            "name": "Updated Sahyadri Sweets",
            "category": "Food",
            "description": "Updated description",
            "city": "",
            "province": "BC",
            "email": "updated@sahyadri.example"
        }
        """;

        mockMvc.perform(
                        put("/api/businesses/B001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.city")
                        .value("City is required"));
    }
    @Test
    void shouldReturn400WhenUpdatingProvinceIsMissing()
            throws Exception {

        String requestJson = """
        {
            "name": "Updated Sahyadri Sweets",
            "category": "Food",
            "description": "Updated description",
            "city": "Surrey",
            "province": "",
            "email": "updated@sahyadri.example"
        }
        """;

        mockMvc.perform(
                        put("/api/businesses/B001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.province")
                        .value("Province is required"));
    }
    @Test
    void shouldReturn400WhenUpdatingEmailIsInvalid()
            throws Exception {

        String requestJson = """
        {
            "name": "Updated Sahyadri Sweets",
            "category": "Food",
            "description": "Updated description",
            "city": "Surrey",
            "province": "BC",
            "email": "invalid-email"
        }
        """;

        mockMvc.perform(
                        put("/api/businesses/B001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.errors.email")
                        .value("Email must be valid"));
    }
}
