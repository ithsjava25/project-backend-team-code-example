package demo.codeexample.auth;

public interface AuthLookup {

       void changePassword(ChangePasswordRequest request, String authHeader);

       LoginResponse getLoginResponse(LoginRequest request);
}