package com.reliaquest.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class EmployeeResponse {
    @JsonProperty("data")
    private List<Employee> data;

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }
}