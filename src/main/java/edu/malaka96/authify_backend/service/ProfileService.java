package edu.malaka96.authify_backend.service;

import edu.malaka96.authify_backend.io.ProfileResponse;
import edu.malaka96.authify_backend.io.ProfileRequest;



public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getProfile(String email);
    void sendOtp(String email);
    void resetPassword(String email, String otp, String newPassword);
}
