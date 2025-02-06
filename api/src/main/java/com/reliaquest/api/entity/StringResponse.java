package com.reliaquest.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StringResponse {
    @JsonProperty("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}