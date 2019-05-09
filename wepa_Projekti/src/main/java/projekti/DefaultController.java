package projekti;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {

    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    FriendRequestRepository frr;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    AccountService as;


    @GetMapping("*")
    public String handleDefault(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account a = accountRepository.findByUsername(username);
        if (a != null) {
            return "redirect:/" + a.getUseraddress();
        }

        return "login";
    }

    @PostMapping("/")
    public String paska(@RequestParam String username, @RequestParam String password, Model model) {
        if (accountRepository.findByUsername(username) != null && passwordEncoder.matches(password, accountRepository.findByUsername(username).getPassword())) {
            model.addAttribute("accounts", accountRepository.findByUsername(username));
            model.addAttribute("friends", accountRepository.findByUsername(username).getFriends());
            return "redirect:/" + accountRepository.findByUsername(username).getUseraddress();
        }
        return "login";

    }

    @GetMapping("/login")
    public String login(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (accountRepository.findByUsername(username) != null) {
            model.addAttribute("account", accountRepository.findByUsername(username));
            return "redirect:/" + accountRepository.findByUsername(username).getUseraddress();
        }

        return "login";
    }

    @GetMapping("/register")
    public String kusi(Model model) {
        model.addAttribute("message", "perse :D");
        return "registration";
    }

    @GetMapping("/u")
    public String homepage(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "homepage";
    }

    @GetMapping("/d")
    public String delete() {
        accountRepository.deleteAll();
        frr.deleteAll();
        
        return "redirect:/login";
    }

    @PostMapping("/search")
    public String userSearch(@RequestParam String usersearch, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account a = accountRepository.findByUsername(username);
        if (accountRepository.findByName(usersearch) != null) {
            return "redirect:/" + accountRepository.findByName(usersearch).getUseraddress();
        } else {
            model.addAttribute("accounts", accountRepository.findByIdNotNull());
            model.addAttribute("requests", a.getReceivedfriendrequests());

            return "usersearch";

        }
    }
    @GetMapping("/search")
    public String userSearch(Model model, @RequestParam String usersearch) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account a = accountRepository.findByUsername(username);
        model.addAttribute("accounts", accountRepository.findByIdNotNull());
        model.addAttribute("requests", a.getReceivedfriendrequests());
        return "usersearch";
    }
}
