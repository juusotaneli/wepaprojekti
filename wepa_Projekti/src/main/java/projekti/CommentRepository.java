/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Juuso
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List <Comment> findByPicture(Picture picture);
    
    
}
