package com.reliaquest.api.entity;

import jakarta.validation.constraints.NotBlank;

public class DeleteMockEmployeeInput {

    @NotBlank
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
