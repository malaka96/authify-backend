package edu.malaka96.authify_backend.controller;

import edu.malaka96.authify_backend.io.ProfileRequest;
import edu.malaka96.authify_backend.io.ProfileResponse;
import edu.malaka96.authify_backend.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid @RequestBody ProfileRequest request){
        return profileService.createProfile(request);
    }

}
