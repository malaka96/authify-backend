package edu.malaka96.authify_backend.service.impl;

import edu.malaka96.authify_backend.io.ProfileRequest;
import edu.malaka96.authify_backend.io.ProfileResponse;
import edu.malaka96.authify_backend.model.UserEntity;
import edu.malaka96.authify_backend.repository.UserRepository;
import edu.malaka96.authify_backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        UserEntity newProfile = convertToUserEntity(request);
        newProfile = userRepository.save(newProfile);
        return convertToProfileResponse(newProfile);
    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "+email));
        return convertToProfileResponse(existingUser);
    }

    private UserEntity convertToUserEntity(ProfileRequest request){
        return UserEntity.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();
    }

    private ProfileResponse convertToProfileResponse(UserEntity entity){
        return ProfileResponse.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .userId(entity.getUserId())
                .isAccountVarified(entity.getIsAccountVerified())
                .build();
    }
}
