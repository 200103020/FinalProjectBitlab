package com.example.stellarinvestment.setting;

import com.example.stellarinvestment.setting.Setting;
import com.example.stellarinvestment.setting.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {
    public List<Setting> findByCategory(SettingCategory category);
}