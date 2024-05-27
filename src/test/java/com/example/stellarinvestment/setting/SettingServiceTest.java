package com.example.stellarinvestment.setting;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;

class SettingServiceTest {

    @Test
    void getEmailSettings() {
        // Mock the SettingRepository
        SettingRepository settingRepository = Mockito.mock(SettingRepository.class);

        // Create sample settings for mail server category
        List<Setting> mailServerSettings = Arrays.asList(
                new Setting("MAIL_HOST", "smtp.example.com", SettingCategory.MAIL_SERVER)
                // ... (other settings for mail server)
        );

        // Create sample settings for mail templates category
        List<Setting> mailTemplatesSettings = Arrays.asList(
                new Setting("MAIL_HOST", "smtp.example.org", SettingCategory.MAIL_TEMPLATES)
                // ... (other settings for mail templates)
        );

        // Mock the findByCategory method of the SettingRepository
        Mockito.when(settingRepository.findByCategory(SettingCategory.MAIL_SERVER)).thenReturn(mailServerSettings);
        Mockito.when(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES)).thenReturn(mailTemplatesSettings);

        // Create an instance of SettingService and set the mocked SettingRepository
        SettingService settingService = new SettingService();
    }
}
