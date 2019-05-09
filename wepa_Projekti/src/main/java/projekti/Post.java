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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;

/**
 *
 * @author Juuso
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@NamedEntityGraph(name = "Post",
  attributeNodes = {@NamedAttributeNode("account"), @NamedAttributeNode("poster"), @NamedAttributeNode("content"), @NamedAttributeNode("ldt"), @NamedAttributeNode("likes")})
public class Post extends AbstractPersistable<Long> {
    
    @ManyToOne
    Account account;
    
    private String poster;
    private String content;
    private LocalDateTime ldt;
    
    @OneToMany (mappedBy = "post")
    private List <LikeObject> likes = new ArrayList<>();
    
}
