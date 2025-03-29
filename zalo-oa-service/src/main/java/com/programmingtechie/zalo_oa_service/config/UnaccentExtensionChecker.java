package com.programmingtechie.zalo_oa_service.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
@Slf4j
public class UnaccentExtensionChecker {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void checkAndInstallUnaccentExtension() {
        String checkExtensionQuery = "SELECT * FROM pg_extension WHERE extname = 'unaccent'";

        if (jdbcTemplate.queryForList(checkExtensionQuery).isEmpty()) {
            String installExtensionQuery = "CREATE EXTENSION IF NOT EXISTS unaccent";
            jdbcTemplate.execute(installExtensionQuery);
            log.info("unaccent extension has been installed successfully.");
        } else {
            log.info("unaccent extension is already installed.");
        }
    }
}
