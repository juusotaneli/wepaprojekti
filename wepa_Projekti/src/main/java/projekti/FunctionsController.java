/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.tools.FileObject;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Juuso
 */
@Controller
public class FunctionsController {

    @Autowired
    AccountRepository ar;
    
    @Autowired
    AccountService as;

    @Autowired
    FriendRequestRepository frr;

    @Autowired
    PostRepository pr;

    @Autowired
    PictureRepository prr;

    @Autowired
    LikeObjectRepository lor;
    
    @Autowired
    CommentRepository cr;

    @Transactional
    @GetMapping("/friendrequest/{id}")
    public String friendRequest(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account loggedinAccount = ar.findByUsername(username);

        Account a = ar.getOne(id);
        FriendRequest fr = new FriendRequest();
        fr.setReceiver(a);
        fr.setSender(loggedinAccount);
        frr.save(fr);

        loggedinAccount.getSentfriendrequests().add(fr);
        a.getReceivedfriendrequests().add(fr);

        model.addAttribute(loggedinAccount);

        return "redirect:/" + a.getUseraddress();
    }

    @GetMapping("/accept/{id}")
    public String acceptFriendRequest(@PathVariable Long id, Model model) {

        Account senderAcco = ar.getOne(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account receiverAcco = ar.findByUsername(username);

        ar.findByUsername(username).getFriends().add(senderAcco.getName());
        ar.getOne(id).getFriends().add(receiverAcco.getName());

        for (int i = 0; i < receiverAcco.getReceivedfriendrequests().size(); i++) {
            if (receiverAcco.getReceivedfriendrequests().get(i).getSender().getId().equals(senderAcco.getId())) {
                receiverAcco.getReceivedfriendrequests().remove(i);
            }
        }
        for (int i = 0; i < frr.findAll().size(); i++) {
            if (frr.findAll().get(i).getReceiver().equals(receiverAcco) && frr.findAll().get(i).getSender().equals(senderAcco)) {
                frr.deleteById(frr.findAll().get(i).getId());
            }

        }

        model.addAttribute("account", senderAcco);
        return "redirect:/notifications";
    }

    @GetMapping("/decline/{id}")
    public String declineFriendRequest(@PathVariable Long id) {
        Account senderAcco = ar.getOne(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account receiverAcco = ar.findByUsername(username);

        for (int i = 0; i < receiverAcco.getReceivedfriendrequests().size(); i++) {
            if (receiverAcco.getReceivedfriendrequests().get(i).getSender().getId().equals(senderAcco.getId())) {
                receiverAcco.getReceivedfriendrequests().remove(i);
            }
        }
        for (int i = 0; i < frr.findAll().size(); i++) {
            if (frr.findAll().get(i).getReceiver().equals(receiverAcco) && frr.findAll().get(i).getSender().equals(senderAcco)) {
                frr.deleteById(frr.findAll().get(i).getId());
            }
        }
        return "redirect:/notifications";
    }

    @Transactional
    @PostMapping("/post/{id}")
    public String postToTheWall(@PathVariable Long id, @RequestParam String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        LocalDateTime date = LocalDateTime.now();

        Account poster = ar.findByUsername(username);

        Account receiver = ar.getOne(id);

        Post post = new Post(receiver, poster.getName(), content, date, new ArrayList<>());

        pr.save(post);

        receiver.getPosts().add(post);

        return "redirect:/" + receiver.getUseraddress();

    }

    @GetMapping("/upload")
    public String uploadPictures(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account loggedInAcco = ar.findByUsername(username);

        model.addAttribute("account", loggedInAcco);
        return "pictures";

    }

    @PostMapping("/upload")
    public String create(@RequestParam("file") MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account loggedInAcco = ar.findByUsername(username);

        Picture p = new Picture(file.getBytes(), false, loggedInAcco, new ArrayList <> ());
        prr.save(p);

        loggedInAcco.getPictures().add(p);

        return "redirect:/" + loggedInAcco.getUseraddress();
    }

    @GetMapping(path = "/picture/{id}", produces = "image/png")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return prr.getOne(id).getContent();
    }

    @GetMapping("/album/{id}")
    public String pictures(Model model, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account loggedInAcco = ar.findByUsername(username);

        Account searchedAcco = ar.getOne(id);
        
        Pageable pageable = PageRequest.of(0, 25, Sort.by("ldt").descending());
        
        model.addAttribute("pictures", prr.findByAccount(searchedAcco));
        model.addAttribute("account", loggedInAcco);
        model.addAttribute("searchedaccount", searchedAcco);
        model.addAttribute("samePerson", as.samePerson(username, searchedAcco.getUseraddress()));
        model.addAttribute("areFriends", as.areFriends(username, searchedAcco.getUseraddress()));
       
        return "album";
    }
    @Transactional
    @PostMapping("/picture/{useraddress}/{id}")
    public String addComment(Model model, @PathVariable Long id, @PathVariable String useraddress, @RequestParam String comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account loggedInAcco = ar.findByUsername(username);
        
        LocalDateTime date = LocalDateTime.now();
        
        Comment c = new Comment (loggedInAcco, prr.getOne(id), comment, date);
        
        prr.getOne(id).getComments().add(c);
        
        cr.save(c);
        return "redirect:/album/" + ar.findByUseraddress(useraddress).getId();
    }
        
    @Transactional
    @GetMapping("/like/{useraddress}/{id}")
    public String addLike(Model model, @PathVariable Long id, @PathVariable String useraddress) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account loggedInAcco = ar.findByUsername(username);

        Post p = pr.getOne(id);

        LikeObject lo = new LikeObject(loggedInAcco, p);

        if (!p.getLikes().contains(lo)) {
            lor.save(lo);
            p.getLikes().add(lo);
        }else{
            for (int i = 0; i < lor.findByPost(p).size(); i++) {
                if (lor.findByPost(p).get(i).getAccount().equals(loggedInAcco)) {
                    lor.delete(lor.findByPost(p).get(i));
                    break;
                }
                
            }
        }

        return "redirect:/" + useraddress;

    }
    @GetMapping("/setprofilepicture/{id}")
    public String setProfilePicture (@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account loggedInAcco = ar.findByUsername(username);
        
        

        return "redirect:/album/" + loggedInAcco.getId();
        
    }
    @GetMapping("/deletepicture/{id}")
    public String deletePicture (@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account loggedInAcco = ar.findByUsername(username);
        
       
        
        return "redirect:/album/" + loggedInAcco.getId();
        
    }
    

}
