package edu.malaka96.authify_backend.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRequest {

    private String userId;
    @NotBlank(message = "Name should not be empty")
    private String name;
    @Email(message = "Enter valid email address")
    @NotNull
    private String email;
    @Min(value = 6, message = "Password must be longer than 6")
    private String password;
    private Boolean isAccountVarifield;

}
