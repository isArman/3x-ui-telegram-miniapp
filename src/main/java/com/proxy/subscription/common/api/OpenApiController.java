package com.proxy.subscription.common.api;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;

@RestController
public class OpenApiController {

    @GetMapping(value = "/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> serveOpenApi() throws Exception {
        ClassPathResource resource = new ClassPathResource("static/openapi.yaml");
        String content = new String(Files.readAllBytes(resource.getFile().toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/yaml"))
                .body(content);
    }
}
