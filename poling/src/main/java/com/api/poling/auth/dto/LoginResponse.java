package com.api.poling.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoginResponse {
    private String id;
    private boolean isAdmin;
    private String token;
}
