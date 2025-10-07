package com.poc.reports.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {

    private long id;
    private String userName;
    private String email;
    private String role;
    private String departmentName;
}
