package com.poc.reports.dto;

import com.poc.reports.models.RoleEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String userName;
    private String email;
    private String password;
    private long roleId;

}
