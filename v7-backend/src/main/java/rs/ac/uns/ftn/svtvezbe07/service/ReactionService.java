package rs.ac.uns.ftn.svtvezbe07.service;

import java.time.LocalDateTime;
import java.util.List;

import rs.ac.uns.ftn.svtvezbe07.model.dto.ReactionDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public interface ReactionService {
	List<Reaction> getAll();
    
	Reaction save (Reaction post);

  	Reaction findReactionById(Integer id);

  	Reaction findReaction(Long id);

  	Reaction createReaction(ReactionDTO newPost);
	
	Reaction findReactionByTimestamp(LocalDateTime a);

    void delete(Long id);

//    List<Reaction> findAllByUser(User user) ;
    
//    List<Reaction> findAllByPost(Post user) ;
}
