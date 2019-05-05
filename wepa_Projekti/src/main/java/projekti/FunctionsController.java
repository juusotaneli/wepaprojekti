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
    FriendRequestRepository frr;

    @Autowired
    PostRepository pr;

    @Autowired
    PictureRepository prr;

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

        return "redirect:/" + a.getUsername();
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

        Post post = new Post(receiver, poster.getName(), content, date);

        pr.save(post);

        receiver.getPosts().add(post);

        return "redirect:/" + receiver.getUseraddress();

    }

    @GetMapping("/upload")
    public String uploadPictures() {
        return "pictures";

    }

    @PostMapping("/upload")
    public String create(@RequestParam("file") MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account loggedInAcco = ar.findByUsername(username);

        Picture p = new Picture(file.getBytes(), false, loggedInAcco);
        prr.save(p);

        loggedInAcco.getPictures().add(p);

        return "redirect:/upload";
    }
    @GetMapping(path = "/picture/{id}", produces = "image/png")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return prr.getOne(id).getContent();
    }
    
    @GetMapping("/album/{id}")
    public String pictures(Model model, @PathVariable Long id) {
        
        Account searchedAcco = ar.getOne(id);
        
        model.addAttribute("pictures", prr.findByAccount(searchedAcco));
        
        return "album";
        
    }

}
