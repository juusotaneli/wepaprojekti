/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    
    @GetMapping("/friendrequest/{id}")
    public String friendRequest(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account a = ar.getOne(id);
        
        FriendRequest fr = new FriendRequest();
        fr.setReceiver(a);
        fr.setSender(ar.findByUsername(username));
        frr.save(fr);

        ar.findByUsername(username).getSentfriendrequests().add(fr);
        
        return "redirect:/" + fr.getReceiver().getUseraddress();
    }

   
}
