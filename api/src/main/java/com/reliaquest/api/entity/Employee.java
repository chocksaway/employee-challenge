package com.reliaquest.api.entity;

import com.google.gson.annotations.SerializedName;

public class Employee {
    @SerializedName("id")
    private String id;

    @SerializedName("employee_name")
    private String employeeName;

    @SerializedName("employee_salary")
    private String employeeSalary;

    @SerializedName("employee_age")
    private int employeeAge;

    @SerializedName("employee_title")
    private String employeeTitle;

    @SerializedName("employee_email")
    private String employeeEmail;


    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("name")
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(String employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }

    public String getEmployeeTitle() {
        return employeeTitle;
    }

    public void setEmployeeTitle(String employeeTitle) {
        this.employeeTitle = employeeTitle;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }
}