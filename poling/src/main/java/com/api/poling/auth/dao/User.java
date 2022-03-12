package com.api.poling.auth.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@Data
@AllArgsConstructor
@Document(collection = "User")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String role;

}
