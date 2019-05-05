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
    
    @Autowired
    PictureRepository prr;

    public boolean samePerson(String username, String useraddress) {
        //tsekkaa onko tarkasteltava profiili oma vai toisen käyttäjän
        if (!ar.findByUsername(username).getUseraddress().equals(useraddress)) {
            return false;
        }
        return true;
    }

    public boolean friendRequestAlreadySent(String username, String useraddress) {
        for (int i = 0; i < ar.findByUseraddress(useraddress).getReceivedfriendrequests().size(); i++) {
            if (ar.findByUseraddress(useraddress).getReceivedfriendrequests().get(i).getSender().getUsername().equals(ar.findByUsername(username).getUsername())) {
                return true;
            }
        }
         for (int i = 0; i < ar.findByUsername(username).getReceivedfriendrequests().size(); i++) {
            if (ar.findByUsername(username).getReceivedfriendrequests().get(i).getSender().getUsername().equals(ar.findByUseraddress(useraddress).getUsername())) {
                return true;
            }
        }
        return false;
    }

    public void addFriend(String username, String useraddress) {
        //ar.findByUsername(username).getFriends().add(ar.findByUseraddress(useraddress));
    }
    public boolean areFriends(String username, String useraddress) {
        for (int i = 0; i < ar.findByUsername(username).getFriends().size(); i++) {
            if (ar.findByUsername(username).getFriends().contains(ar.findByUseraddress(useraddress).getName()) && ar.findByUseraddress(useraddress).getFriends().contains(ar.findByUsername(username).getName())) {
                return true;
            }

        }

        return false;

    }
    public boolean hasPicture(String useraddress) {
        Account a = ar.findByUseraddress(useraddress);

        return !a.getPictures().isEmpty();
    }

}
