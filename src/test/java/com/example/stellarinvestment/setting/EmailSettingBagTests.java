package com.example.stellarinvestment.setting;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmailSettingBagTests {

    @Test
    void getHost() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("MAIL_HOST", "example.com", SettingCategory.PAYMENT));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String host = emailSettingBag.getHost();

        // Assert
        assertEquals("example.com", host);
    }

    @Test
    void getPort() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("MAIL_PORT", "12345", SettingCategory.MAIL_SERVER));

        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        int port = emailSettingBag.getPort();

        // Assert
        assertEquals(12345, port);
    }

    @Test
    void getUsername() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("MAIL_USERNAME", "john.doe@example.com", SettingCategory.MAIL_TEMPLATES));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String username = emailSettingBag.getUsername();

        // Assert
        assertEquals("john.doe@example.com", username);
    }

    @Test
    void getPassword() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("MAIL_PASSWORD", "password123", SettingCategory.MAIL_TEMPLATES));

        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String password = emailSettingBag.getPassword();

        // Assert
        assertEquals("password123", password);
    }

    @Test
    void getSmtpAuth() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("SMTP_AUTH", "true", SettingCategory.PAYMENT));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String result = emailSettingBag.getSmtpAuth();

        // Assert
        assertEquals("true", result);
    }

    @Test
    void getSmtpSecured() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("SMTP_SECURED", "true", SettingCategory.MAIL_SERVER));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String smtpSecured = emailSettingBag.getSmtpSecured();

        // Assert
        assertEquals("true", smtpSecured);
    }

    @Test
    void getFromAddress() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("MAIL_FROM", "test@example.com", SettingCategory.MAIL_TEMPLATES));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String result = emailSettingBag.getFromAddress();

        // Assert
        assertEquals("test@example.com", result);
    }

    @Test
    void getSenderName() {
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("MAIL_SENDER_NAME", "John Doe", SettingCategory.MAIL_SERVER));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        String senderName = emailSettingBag.getSenderName();

        assertEquals("John Doe", senderName);
    }

    @Test
    void getCustomerVerifySubject() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("CUSTOMER_VERIFY_SUBJECT", "Subject", SettingCategory.PAYMENT));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String result = emailSettingBag.getCustomerVerifySubject();

        // Assert
        assertEquals("Subject", result);
    }

    @Test
    void getCustomerVerifyContent() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("CUSTOMER_VERIFY_CONTENT", "Test Content", SettingCategory.MAIL_SERVER));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String result = emailSettingBag.getCustomerVerifyContent();

        // Assert
        assertEquals("Test Content", result);
    }

    @Test
    void getOrderConfirmationSubject() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("ORDER_CONFIRMATION_SUBJECT", "Order Confirmation Subject", SettingCategory.MAIL_TEMPLATES));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String result = emailSettingBag.getOrderConfirmationSubject();

        // Assert
        assertEquals("Order Confirmation Subject", result);
    }

    @Test
    void getOrderConfirmationContent() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("ORDER_CONFIRMATION_CONTENT", "Test content", SettingCategory.PAYMENT));
        EmailSettingBag emailSettingBag = new EmailSettingBag(listSettings);

        // Act
        String result = emailSettingBag.getOrderConfirmationContent();

        // Assert
        assertEquals("Test content", result);
    }
}
