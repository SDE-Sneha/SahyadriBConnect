package com.sbc.service;

import com.sbc.dto.BusinessRequest;
import com.sbc.dto.BusinessResponse;
import com.sbc.exception.BusinessNotFoundException;
import com.sbc.model.Business;
import com.sbc.model.BusinessStatus;
import com.sbc.repository.IBusinessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.sbc.exception.InvalidBusinessStatusTransitionException;
import java.util.List;
import java.util.UUID;

@Service
public class BusinessService {

    private static final Logger log = LoggerFactory.getLogger(BusinessService.class);

    private final IBusinessRepository businessRepository;

    public BusinessService(IBusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    // --------------------------------------------------
    // GET
    // --------------------------------------------------

    public Business getBusiness(String id) {
        log.debug("Looking up business id={}", id);
        return businessRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Business not found for id={}", id);
                    return new BusinessNotFoundException(id);
                });
    }

    public List<Business> getAllBusinesses() {
        log.debug("Fetching all businesses");
        return businessRepository.findAll();
    }

    public List<Business> searchByCategory(String category) {
        log.debug("Searching businesses by category={}", category);
        return businessRepository.findByCategory(category);
    }

    public List<Business> searchByCity(String city) {
        log.debug("Searching businesses by city={}", city);
        return businessRepository.findByCity(city);
    }
    public List<Business> searchBusinesses(
            String category,
            String city) {

        boolean hasCategory =
                category != null && !category.isBlank();

        boolean hasCity =
                city != null && !city.isBlank();

        if (!hasCategory && !hasCity) {
            return businessRepository.findAll();
        }

        if (hasCategory && !hasCity) {
            return businessRepository.findByCategory(category);
        }

        if (!hasCategory) {
            return businessRepository.findByCity(city);
        }

        return businessRepository.findAll()
                .stream()
                .filter(business ->
                        business.getCategory() != null
                                && business.getCategory()
                                .equalsIgnoreCase(category))
                .filter(business ->
                        business.getCity() != null
                                && business.getCity()
                                .equalsIgnoreCase(city))
                .toList();
    }
    // --------------------------------------------------
    // CREATE
    // --------------------------------------------------

    public Business createBusiness(BusinessRequest request) {

        log.info("Creating new business name={} category={} city={}", request.getName(), request.getCategory(), request.getCity());

        Business business = new Business();

        business.setId(UUID.randomUUID().toString());

        business.setName(request.getName());
        business.setCategory(request.getCategory());
        business.setDescription(request.getDescription());
        business.setCity(request.getCity());
        business.setProvince(request.getProvince());
        business.setPhone(request.getPhone());
        business.setEmail(request.getEmail());
        business.setWebsite(request.getWebsite());

        // New businesses always require admin approval.
        business.setStatus(BusinessStatus.PENDING);

        Business savedBusiness = businessRepository.save(business);
        log.info("Business created id={} status={}", savedBusiness.getId(), savedBusiness.getStatus());
        return savedBusiness;
    }

    // --------------------------------------------------
    // UPDATE
    // --------------------------------------------------

    public Business updateBusiness(
            String id,
            BusinessRequest request) {

        log.info("Updating business id={} with new values", id);

        Business business = businessRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update missing business id={}", id);
                    return new BusinessNotFoundException(id);
                });

        business.setName(request.getName());
        business.setCategory(request.getCategory());
        business.setDescription(request.getDescription());
        business.setCity(request.getCity());
        business.setProvince(request.getProvince());
        business.setPhone(request.getPhone());
        business.setEmail(request.getEmail());
        business.setWebsite(request.getWebsite());

        Business updatedBusiness = businessRepository.save(business);
        log.info("Business updated id={} status={}", updatedBusiness.getId(), updatedBusiness.getStatus());
        return updatedBusiness;
    }

    // --------------------------------------------------
    // DELETE
    // --------------------------------------------------

    public void deleteBusiness(String id) {

        log.warn("Attempting to delete business id={}", id);

        businessRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot delete missing business id={}", id);
                    return new BusinessNotFoundException(id);
                });

        businessRepository.deleteById(id);
        log.info("Business deleted id={}", id);
    }

    // --------------------------------------------------
    // ADMIN STATUS
    // --------------------------------------------------

    public Business updateBusinessStatus(
            String id,
            BusinessStatus newStatus) {

        log.info("Changing business id={} status from {} to {}", id, businessRepository.findById(id).map(Business::getStatus).orElse(null), newStatus);

        Business business = businessRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update status for missing business id={}", id);
                    return new BusinessNotFoundException(id);
                });

        BusinessStatus currentStatus = business.getStatus();

        validateStatusTransition(currentStatus, newStatus);

        business.setStatus(newStatus);

        Business updatedBusiness = businessRepository.save(business);
        log.info("Business status updated id={} from {} to {}", id, currentStatus, newStatus);
        return updatedBusiness;
    }

    // --------------------------------------------------
    // STATUS TRANSITION RULES
    // --------------------------------------------------

    private void validateStatusTransition(
            BusinessStatus currentStatus,
            BusinessStatus newStatus) {

        boolean validTransition =
                (currentStatus == BusinessStatus.PENDING
                        && (newStatus == BusinessStatus.APPROVED
                        || newStatus == BusinessStatus.REJECTED))

                        || (currentStatus == BusinessStatus.APPROVED
                        && newStatus == BusinessStatus.SUSPENDED)

                        || (currentStatus == BusinessStatus.SUSPENDED
                        && newStatus == BusinessStatus.APPROVED);

        if (!validTransition) {
            log.warn("Denied invalid status transition current={} new={}", currentStatus, newStatus);
            throw new InvalidBusinessStatusTransitionException(
                    "Invalid business status transition: "
                            + currentStatus
                            + " -> "
                            + newStatus
            );
        }
    }

    public BusinessResponse toResponse(Business business) {

        return new BusinessResponse(
                business.getId(),
                business.getName(),
                business.getCategory(),
                business.getDescription(),
                business.getCity(),
                business.getProvince(),
                business.getPhone(),
                business.getEmail(),
                business.getWebsite(),
                business.getStatus()
        );
    }

}