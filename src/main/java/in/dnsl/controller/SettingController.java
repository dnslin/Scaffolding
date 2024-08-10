package in.dnsl.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import in.dnsl.model.dto.UploadSettingsDTO;
import in.dnsl.model.entity.UploadConfiguration;
import in.dnsl.service.UploadConfigurationService;
import in.dnsl.utils.JSON;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/setting")
public class SettingController {

    private final UploadConfigurationService service;

    @GetMapping("/get")
    public Wrapper<UploadConfiguration> getUploadConfiguration() {
        UploadConfiguration config = service.getCurrentConfiguration();
        return WrapMapper.ok(config);
    }


    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/update")
    public Wrapper<UploadSettingsDTO> updateUploadSettings(@Valid @RequestBody UploadSettingsDTO settingsDTO) {
        UploadConfiguration convert = JSON.convert(settingsDTO, UploadConfiguration.class);
        UploadConfiguration config = service.updateConfiguration(convert);
        return WrapMapper.ok(JSON.convert(config, UploadSettingsDTO.class));
    }

}
