package models;

import lombok.Data;



@Data
public class UserDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String ownName;
}
