/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity

/**
 *
 * @author Juuso
 */
@NamedEntityGraph(name = "Acco",
  attributeNodes = {@NamedAttributeNode("name"), @NamedAttributeNode("username"), @NamedAttributeNode("useraddress")})

public class Account extends AbstractPersistable<Long> {
    private String name;
    private String username;
    private String password;
    private String useraddress;
    private ArrayList <String> friends;
    
    @OneToMany(mappedBy="receiver")
    private List <FriendRequest> receivedfriendrequests = new ArrayList<>();
    
    @OneToMany(mappedBy="sender")
    private List <FriendRequest> sentfriendrequests = new ArrayList<>();
    
    @OneToMany(mappedBy="account")
    private List <Post> posts = new ArrayList<>();
    
    @OneToMany(mappedBy="account")
    private List <Picture> pictures = new ArrayList<>();
    
}
