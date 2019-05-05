/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Juuso
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Post extends AbstractPersistable<Long> {
    
    @ManyToOne
    Account account;
    
    private String poster;
    private String content;
    private LocalDateTime ldt;
    
    
    
}
