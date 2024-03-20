package org.pinsoft.friendapp.domain.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.pinsoft.friendapp.utils.constants.ValidationMessageConstants;
import org.pinsoft.friendapp.utils.validations.annotations.Password;
import org.pinsoft.friendapp.utils.validations.annotations.PasswordMatching;
import org.pinsoft.friendapp.utils.validations.annotations.UniqueEmail;
import org.pinsoft.friendapp.utils.validations.annotations.UniqueUsername;

import java.io.Serializable;

@PasswordMatching
@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel implements Serializable {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String address;
    private String city;


    @Pattern(regexp = "^([a-zA-Z0-9]+)$")
    @Size(min = 3, max = 16, message = ValidationMessageConstants.USER_INVALID_USERNAME_MESSAGE)
    @UniqueUsername
    public String getUsername() {
        return this.username;
    }


    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$",message = ValidationMessageConstants.USER_INVALID_EMAIL_MESSAGE)
    @UniqueEmail
    public String getEmail() {
        return this.email;
    }


    @Password(minLength = 4, maxLength = 16, containsOnlyLettersAndDigits = true, message = ValidationMessageConstants.USER_INVALID_PASSWORD_MESSAGE)
    public String getPassword() {
        return this.password;
    }

    @Pattern(regexp = "^[A-Z]([a-zA-Z]+)?$", message = ValidationMessageConstants.USER_INVALID_FIRST_NAME_MESSAGE)
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Pattern(regexp = "^[A-Z]([a-zA-Z]+)?$", message = ValidationMessageConstants.USER_INVALID_LAST_NAME_MESSAGE)
    public String getLastName() {
        return this.lastName;
    }


    @NotNull(message = ValidationMessageConstants.USER_ADDRESS_REQUIRED_MESSAGE)
    @Length(min = 1, message = ValidationMessageConstants.USER_ADDRESS_REQUIRED_MESSAGE)
    public String getAddress() {
        return this.address;
    }

    @NotNull(message = ValidationMessageConstants.USER_CITY_REQUIRED_MESSAGE)
    @Length(min = 1, message = ValidationMessageConstants.USER_CITY_REQUIRED_MESSAGE)
    public String getCity() {
        return this.city;
    }

}
