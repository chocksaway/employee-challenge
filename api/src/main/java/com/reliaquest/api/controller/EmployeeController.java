package com.reliaquest.api.controller;

import com.reliaquest.api.entity.DeleteMockEmployeeInput;
import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.service.EmployeeService;
import com.reliaquest.api.entity.CreateMockEmployeeInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Get all employees
     *
     * @return list of employees
     */
    @GetMapping("/")
    public ResponseEntity<List<Employee>> getEmployees() {
        return employeeService.getEmployees();
    }

    /**
     * Search for employee by name
     *
     * @param searchString - partial name of employee
     * @return list of employees
     */
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        return employeeService.getEmployeesByNameSearch(searchString);
    }

    /**
     * Get employee by Id
     *
     * @param id - employee Id
     * @return employee
     */
    @GetMapping("/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        final UUID uuid = UUID.fromString(id);

        return employeeService.getEmployeeById(uuid);
    }


    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return employeeService.getHighestSalaryOfEmployees();
    }


    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return employeeService.getTopTenHighestEarningEmployeeNames();
    }

    /**
     * Create employee
     * @param employee - CreateMockEmployeeInput - copied from Server project
     * @return - employee which has been created (including id)
     */
    @PostMapping("/")
    ResponseEntity<Employee> createEmployee(@RequestBody CreateMockEmployeeInput employee) {
        return employeeService.createEmployee(employee);
    }

    /**
     * Delete employee by id
     * @param id - the id of the employee
     * @return - the response string (name of employee Successfully processed request)
     *
     * Using RequestParam - 405 method not allowed with a PathParam
     *
     */
    @DeleteMapping("/")
    ResponseEntity<String> deleteEmployeeById(@RequestParam String id) {
        final UUID uuid = UUID.fromString(id);

        Optional<Employee> employee = employeeService.findEmployeeById(uuid);

        if (employee.isPresent()) {
            DeleteMockEmployeeInput input = new DeleteMockEmployeeInput();
            input.setName(employee.get().getEmployeeName());
            Optional<String> deleted = employeeService.deleteEmployee(input);

            return deleted.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).build());
        }

        return ResponseEntity.status(404).build();
    }
}
