package org.pinsoft.interview.domain.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.pinsoft.interview.utils.constants.ValidationMessageConstants;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateBindingModel implements Serializable {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String city;

    @NotNull(message = ValidationMessageConstants.ID_REQUIRED_MESSAGE)
    @Length(min = 1, message = ValidationMessageConstants.ID_REQUIRED_MESSAGE)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Pattern(regexp = "^([a-zA-Z0-9]+)$")
    @Size(min = 4, max = 16, message = ValidationMessageConstants.USER_INVALID_USERNAME_MESSAGE)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$",message = ValidationMessageConstants.USER_INVALID_EMAIL_MESSAGE)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotNull(message = ValidationMessageConstants.USER_ADDRESS_REQUIRED_MESSAGE)
    @Length(min = 1, message = ValidationMessageConstants.USER_ADDRESS_REQUIRED_MESSAGE)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull(message = ValidationMessageConstants.USER_CITY_REQUIRED_MESSAGE)
    @Length(min = 1, message = ValidationMessageConstants.USER_CITY_REQUIRED_MESSAGE)
    public String getCity() {
        return this.city;
    }

}
