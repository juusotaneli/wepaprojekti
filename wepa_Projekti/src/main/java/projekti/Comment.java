/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
public class Comment extends AbstractPersistable<Long> { 
    
    @OneToOne
    private Account account;
    
    @ManyToOne
    private Picture picture;
    
    private String text;
    private LocalDateTime ldt;

}
