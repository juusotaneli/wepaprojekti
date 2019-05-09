/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author Juuso
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Picture extends AbstractPersistable<Long> {
    
    @Type(type = "org.hibernate.type.BinaryType")
    //@Lob
    private byte[] content;
    
    private boolean profilepicture;

    @ManyToOne
    private Account account;
    //
    
    @OneToMany (mappedBy = "picture")
    private List <Comment> comments = new ArrayList <> ();
    
    public List <Comment> onlytencomments () {
        
        return comments;
        

            
    }

}
    
    
    
