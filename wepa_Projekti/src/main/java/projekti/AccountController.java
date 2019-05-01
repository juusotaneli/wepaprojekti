/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

/**
 *
 * @author Juuso
 */
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @PostMapping("/createNewAccount")
    public String registration(@RequestParam String name, @RequestParam String username, @RequestParam String password, @RequestParam String useraddress) {
        if (accountRepository.findByUsername(username) == null) {
            accountRepository.save(new Account(name, username, passwordEncoder.encode(password), useraddress)) ;
        }
        return "redirect:/login";
    }
}
