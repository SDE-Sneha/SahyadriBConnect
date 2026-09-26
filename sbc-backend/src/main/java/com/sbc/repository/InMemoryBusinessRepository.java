package com.sbc.repository;

import com.sbc.model.Business;
import com.sbc.model.BusinessStatus;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBusinessRepository implements IBusinessRepository {

    private final Map<String, Business> businesses = new ConcurrentHashMap<>();

    public InMemoryBusinessRepository() {
        loadSampleData();
    }

    @Override
    public Optional<Business> findById(String id) {
        return Optional.ofNullable(businesses.get(id));
    }

    @Override
    public List<Business> findAll() {
        return new ArrayList<>(businesses.values());
    }

    @Override
    public List<Business> findByCategory(String category) {

        return businesses.values()
                .stream()
                .filter(business ->
                        business.getCategory()
                                .equalsIgnoreCase(category))
                .toList();
    }

    @Override
    public List<Business> findByCity(String city) {

        return businesses.values()
                .stream()
                .filter(business ->
                        business.getCity()
                                .equalsIgnoreCase(city))
                .toList();
    }

    @Override
    public Business save(Business business) {

        businesses.put(business.getId(), business);

        return business;
    }

    @Override
    public void deleteById(String id) {

        businesses.remove(id);
    }

    private void loadSampleData() {

        save(new Business(
                "B001",
                "Sahyadri Sweets",
                "Food",
                "Traditional Maharashtrian sweets and snacks",
                "Surrey",
                "BC",
                "604-555-1001",
                "info@sahyadrisweets.example",
                "https://example.com/sahyadri-sweets",
                BusinessStatus.APPROVED
        ));

        save(new Business(
                "B002",
                "Marathi Cultural Academy",
                "Education",
                "Marathi language and cultural education programs",
                "Langley",
                "BC",
                "604-555-1002",
                "info@marathiacademy.example",
                "https://example.com/marathi-academy",
                BusinessStatus.APPROVED
        ));

        save(new Business(
                "B003",
                "Desi Tech Solutions",
                "Technology",
                "Software development and IT consulting services",
                "Vancouver",
                "BC",
                "604-555-1003",
                "contact@desitech.example",
                "https://example.com/desi-tech",
                BusinessStatus.PENDING
        ));

        save(new Business(
                "B004",
                "Maharashtra Catering",
                "Food",
                "Vegetarian Maharashtrian catering for events",
                "Burnaby",
                "BC",
                "604-555-1004",
                "hello@mahacatering.example",
                "https://example.com/maha-catering",
                BusinessStatus.APPROVED
        ));

        save(new Business(
                "B005",
                "Sahyadri Accounting Services",
                "Professional Services",
                "Accounting and bookkeeping services for small businesses",
                "Surrey",
                "BC",
                "604-555-1005",
                "info@sahyadriaccounting.example",
                "https://example.com/sahyadri-accounting",
                BusinessStatus.PENDING
        ));
    }
    public void reset() {
        businesses.clear();
        loadSampleData();
    }
}