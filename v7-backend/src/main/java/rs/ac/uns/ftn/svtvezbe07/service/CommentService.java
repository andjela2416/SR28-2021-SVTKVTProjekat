package rs.ac.uns.ftn.svtvezbe07.service;

import java.util.List;

import rs.ac.uns.ftn.svtvezbe07.model.dto.CommentDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.PostDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public interface CommentService {
	  	List<Comment> getAll();
	    
	  	Comment save (Comment post);

	  	Comment findCommentById(Integer id);

	  	Comment findComment(Long id);

	  	Comment createComment(CommentDTO newPost);
		
	  	Comment findCommentByText(String content);

	    void delete(Long id);

	    List<Comment> findAllByUserId(User user) ;
	    
	    List<Comment> findAllByPost(Post user) ;
}
