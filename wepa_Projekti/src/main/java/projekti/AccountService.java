/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Juuso
 */
@Service
public class AccountService {

    @Autowired
    AccountRepository ar;

    public boolean areFriends(String username, String useraddress) {
        //tsekkaa onko tarkasteltava profiili oma vai toisen käyttäjän
        if (!ar.findByUsername(username).getUseraddress().equals(useraddress)) {
            return true;
        }
        return false;
    }

    public void addFriend(String username, String useraddress) {
        //ar.findByUsername(username).getFriends().add(ar.findByUseraddress(useraddress));
    }

    public boolean friendRequestAlreadySent(String username, String useraddress) {
        for (int i = 0; i < ar.findByUsername(username).getSentfriendrequests().size(); i++) {
            if (ar.findByUsername(username).getSentfriendrequests().get(i).getReceiver().getUsername().equals(ar.findByUseraddress(useraddress).getUsername())) {
                return true;
            }

        }
        
        return false;

    }

}
