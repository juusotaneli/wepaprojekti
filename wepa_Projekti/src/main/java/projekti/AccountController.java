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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired 
    AccountService as;
    
    @Autowired 
    FriendRequestRepository ar;
    
    @PostMapping("/register")
    public String registration(@RequestParam String name, @RequestParam String username, @RequestParam String password, @RequestParam String useraddress) {
        if (accountRepository.findByUsername(username) == null) {
            accountRepository.save(new Account(name, username, passwordEncoder.encode(password), useraddress, new ArrayList (), new ArrayList (), new ArrayList ())) ;
        }
        return "redirect:/login";
    }
    @GetMapping("/{useraddress}")
    public String home(Model model, @PathVariable String useraddress) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account loggedInUser = accountRepository.findByUseraddress(username);
        Account searchedUser = accountRepository.findByUseraddress(useraddress);
        if (searchedUser != null && !loggedInUser.equals(searchedUser)) {
            model.addAttribute("account", searchedUser);
            model.addAttribute("areFriends", as.areFriends(username, useraddress));
            model.addAttribute("friendRequestAlreadySent", as.friendRequestAlreadySent(username, useraddress));
            model.addAttribute("requests", searchedUser.getReceivedfriendrequests());
            return "homepage";
        } else if (searchedUser != null && loggedInUser.equals(searchedUser)) {
            model.addAttribute("account", loggedInUser);
            return "homepage";
        } else {
            return "index";
        }
    }
    
}
