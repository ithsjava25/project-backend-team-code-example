package demo.codeexample.auth;

public interface AuthLookup {

    // public API interface

       void changePassword(ChangePasswordRequest request, String authHeader);

       LoginResponse getLoginResponse(LoginRequest request);
}