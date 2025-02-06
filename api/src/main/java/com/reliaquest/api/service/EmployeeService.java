package com.reliaquest.api.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.reliaquest.api.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${endpoint.url}")
    private String endpointUrl;

    public EmployeeService(RestTemplate restTemplate, Gson gson) {
        this.restTemplate = restTemplate;
        this.gson = gson;
    }

    public ResponseEntity<List<Employee>> getEmployees() {
        final List<Employee> allEmployees = getApiData();
        return ResponseEntity.ok(allEmployees);
    }

    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = getApiData().stream()
                .filter(each -> each.getEmployeeName().contains(searchString)).toList();

        return ResponseEntity.ok(employees);
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        List<Employee> employees = getApiData();
        OptionalInt highestSalary = employees.stream()
                .map(Employee::getEmployeeSalary)
                .mapToInt(Integer::parseInt)
                .max();

        return highestSalary.isPresent()
                ? ResponseEntity.ok(highestSalary.getAsInt())
                : ResponseEntity.status(404).build();
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = getApiData();
        List<String> topTenNames = employees.stream()
                .sorted((e1, e2) -> Integer.compare(Integer.parseInt(e2.getEmployeeSalary()), Integer.parseInt(e1.getEmployeeSalary())))
                .limit(10)
                .map(Employee::getEmployeeName)
                .toList();

        return ResponseEntity.ok(topTenNames);
    }

    public ResponseEntity<Employee> getEmployeeById(UUID id) {
        Optional<Employee> employee = getEmployee(id);

        return employee.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    public ResponseEntity<Employee> createEmployee(CreateMockEmployeeInput employeeInput) {
        final Optional<Employee> foundEmployee = getApiData().stream()
                .filter(each -> each.getEmployeeName().equals(employeeInput.getName()))
                .findFirst();

        if (foundEmployee.isPresent()) {
            return ResponseEntity.status(409).build();
        }

        Optional<Employee> employee = createEmployeeByCallingEndpoint(employeeInput);

        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(409).build());
    }

    public Optional<String> deleteEmployee(DeleteMockEmployeeInput employeeInput) {
        String url = UriComponentsBuilder.fromHttpUrl(endpointUrl).toUriString();

        HttpEntity<DeleteMockEmployeeInput> requestEntity = new HttpEntity<>(employeeInput);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity,
                new ParameterizedTypeReference<>() {
                });

        if (response.getBody() == null) {
            logger.error("Response body is null");
            return Optional.empty();
        }

        try {
            JsonObject root = JsonParser.parseString(response.getBody()).getAsJsonObject();
            logger.info("Parsed JSON: {}", root);

            StringResponse stringResponse = gson.fromJson(root, StringResponse.class);

            String message = stringResponse.getStatus();

            return Optional.of(message);
        } catch (Exception e) {
            logger.error("Error parsing JSON response", e);
            return Optional.empty();
        }
    }


    private Optional<Employee> createEmployeeByCallingEndpoint(final CreateMockEmployeeInput employeeInput) {
        String url = UriComponentsBuilder.fromHttpUrl(endpointUrl).toUriString();

        // Make the POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, employeeInput, String.class);

        return parseStringResponseToEmployee(response);
    }

    public Optional<Employee> findEmployeeById(UUID id) {
        return getEmployee(id);
    }

    private Optional<Employee> getEmployee(final UUID id) {

        String url = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/{id}").buildAndExpand(id).toUriString();
        ResponseEntity<String> response;

        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e) {
            logger.info("Employee {} not found", id);
            return Optional.empty();
        }

        return parseStringResponseToEmployee(response);
    }

    private Optional<Employee> parseStringResponseToEmployee(final ResponseEntity<String> response) {
        logger.info("Employee Response Status: {}", response.getStatusCode());
        logger.info("Employee Raw Response Body: {}", response.getBody());

        if (response.getBody() == null) {
            logger.error("Employee Response body is null");
            return Optional.empty();
        }

        try {
            JsonObject root = JsonParser.parseString(response.getBody()).getAsJsonObject();

            SingleEmployeeResponse employeeResponse = gson.fromJson(root, SingleEmployeeResponse.class);

            Employee singleEmployee = employeeResponse.getData();
            return Optional.of(singleEmployee);
        } catch (Exception e) {
            logger.error("Error parsing JSON response", e);
            return Optional.empty();
        }
    }

    private List<Employee> getApiData() {
        String url = UriComponentsBuilder.fromHttpUrl(endpointUrl).toUriString();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        logger.info("Get Employees Response Status: {}", response.getStatusCode());
        logger.info("Get Employees Raw Response Body: {}", response.getBody());

        if (response.getBody() == null) {
            logger.error("Get Employees Response body is null");
        }

        try {
            JsonObject root = JsonParser.parseString(response.getBody()).getAsJsonObject();

            EmployeeResponse employeeResponse = gson.fromJson(root, EmployeeResponse.class);

            List<Employee> employees = employeeResponse.getData();

            employees.forEach(each -> logger.info(String.valueOf(each)));
            return employees;
        } catch (Exception e) {
            logger.error("Error parsing JSON response", e);
        }
        return List.of();
    }
}