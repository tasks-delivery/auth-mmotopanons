package auth.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import auth.model.UserToken;

public interface TokenRepository extends CrudRepository<UserToken, Long> {

    @Query("SELECT u FROM UserToken u where u.accountId = :accountId")
    UserToken findUserTokenByAccountId(@Param("accountId") String accountId);

}
