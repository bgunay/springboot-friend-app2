package org.pinsoft.friendapp.testUtils;


import org.pinsoft.friendapp.domain.dto.user.*;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UsersUtils {
    public static UserEntity createUser() {
        return new UserEntity() {{
            setId("1");
            setPassword("1111");
            setFirstName("AliVeli");
            setLastName("Aliv");
            setUsername("ali");
            setEmail("ali@abv.bg");
            setCity("Istanbul");
            setAddress("Vasil Levski 1");
            setOnline(false);
        }};
    }

    public static List<UserEntity> getUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new UserEntity() {{
                    setId(String.valueOf(index + 1));
                    setPassword("1111");
                    setFirstName("Ali " + index);
                    setLastName("Aliv " + index);
                    setUsername("ali " + index);
                    setEmail("ali " + index + " @abv.bg");
                    setCity("Istanbul");
                    setAddress("Vasil Levski 1");
                    setOnline(false);
                }})
                .collect(Collectors.toList());
    }

    public static UserRegisterBindingModel getUserRegisterBindingModel() {
        return new UserRegisterBindingModel() {{
            setPassword("1111");
            setConfirmPassword("1111");
            setFirstName("AliVeli");
            setLastName("Aliv");
            setUsername("ali");
            setEmail("ali@abv.bg");
            setCity("Istanbul");
            setAddress("Vasil Levski 1");
        }};
    }

    public static List<UserServiceModel> getUserServiceModels(int count) {
        LocalDateTime time = LocalDateTime.now();

        return IntStream.range(0, count)
                .mapToObj(index -> new UserServiceModel() {{
                    setId(String.valueOf(index + 1));
                    setPassword("1111");
                    setFirstName("Ali " + index);
                    setLastName("Aliv " + index);
                    setUsername("ali " + index);
                    setEmail("ali " + index + " @abv.bg");
                    setCity("Istanbul");
                    setAddress("Vasil Levski 1");
                    setOnline(false);
                }})
                .collect(Collectors.toList());
    }

    public static UserCreateViewModel getUserCreateViewModel() {
        return new UserCreateViewModel() {{
            setId("1");
            setFirstName("AliVeli");
            setLastName("Aliv");
            setUsername("ali");
            setEmail("ali@abv.bg");
            setCity("Istanbul");
            setAddress("Vasil Levski 1");
        }};
    }

    public static UserDetailsViewModel getUserDetailsViewModel() {
        return new UserDetailsViewModel() {{
            setId("1");
            setFirstName("AliVeli");
            setLastName("Aliv");
            setUsername("ali");
            setEmail("ali@abv.bg");
            setCity("Istanbul");
            setAddress("Vasil Levski 1");
        }};
    }

    public static UserEditViewModel getUserEditViewModel() {
        return new UserEditViewModel() {{
            setId("1");
            setFirstName("AliVeli");
            setLastName("Aliv");
            setUsername("ali");
            setEmail("ali@abv.bg");
            setCity("Istanbul");
            setAddress("Vasil Levski 1");
        }};
    }

    public static UserUpdateBindingModel getUserUpdateBindingModel() {
        return new UserUpdateBindingModel() {{
            setId("1");
            setFirstName("AliVeli");
            setLastName("Aliv");
            setUsername("ali");
            setEmail("ali@abv.bg");
            setCity("Istanbul");
            setAddress("Vasil Levski 1");
        }};
    }
}

