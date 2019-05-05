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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    @Autowired
    PostRepository pr;
    
    @Autowired
    PictureRepository prr;

    @PostMapping("/register")
    public String registration(@RequestParam String name, @RequestParam String username, @RequestParam String password, @RequestParam String useraddress) {
        if (accountRepository.findByUsername(username) == null) {
            accountRepository.save(new Account(name, username, passwordEncoder.encode(password), useraddress, new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()));
        }
        return "redirect:/login";
    }

    @GetMapping("/{useraddress}")
    public String home(Model model, @PathVariable String useraddress) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account loggedInUser = accountRepository.findByUseraddress(username);
        Account searchedUser = accountRepository.findByUseraddress(useraddress);
        Pageable pageable;

        pageable = PageRequest.of(0, 25, Sort.by("ldt").descending());
        
        List <Account> posts = pr.findByAccount(searchedUser, pageable);
        
        Collections.reverse(posts);
        
        Object picture;
        
        if (searchedUser.getPictures().isEmpty()) {
            picture = null;
        }else{
            picture = prr.findByAccount(searchedUser).get(0);
        }

        if (searchedUser != null && useraddress.equals(loggedInUser.getUseraddress())) {
            model.addAttribute("account", searchedUser);
            model.addAttribute("posts", posts);
            model.addAttribute("samePerson", as.samePerson(username, useraddress));
            model.addAttribute("areFriends", as.areFriends(username, useraddress));
            model.addAttribute("hasPicture", as.hasPicture(useraddress));
            model.addAttribute("friendRequestAlreadySent", as.friendRequestAlreadySent(username, useraddress));
            model.addAttribute("requests", searchedUser.getReceivedfriendrequests());
            model.addAttribute("picture", picture);

            return "homepage";
            
        } else if (searchedUser != null && !loggedInUser.equals(searchedUser)) {
            model.addAttribute("account", searchedUser);
            model.addAttribute("posts", posts);
            model.addAttribute("picture", picture);
            model.addAttribute("hasPicture", as.hasPicture(useraddress));
            model.addAttribute("samePerson", as.samePerson(username, useraddress));
            model.addAttribute("areFriends", as.areFriends(username, useraddress));
            model.addAttribute("friendRequestAlreadySent", as.friendRequestAlreadySent(username, useraddress));
            model.addAttribute("requests", loggedInUser.getReceivedfriendrequests());
            return "homepage";

        } else if (loggedInUser.equals(searchedUser)) {
            model.addAttribute("picture", picture);
            model.addAttribute("hasPicture", as.hasPicture(useraddress));
            model.addAttribute("posts", posts);
            model.addAttribute("account", loggedInUser);
            model.addAttribute("requests", loggedInUser.getReceivedfriendrequests());
            return "homepage";
        } else {
            return "index";
        }
    }

    @GetMapping("/notifications")
    public String getFriendRequests(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account a = accountRepository.findByUsername(username);
        model.addAttribute("requests", a.getReceivedfriendrequests());
        model.addAttribute("account", a);
        model.addAttribute("friends", a.getFriends());
        return "notifications";
    }
    /*@GetMapping("/album")
    public String goToAlbum(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account a = accountRepository.findByUsername(username);
        
        model.addAttribute("pictures", prr.findByAccount(a));
        
        return "album";
    */
    

}
