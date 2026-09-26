package com.sbc.repository;

import com.sbc.model.Business;

import java.util.List;
import java.util.Optional;

public interface IBusinessRepository {

    Optional<Business> findById(String id);

    List<Business> findAll();

    List<Business> findByCategory(String category);

    List<Business> findByCity(String city);

    Business save(Business business);

    void deleteById(String id);
}