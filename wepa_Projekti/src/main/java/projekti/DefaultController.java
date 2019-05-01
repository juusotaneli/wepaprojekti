package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("*")
    public String handleDefault() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String paska(@RequestParam String username, @RequestParam String password, Model model) {
        Account a = accountRepository.findByUsername(username);
        if (a != null && passwordEncoder.matches(password, a.getPassword())) {
            model.addAttribute("accounts", accountRepository.findAll());
            return "redirect:/" + accountRepository.findByUsername(username).getUseraddress();

        }
        return "login";

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/{useraddress}")
    public String home(Model model, @PathVariable String useraddress) {
        model.addAttribute("account", accountRepository.findByUseraddress(useraddress));
        return "homepage";
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
        return "login";
    }

    @PostMapping("/search")
    public String userSearch(@RequestParam String usersearch, Model model) {
        if (accountRepository.findByUsername(usersearch) != null) {
            return "redirect:/" + accountRepository.findByUsername(usersearch).getUseraddress();
        } else {
            model.addAttribute("accounts", accountRepository.findAll());
            return "usersearch";

        }
    }
    @GetMapping("/search")
    public String userSearch(Model model, @RequestParam String usersearch) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "usersearch";
    }
   
}
