package com.example.stellarinvestment.setting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SettingTests {

    @Test
    void getKey() {
        String longKey = "a".repeat(129);
        Setting setting = new Setting(longKey, "value", SettingCategory.PAYMENT);
        String expected = "Setting [key=" + longKey.substring(0, 129) + ", value=value]";
        String actual = setting.toString();
        assertEquals(expected, actual);
    }

    @Test
    void setKey() {
        // Arrange
        Setting setting = new Setting();
        String key = "newKey";

        // Act
        setting.setKey(key);

        // Assert
        assertEquals(key, setting.getKey());
    }

    @Test
    void getValue() {
        Setting setting = new Setting("key", "value", SettingCategory.MAIL_SERVER);
        assertEquals("value", setting.getValue());
    }

    @Test
    void setValue() {
        Setting setting = new Setting();
        String input = "test value";

        setting.setValue(input);

        assertEquals(input, setting.getValue());
    }

    @Test
    void getCategory() {
        Setting setting = new Setting("key", "value", SettingCategory.MAIL_SERVER);
        assertEquals(SettingCategory.MAIL_SERVER, setting.getCategory());
    }

    @Test
    void setCategory() {
        // Arrange
        Setting setting = new Setting();
        SettingCategory category = SettingCategory.MAIL_SERVER;

        // Act
        setting.setCategory(category);

        // Assert
        assertEquals(category, setting.getCategory());
    }

    @Test
    void testHashCode() {
        Setting setting = new Setting("key", "value", SettingCategory.MAIL_TEMPLATES);
        int hashCode1 = setting.hashCode();
        int hashCode2 = setting.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testEquals() {
        Setting setting1 = new Setting("key1");
        Setting setting2 = new Setting("key1");

        assertTrue(setting1.equals(setting2));
    }

    @Test
    void testToString() {
        Setting setting = new Setting("key", "value", SettingCategory.MAIL_SERVER);
        String expected = "Setting [key=key, value=value]";
        String actual = setting.toString();
        assertEquals(expected, actual);
    }
}
