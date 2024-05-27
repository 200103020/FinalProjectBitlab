package com.example.stellarinvestment.setting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingRepositoryTest {

    @Mock
    private SettingRepository settingRepository;

    @Test
    void findByCategory() {
        // Create a sample SettingCategory enum value
        SettingCategory category = SettingCategory.MAIL_SERVER;

        // Create a list of sample Setting objects
        List<Setting> settingList = new ArrayList<>();
        // Populate settingList with sample data

        // Mock the behavior of the settingRepository.findByCategory method
        when(settingRepository.findByCategory(category)).thenReturn(settingList);

        // Call the findByCategory method
        List<Setting> result = settingRepository.findByCategory(category);

        // Assert the result
        assertEquals(settingList, result);
    }
}
