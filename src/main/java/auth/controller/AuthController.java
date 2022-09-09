package auth.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auth.model.TokenContainer;
import auth.model.UserToken;
import auth.repository.TokenRepository;
import auth.service.AuthService;
import auth.settings.CommonSettings;
import auth.util.RandomUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class AuthController {

    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(CommonSettings.class);
    List<String> adminUsers = applicationContext.getEnvironment().getProperty("admins", List.class);

    @Autowired
    private TokenRepository tokenRepository;

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;
        public String accountName;

        public LoginResponse(final String token, final String accountName) {
            this.token = token;
            this.accountName = accountName;
        }
    }

    @GetMapping("/api/account/token/validation")
    public TokenContainer getTokenStatus(HttpServletRequest request) {
        UserToken currentUserToken = AuthService.getCurrentUser(request);
        UserToken userToken = tokenRepository.findUserTokenByAccountId(currentUserToken.getAccountId());
        if (userToken == null){
            return new TokenContainer(false, null);
        }else {
            if(AuthService.tokenIsValid(userToken, currentUserToken)){
                return new TokenContainer(true, AuthService.getRole(userToken));
            }else {
                return new TokenContainer(false, null);
            }
        }
    }

    @GetMapping("/api/account/permission/validation")
    public boolean checkUserPermission(@RequestParam("login") String login, HttpServletRequest request){
        UserToken currentUserToken = AuthService.getCurrentUser(request);
        UserToken userToken = tokenRepository.findUserTokenByAccountId(currentUserToken.getAccountId());
        if (AuthService.tokenIsValid(userToken, currentUserToken)){
            return AuthService.userHasPermissionForAction(login, currentUserToken);
        }else {
            return false;
        }
    }

    @GetMapping("/api/login")
    public TokenContainer login(@RequestParam("AccountId") String login){
        String role = "user";
        if (adminUsers.contains(login)){
            role = "Admin";
        }
        String token = Jwts.builder().setSubject(login)
                .claim("roles", role).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

        UserToken userToken = tokenRepository.findUserTokenByAccountId(login);
        if (userToken != null){
            userToken.setToken(token);
        }else {
            userToken = new UserToken(login, true, token);
        }
        tokenRepository.save(userToken);
        return new TokenContainer(true, AuthService.getRole(userToken), token);
    }

    @GetMapping("/api/resetToken")
    public boolean resetToken(@RequestParam("login") String login, HttpServletRequest request){
        UserToken currentUserToken = AuthService.getCurrentUser(request);
        UserToken userToken = tokenRepository.findUserTokenByAccountId(currentUserToken.getAccountId());
        if (AuthService.tokenIsValid(userToken, currentUserToken)){
            if (AuthService.userHasPermissionForAction(login, currentUserToken)){
                userToken.setToken(RandomUtil.getRandomStringWith(32));
                tokenRepository.save(userToken);
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

}
