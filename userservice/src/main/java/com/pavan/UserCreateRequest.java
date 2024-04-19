package com.pavan;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserCreateRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String contact;
    public User toUser() {
        return User.builder()
                .name(this.name)
                .contact(this.contact)
                .email(this.email)
                .build();
    }
}
