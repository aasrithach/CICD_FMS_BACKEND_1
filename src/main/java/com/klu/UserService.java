package com.klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repo1;

    public User insertData(User user) {
        return repo1.save(user);
    }

    public User findByEmail(String email) {
        return repo1.findByEmail(email);
    }

    /**
     * Return the authenticated user on success; return null on failure.
     * Do NOT return the incoming request object if authentication fails.
     */
    public User loginCheck(User incoming) {
        User stored = repo1.findByEmail(incoming.getEmail());
        if (stored == null) {
            // user not found
            return null;
        }
        // decrypt stored password and compare
        try {
            String decrypted = new Cryptography().decryptData(stored.getPassword());
            if (decrypted.equals(incoming.getPassword())) {
                return stored;
            } else {
                return null; // wrong password
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
