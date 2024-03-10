package com.ems.employee_service.utils;

import java.util.UUID;

public class TestDataGenerator {

    public static String generateUniqueEmail(String name) {
        // Generate a random UUID and combine it with the employee's name to create a meaningful email address
        String uuid = UUID.randomUUID().toString().substring(0, 8); // Take a substring to shorten the UUID
        return name.toLowerCase().replace(" ", ".") + "." + uuid + "@test.com";
    }

    public static String generateUniqueEmployeeId(){
        return UUID.randomUUID().toString().substring(0,8);
    }
}
