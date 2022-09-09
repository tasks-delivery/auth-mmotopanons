package auth.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res,
        final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final String token = request.getHeader("Authorization");
        if (token == null) {
            throw new ServletException("Missing or invalid Authorization header.");
        }
        final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
        request.setAttribute("claims", claims);
        chain.doFilter(req, res);
    }

}
