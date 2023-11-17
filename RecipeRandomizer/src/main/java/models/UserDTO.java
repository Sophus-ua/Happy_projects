package models;

import lombok.Data;

import persistence.entity.User;

import java.time.LocalDate;


@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private String ownName;
    private boolean enabled;
    private LocalDate registrationDate;
    private LocalDate lastLoginDate;


    public UserDTO(){}

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.ownName = user.getOwnName();
        this.enabled = user.isEnabled();
        this.registrationDate = user.getRegistrationDate();
        this.lastLoginDate = user.getLastLoginDate();
    }
}
