package com.klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin // CORS is also configured globally in WebConfig; keep this for safety
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            // basic validation
            if (user.getEmail() == null || user.getPassword() == null || user.getName() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name, email and password are required");
            }
            // check existing
            User existing = userService.findByEmail(user.getEmail());
            if (existing != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
            }

            // encrypt password before saving
            user.setPassword(new Cryptography().encryptData(user.getPassword()));
            User saved = userService.insertData(user);
            // do not return password field
            saved.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email and password are required");
            }
            User result = userService.loginCheck(user);
            if (result == null) {
                // loginCheck returns null for not found OR wrong password
                // Distinguish between not found and wrong password:
                User byEmail = userService.findByEmail(user.getEmail());
                if (byEmail == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
                }
            }
            // remove password in response
            result.setPassword(null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }
}
