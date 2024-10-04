package com.uevitondev.deliverybackend.domain.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
public class ApiTestController {


    @GetMapping("/public")
    public ResponseEntity<TestResponse> routePublic() {
        return ResponseEntity.ok().body(new TestResponse("access public route success!"));
    }

    @GetMapping("/authenticated")
    public ResponseEntity<TestResponse> routeAuthenticated() {
        return ResponseEntity.ok().body(new TestResponse("access authenticated route success!"));
    }

    @GetMapping("/admin")
    public ResponseEntity<TestResponse> routeAdmin() {
        return ResponseEntity.ok().body(new TestResponse("access admin route success!"));
    }


}