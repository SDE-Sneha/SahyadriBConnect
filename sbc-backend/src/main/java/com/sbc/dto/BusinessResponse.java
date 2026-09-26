package com.sbc.dto;

import com.sbc.model.BusinessStatus;

public class BusinessResponse {

    private String id;
    private String name;
    private String category;
    private String description;
    private String city;
    private String province;
    private String phone;
    private String email;
    private String website;
    private BusinessStatus status;

    public BusinessResponse() {
    }

    public BusinessResponse(
            String id,
            String name,
            String category,
            String description,
            String city,
            String province,
            String phone,
            String email,
            String website,
            BusinessStatus status) {

        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.city = city;
        this.province = province;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public BusinessStatus getStatus() {
        return status;
    }

    public void setStatus(BusinessStatus status) {
        this.status = status;
    }
}