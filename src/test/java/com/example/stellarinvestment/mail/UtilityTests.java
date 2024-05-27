package com.example.stellarinvestment.mail;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import static org.junit.jupiter.api.Assertions.*;

class UtilityTests {

    @Test
    void getSiteURL() {
        // Create a mock HttpServletRequest
        MockHttpServletRequest request = new MockHttpServletRequest();

        // Set the servlet path
        request.setServletPath("/path");

        // Call the getSiteURL method
        String siteURL = Utility.getSiteURL(request);

        // Assert the result as needed
        assertEquals("http://localhost", siteURL);
    }

    @Test
    void prepareMailSender() {
        assertThrows(NullPointerException.class, () -> {
            Utility.prepareMailSender(null);
        });
    }
}
