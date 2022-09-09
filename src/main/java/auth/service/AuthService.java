package auth.service;

import javax.servlet.http.HttpServletRequest;

import auth.model.UserToken;
import auth.util.RandomUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthService {

    public static UserToken getCurrentUser(HttpServletRequest request){
        UserToken userToken = new UserToken();

        String token = request.getHeader("Authorization");
        String accountId = request.getHeader("AccountId");

        if (token == null){
            userToken.setAccountId(RandomUtil.getRandomStringWith(10));
            userToken.setToken(RandomUtil.getRandomStringWith(32));
        }else {
            userToken.setToken(token);
            userToken.setAccountId(accountId);
        }

        if (!userTokenHaveUserNameFromCookie(userToken)) {
            userToken.setAccountId(RandomUtil.getRandomStringWith(10));
            userToken.setToken(RandomUtil.getRandomStringWith(32));
        }
        return userToken;
    }

    /**
     * Check that user token contains username that is username from cookie
     */
    public static boolean userTokenHaveUserNameFromCookie(UserToken userToken){
        final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(userToken.getToken()).getBody();
        return claims.getSubject().equals(userToken.getAccountId());
    }

    /**
     * Return role of user
     */
    public static String getRole(UserToken userToken){
        final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(userToken.getToken()).getBody();
        return claims.get("roles").toString();
    }

    /**
     * This method compare login for operation with current logged user
     * @param login - login of user
     * @param userToken - user token object
     * @return  true of false
     */
    public static boolean userHasPermissionForAction(String login, UserToken userToken){
        if (userTokenHaveUserNameFromCookie(userToken)){
            return userToken.getAccountId().equals(login);
        }else {
            return false;
        }
    }

    /**
     * This method compare token of current user and token from database
     * @param userToken - token from DB
     * @param currentUserToken - token of logged user
     * @return true/false
     */
    public static boolean tokenIsValid(UserToken userToken, UserToken currentUserToken){
        String currentToken = currentUserToken.getToken();
        if (userToken == null){
            return false;
        }else {
            return currentToken.equals(userToken.getToken());
        }
    }
}
