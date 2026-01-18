package br.edu.ifba.inf015.medHealthAPI.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class StatusController {
    @Autowired
    private DataSource dataSource;

    @Value("${info.app.version:unknown}")
    private String appVersion;

    @GetMapping
    @Operation(summary = "Get the status of the API and Database")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        Map<String, String> database = new LinkedHashMap<>();

        status.put("updated_at", Instant.now().toString());
        status.put("status", "UP");
        status.put("version", appVersion);

        try(Connection connection = dataSource.getConnection()){
            DatabaseMetaData metaData = connection.getMetaData();
            database.put("status", "UP");
            database.put("product", metaData.getDatabaseProductName());
            database.put("version", metaData.getDatabaseProductVersion());
        } catch (Exception e) {
            database.put("status", "DOWN");
            database.put("error", e.getMessage());
        }

        status.put("database", database);

        return ResponseEntity.ok(status);
    }
}
