package com.uevitondev.deliverybackend.domain.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery/v1/api/test")
public class ApiTestController {

    @GetMapping("/public")
    public ResponseEntity<String> routePublic() {
        return ResponseEntity.ok().body("access public route success!");
    }

    @GetMapping("/authenticated")
    public ResponseEntity<String> routeAuthenticated() {
        return ResponseEntity.ok().body("access authenticated route success!");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> routeAdmin() {
        return ResponseEntity.ok().body("access admin route success!");
    }


}