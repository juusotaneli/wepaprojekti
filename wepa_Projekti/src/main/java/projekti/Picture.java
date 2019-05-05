/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author Juuso
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Picture extends AbstractPersistable<Long> {
    
    @Lob
    private byte[] content;
    
    private boolean profilepicture;

    @ManyToOne
    Account account;
    /*
    @OneToMany
    List <Comment> comments = new ArrayList <> ();
    */
}
    
    
    
