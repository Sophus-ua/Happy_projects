package persistence.dao.services.implementations;

import exceptions.LoginAndRegistrationException;
import lombok.extern.slf4j.Slf4j;
import models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import persistence.dao.repositories.IUserRepository;
import persistence.dao.services.interfaces.IUserService;
import persistence.entity.User;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional = userRepository.findByUsername(username);
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException("User not found with login: " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(optional.get().getUsername())
                .password(optional.get().getPassword())
                .roles(optional.get().getRole().name())
                .disabled(!optional.get().isEnabled())
                .build();
    }


    @Nullable
    @Override
    public Long getUserIdByUsername(String username) {
        Integer userId = userRepository.getIdByUsername(username);
        if (userId != null)
            return userId.longValue();
        else
            return null;
    }


    @Override
    public String registrationNewUser(UserDTO userDTO) throws LoginAndRegistrationException {
        if (userDTO == null || userDTO.getOwnName() == null || userDTO.getPassword() == null ||
                userDTO.getUsername() == null || userDTO.getOwnName().isEmpty() || userDTO.getPassword().isEmpty() ||
                userDTO.getUsername().isEmpty()) {
            throw new LoginAndRegistrationException(ExceptionMessage.ERROR_DATA.message);
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword()))
            throw new LoginAndRegistrationException(ExceptionMessage.PASSWORDS_DO_NOT_MATCH.message);

        if (userRepository.existsByUsername(userDTO.getUsername()))
            throw new LoginAndRegistrationException(ExceptionMessage.ALREADY_EXISTS.message);

        User user = userRepository.save(new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getOwnName()));
        return String.format(ExceptionMessage.GREETING_USER.message, user.getOwnName());
    }


    private enum ExceptionMessage {
        PASSWORDS_DO_NOT_MATCH("Ви ввели різні паролі..."),
        ALREADY_EXISTS("Користувач з таким логіном вже існує :("),
        FAILED_TO_SAVE("Не вдалося зареєструвати нового користувача :("),
        GREETING_USER("%1s, вітаємо ви успішно зареєструвались :)"),
        INCORRECTLY_ENTERED_DATA("Ви ввели неправильно логін чи пароль"),
        ERROR_DATA("Помилка в данних користувача");
        private final String message;

        ExceptionMessage(String message) {
            this.message = message;
        }
    }

    @Override
    public void updateLastLoginDateByUsername(String username) {
        LocalDate loginDate = LocalDate.now();
        userRepository.updateLastLoginDateByUsername(loginDate, username);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public String changeUserActivityStatus(Long userId) {
        if (userId == null)
            return Message.ERROR_WHEN_CHANGING_ACTIVATION_STATUS.message;

        boolean activityStatus = userRepository.isEnabled(userId);
        userRepository.changeUserActivityStatus(userId);
        boolean currentActivityStatus = userRepository.isEnabled(userId);

        if (activityStatus == currentActivityStatus)
            return Message.ERROR_WHEN_CHANGING_ACTIVATION_STATUS.message;
        else if (currentActivityStatus)
            return Message.SUCCESSFUL_ACTIVATION.message;
        else
            return Message.SUCCESSFUL_DEACTIVATION.message;
    }

    @Override
    @Secured("ROLE_ADMIN")
    public String deleteUserById(Long userId) {
        if (userId == null || userRepository.userExistsById(userId) == 0)
            return Message.ERROR_WHEN_TRYING_TO_DELETE_USER.message;

        userRepository.deleteById(userId);
        userRepository.resetAutoIncrement();

        if (userRepository.userExistsById(userId) > 0)
            return Message.ERROR_WHEN_TRYING_TO_DELETE_USER.message;

        return Message.SUCCESSFUL_REMOVAL.message;
    }

    private enum Message {
        SUCCESSFUL_ACTIVATION("Користувач успішно активований"),
        SUCCESSFUL_DEACTIVATION("Користувач успішно деактивований"),
        ERROR_WHEN_CHANGING_ACTIVATION_STATUS("Помилка при спробі змінити статус активації користувача"),
        SUCCESSFUL_REMOVAL("Користувач видалений"),
        ERROR_WHEN_TRYING_TO_DELETE_USER("Помилка при спробі видалити користувача");
        private final String message;

        Message(String message) {
            this.message = message;
        }
    }

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
