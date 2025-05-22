package com.t7solution.feedbacksystem.dto;

import com.t7solution.feedbacksystem.model.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Long roleId;
    private String name;

    public Role orElse(Object o) {
        return null;
    }
}
