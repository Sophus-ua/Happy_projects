package persistence.dao.services.interfaces;

import exceptions.LoginAndRegistrationException;
import models.UserDTO;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;



@Service
public interface IUserService extends UserDetailsService{
    String registrationNewUser(UserDTO userDTO) throws LoginAndRegistrationException;
    @Nullable
    Long getUserIdByUsername(String username);

    void updateLastLoginDateByUsername(String username);

    String changeUserActivityStatus(Long userId);

    String deleteUserById(Long userId);
}
