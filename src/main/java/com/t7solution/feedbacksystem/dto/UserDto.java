package com.t7solution.feedbacksystem.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userName;
    private String email;
    private String password;
    private long roleId;
    private Long managerId;
}
