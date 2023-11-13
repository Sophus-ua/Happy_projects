package persistence.dao.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import persistence.dao.repositories.IUserRepository;
import persistence.dao.services.interfaces.IUserService;

@Slf4j
@Service
public class UserServiceImpl  implements IUserService {
    private IUserRepository userRepository;
    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
