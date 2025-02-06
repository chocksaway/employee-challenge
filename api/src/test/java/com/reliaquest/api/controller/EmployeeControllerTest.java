package com.reliaquest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.entity.CreateMockEmployeeInput;
import com.reliaquest.api.entity.DeleteMockEmployeeInput;
import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateMockEmployeeInput createMockEmployeeInput;

    private Employee employee;

    @BeforeEach
    void setUp() {
        createMockEmployeeInput = new CreateMockEmployeeInput();
        createMockEmployeeInput.setName("John Doe");

        employee = new Employee();
        employee.setId(String.valueOf(UUID.randomUUID()));
        employee.setEmployeeName("John Doe");
    }

    @Test
    void deleteEmployeeById_ShouldReturnOk_WhenEmployeeExists() throws Exception {
        Mockito.when(employeeService.findEmployeeById(any(UUID.class))).thenReturn(Optional.of(employee));
        Mockito.when(employeeService.deleteEmployee(any(DeleteMockEmployeeInput.class))).thenReturn(Optional.of("Employee deleted successfully"));

        mockMvc.perform(delete("/")
                .param("id", employee.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteEmployeeById_ShouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
        Mockito.when(employeeService.findEmployeeById(any(UUID.class))).thenReturn(Optional.empty());

        mockMvc.perform(delete("/")
                .param("id", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployee_ShouldReturnOk_WhenEmployeeIsCreated() throws Exception {
        Mockito.when(employeeService.createEmployee(any(CreateMockEmployeeInput.class))).thenReturn(ResponseEntity.ok(employee));

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockEmployeeInput)))
                .andExpect(status().isOk());
    }

    @Test
    void createEmployee_ShouldReturnConflict_WhenEmployeeAlreadyExists() throws Exception {
        Mockito.when(employeeService.createEmployee(any(CreateMockEmployeeInput.class)))
                .thenReturn(ResponseEntity.status(409).build());

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockEmployeeInput)))
                .andExpect(status().isConflict());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_ShouldReturnOk_WithListOfNames() throws Exception {
        List<String> topTenNames = List.of("John Doe", "Jane Smith", "Alice Johnson", "Bob Brown", "Charlie Davis",
                "Eve White", "Frank Black", "Grace Green", "Hank Blue", "Ivy Red");

        Mockito.when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(ResponseEntity.ok(topTenNames));

        mockMvc.perform(get("/topTenHighestEarningEmployeeNames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"John Doe\",\"Jane Smith\",\"Alice Johnson\",\"Bob Brown\",\"Charlie Davis\",\"Eve White\",\"Frank Black\",\"Grace Green\",\"Hank Blue\",\"Ivy Red\"]"));
    }

    @Test
    void getHighestSalaryOfEmployees_ShouldReturnOk_WithHighestSalary() throws Exception {
        int highestSalary = 100000;

        Mockito.when(employeeService.getHighestSalaryOfEmployees()).thenReturn(ResponseEntity.ok(highestSalary));

        mockMvc.perform(get("/highestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(highestSalary)));
    }

    @Test
    void getEmployeeById_ShouldReturnOk_WhenEmployeeExists() throws Exception {
        Mockito.when(employeeService.getEmployeeById(any(UUID.class))).thenReturn(ResponseEntity.ok(employee));

        mockMvc.perform(get("/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employee)));
    }

    @Test
    void getEmployeeById_ShouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
        Mockito.when(employeeService.getEmployeeById(any(UUID.class))).thenReturn(ResponseEntity.status(404).build());

        mockMvc.perform(get("/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeesByNameSearch_ShouldReturnOk_WithListOfEmployees() throws Exception {
        List<Employee> employees = List.of(employee);

        Mockito.when(employeeService.getEmployeesByNameSearch(any(String.class))).thenReturn(ResponseEntity.ok(employees));

        mockMvc.perform(get("/search/John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employees)));
    }

    @Test
    void getEmployees_ShouldReturnOk_WithListOfEmployees() throws Exception {
        List<Employee> employees = List.of(employee);

        Mockito.when(employeeService.getEmployees()).thenReturn(ResponseEntity.ok(employees));

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employees)));
    }
}