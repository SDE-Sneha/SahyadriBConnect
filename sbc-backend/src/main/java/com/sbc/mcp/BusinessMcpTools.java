package com.sbc.mcp;

import com.sbc.model.Business;
import com.sbc.service.BusinessService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusinessMcpTools {

    private final BusinessService businessService;

    public BusinessMcpTools(BusinessService businessService) {
        this.businessService = businessService;
    }

    @McpTool(
            name = "get_business",
            description = "Get a business by its unique business ID"
    )
    public Business getBusiness(String id) {

        return businessService.getBusiness(id);
    }

    @McpTool(
            name = "search_businesses",
            description = "Search businesses using an optional category and city. " +
                    "If both are provided, businesses matching both filters are returned. " +
                    "If neither is provided, all businesses are returned."
    )
    public List<Business> searchBusinesses(
            String category,
            String city) {

        return businessService.searchBusinesses(
                category,
                city
        );
    }

    @McpTool(
            name = "get_businesses_by_category",
            description = "Find businesses belonging to a specific category"
    )
    public List<Business> getBusinessesByCategory(
            String category) {

        return businessService.searchByCategory(category);
    }

    @McpTool(
            name = "get_businesses_by_city",
            description = "Find businesses located in a specific city"
    )
    public List<Business> getBusinessesByCity(
            String city) {

        return businessService.searchByCity(city);
    }
}