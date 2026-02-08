package edu.malaka96.authify_backend.io;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

//    @NotBlank(message = "New password is required")
    private String newPassword;
//    @NotBlank(message = "OTP is required")
    private String otp;
//    @NotBlank(message = "Email is required")
    private String email;

}
