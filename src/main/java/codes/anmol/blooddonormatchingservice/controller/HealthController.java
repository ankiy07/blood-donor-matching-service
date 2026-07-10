package codes.anmol.blooddonormatchingservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> res = new HashMap<>();
        res.put("status", String.valueOf(HttpStatus.OK.value()));
        res.put("message", "Ok!");
        res.put("timestamp", String.valueOf(Instant.now()));
        return ResponseEntity.ok(res);
    }
}
