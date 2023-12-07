package chatapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthCheck {

        @RequestMapping("/health-check")
        public ResponseEntity<Map<String, String>> health() {
            Map<String, String> response = new HashMap<>();
            response.put("message", "OK");
            return ResponseEntity.status(200).body(response);
        }
}
