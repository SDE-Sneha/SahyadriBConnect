package com.sbc.api;

import com.sbc.dto.BusinessRequest;
import com.sbc.dto.BusinessResponse;
import com.sbc.model.Business;
import com.sbc.model.BusinessStatus;
import com.sbc.service.BusinessService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private static final Logger log = LoggerFactory.getLogger(BusinessController.class);

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    // GET - All / Search
    @GetMapping
    public List<BusinessResponse> getBusinesses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city) {

        log.info("Fetching businesses with category={} and city={}", category, city);

        List<Business> businesses;

        if (category != null) {
            businesses = businessService.searchByCategory(category);

        } else if (city != null) {
            businesses = businessService.searchByCity(city);

        } else {
            businesses = businessService.getAllBusinesses();
        }

        List<BusinessResponse> response = businesses.stream()
                .map(businessService::toResponse)
                .toList();

        log.debug("Found {} businesses for filters category={} city={}", response.size(), category, city);
        return response;
    }

    // GET - One Business
    @GetMapping("/{id}")
    public BusinessResponse getBusiness(
            @PathVariable String id) {

        log.info("Fetching business by id={}", id);
        Business business = businessService.getBusiness(id);
        BusinessResponse response = businessService.toResponse(business);
        log.debug("Business retrieved id={}", id);
        return response;
    }

    // POST - Create
    @PostMapping
    public BusinessResponse createBusiness(
            @Valid @RequestBody BusinessRequest request) {

        log.info("Creating business with name={} city={} category={}", request.getName(), request.getCity(), request.getCategory());
        Business business = businessService.createBusiness(request);
        BusinessResponse response = businessService.toResponse(business);
        log.info("Business created successfully id={} status={}", response.getId(), response.getStatus());
        return response;
    }

    // PUT - Update
    @PutMapping("/{id}")
    public BusinessResponse updateBusiness(
            @PathVariable String id,
            @Valid @RequestBody BusinessRequest request) {

        log.info("Updating business id={} with name={} city={} category={}", id, request.getName(), request.getCity(), request.getCategory());
        Business business = businessService.updateBusiness(id, request);
        BusinessResponse response = businessService.toResponse(business);
        log.info("Business updated successfully id={} status={}", response.getId(), response.getStatus());
        return response;
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBusiness(
            @PathVariable String id) {

        log.warn("Deleting business id={}", id);
        businessService.deleteBusiness(id);
        log.info("Business deleted successfully id={}", id);
    }

    // ADMIN - Approve
    @PutMapping("/{id}/approve")
    public BusinessResponse approveBusiness(
            @PathVariable String id) {

        log.info("Approving business id={}", id);
        Business business = businessService.updateBusinessStatus(id, BusinessStatus.APPROVED);
        BusinessResponse response = businessService.toResponse(business);
        log.info("Business approved id={} status={}", response.getId(), response.getStatus());
        return response;
    }

    // ADMIN - Reject
    @PutMapping("/{id}/reject")
    public BusinessResponse rejectBusiness(
            @PathVariable String id) {

        log.info("Rejecting business id={}", id);
        Business business = businessService.updateBusinessStatus(id, BusinessStatus.REJECTED);
        BusinessResponse response = businessService.toResponse(business);
        log.info("Business rejected id={} status={}", response.getId(), response.getStatus());
        return response;
    }

    // ADMIN - Suspend
    @PutMapping("/{id}/suspend")
    public BusinessResponse suspendBusiness(
            @PathVariable String id) {

        log.info("Suspending business id={}", id);
        Business business = businessService.updateBusinessStatus(id, BusinessStatus.SUSPENDED);
        BusinessResponse response = businessService.toResponse(business);
        log.info("Business suspended id={} status={}", response.getId(), response.getStatus());
        return response;
    }
}