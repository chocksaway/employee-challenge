package com.reliaquest.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleEmployeeResponse {
    @JsonProperty("data")
    private Employee data;

    public Employee getData() {
        return data;
    }

    public void setData(Employee data) {
        this.data = data;
    }
}