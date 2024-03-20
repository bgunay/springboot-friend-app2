package org.pinsoft.friendapp.utils.responseHandler.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomExceptionTest {

    private final String message = "Exception";
    private final CustomException customException = new CustomException(message);

    @Test
    public void testCustomExceptionWithMessage() {
        assertEquals(customException.getMessage(), message);
    }

    @Test
    void shouldThrowException() {
        Throwable exception = assertThrows(CustomException.class, () -> {
            throw new CustomException("Not supported");
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