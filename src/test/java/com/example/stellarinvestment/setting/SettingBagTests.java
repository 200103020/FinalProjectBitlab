package com.example.stellarinvestment.setting;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SettingBagTests {

    @Test
    void get() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        Setting setting1 = new Setting("key1", "value1", SettingCategory.MAIL_SERVER);
        Setting setting2 = new Setting("key2", "value2", SettingCategory.MAIL_TEMPLATES);
        listSettings.add(setting1);
        listSettings.add(setting2);
        SettingBag settingBag = new SettingBag(listSettings);

        // Act
        Setting result = settingBag.get("key1");

        // Assert
        assertEquals(setting1, result);
    }

    @Test
    void getValue() {
        // Arrange
        List<Setting> listSettings = new ArrayList<>();
        Setting setting1 = new Setting("key1", "value1", SettingCategory.PAYMENT);
        Setting setting2 = new Setting("key2", "value2", SettingCategory.MAIL_SERVER);
        listSettings.add(setting1);
        listSettings.add(setting2);
        SettingBag settingBag = new SettingBag(listSettings);

        // Act
        String value = settingBag.getValue("key1");

        // Assert
        assertEquals("value1", value);
    }

    @Test
    void update() {
        // Initialize the SettingBag object
        List<Setting> listSettings = new ArrayList<>();
        Setting setting = new Setting("key1", "value1", SettingCategory.MAIL_TEMPLATES);
        listSettings.add(setting);
        SettingBag settingBag = new SettingBag(listSettings);

        // Call the update method with an existing key and non-null value
        settingBag.update("key1", "new_value");

        // Check that the corresponding setting's value is updated
        assertEquals("new_value", setting.getValue());
    }

    @Test
    void list() {
        // Initialize the SettingBag object
        List<Setting> listSettings = new ArrayList<>();
        listSettings.add(new Setting("key1", "value1", SettingCategory.PAYMENT));
        listSettings.add(new Setting("key2", "value2", SettingCategory.MAIL_SERVER));
        SettingBag settingBag = new SettingBag(listSettings);

        // Invoke the list method
        List<Setting> result = settingBag.list();

        // Assert that the returned list is not null and has the same size as the original list
        assertNotNull(result);
        assertEquals(listSettings.size(), result.size());

        // Assert that each setting in the returned list is equal to the corresponding setting in the original list
        for (int i = 0; i < listSettings.size(); i++) {
            assertEquals(listSettings.get(i), result.get(i));
        }
    }
}
