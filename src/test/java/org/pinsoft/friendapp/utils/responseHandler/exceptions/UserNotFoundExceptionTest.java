package org.pinsoft.friendapp.utils.responseHandler.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserNotFoundExceptionTest {


    private final String message = "Exception";
    private final UserNotFoundException UserNotFoundException = new UserNotFoundException(message);

    @Test
    public void testUserNotFoundExceptionWithMessage() {
        assertEquals(UserNotFoundException.getMessage(), message);
    }

    @Test
    void shouldThrowException() {
        Throwable exception = assertThrows(UserNotFoundException.class, () -> {
            throw new UserNotFoundException("Not supported");
        });
        assertEquals("Not supported", exception.getMessage());
    }

    @Test
    void assertThrowsException() {
        String str = null;
        assertThrows(IllegalArgumentException.class, () -> {
            Integer.valueOf(str);
        });

    }
}