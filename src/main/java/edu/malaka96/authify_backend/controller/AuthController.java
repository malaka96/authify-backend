package edu.malaka96.authify_backend.controller;

import edu.malaka96.authify_backend.io.AuthRequest;
import edu.malaka96.authify_backend.io.AuthResponse;
import edu.malaka96.authify_backend.io.ResetPasswordRequest;
import edu.malaka96.authify_backend.service.AuthService;
import edu.malaka96.authify_backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

//    private final AuthenticationManager authenticationManager;
//    private final CustomerUserDetailsService userDetailsService;
//    private final JwtUtil jwtUtil;

    private final AuthService authService;
    private final ProfileService profileService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        try{
//            authenticate(request.getEmail(),request.getPassword());
//            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//            final String jwtToken = jwtUtil.generateToken(userDetails);
            String jwtToken = authService.login(request.getEmail(),request.getPassword());
            ResponseCookie cookie = ResponseCookie.from("jwt",jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                    .body(new AuthResponse(request.getEmail(), jwtToken));

        }catch (BadCredentialsException ex){
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("message", "Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }catch (DisabledException ex){
            Map<String, Object> error = new HashMap<>();
            error.put(" error", true);
            error.put("message", "Account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }catch (Exception ex){
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("message", "Authentication is falied");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

//    private void authenticate(String email, String password){
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
//    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name") String email){
        return ResponseEntity.ok(email != null);
    }

    @PostMapping("/send-reset-otp")
    public void sendResetOtp(@RequestParam String email){
        try{
            profileService.sendOtp(email);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        profileService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successful");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email){
        profileService.sendVerifyOtp(email);
        return ResponseEntity.ok("Verify OTP is sent");
    }

    @PostMapping("/verify-otp")
    public void verifyEmail(@RequestBody Map<String, Object> request, @CurrentSecurityContext(expression = "authentication?.name") String email){
        if(request.get("otp").toString() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missin details");
        }
        try {
            profileService.verifyOtp(email, request.get("otp").toString());
        } catch (ResponseStatusException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
