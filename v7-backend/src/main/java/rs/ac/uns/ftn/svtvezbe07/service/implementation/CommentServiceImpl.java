package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.CommentRepository;
import rs.ac.uns.ftn.svtvezbe07.service.CommentService;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
@Service
public class CommentServiceImpl implements CommentService{

 	@Autowired
    private CommentRepository commentRepository;

 	private PostService postService;

    @Override
    public List<Comment> getAll() {
        return this.commentRepository.findAll();
    }
    

	public Comment save(Comment comment) {
		return this.commentRepository.save(comment);
	}


	@Override
	public List<Comment> findAllByUserId(User user) {
	    return commentRepository.findAllByUserId(user);
	}
	

	@Override
	public List<Comment> findAllByPost(Post user) {
	    return commentRepository.findAllByPost(user);
	}


	
	/*public Optional<Comment> getByIdInt(Integer id) {
		return CommentRepository.findCommentById(id);
	}*/
	


    @Override
    public Comment findCommentByText(String content) {
        Optional<Comment> Comment = commentRepository.findCommentByText(content);
        if(!Comment.isEmpty()){
            return Comment.get();
        }

        return null;
    }

    public Comment findComment(Long id) {
        Optional<Comment> Comment = commentRepository.findById(id);
        if(!Comment.isEmpty()){
            return Comment.get();
        }

        return null;
    }
    
    public Comment findCommentById(Integer id) {
        Comment Comment = commentRepository.findCommentById(id);
        if(Comment!=null){
            return Comment;
        }

        return null;
    }
    @Override
    public void delete(Long idInt) {
        this.commentRepository.deleteById(idInt);
    }

	@Override
	public Comment createComment(rs.ac.uns.ftn.svtvezbe07.model.dto.CommentDTO newComment) {
		 Comment comment = new Comment();
	        comment.setText(newComment.getText());
	        comment.setTimestamp(LocalDateTime.now());
	        //comment.setComment(newComment.getComment());
	        comment.setDeleted(false);
	        Long l = newComment.getPost();
	        Post p =postService.findPost(l);
	        comment.setPost(p);
	        comment.setRepliesComment(newComment.getReplies());
	        comment.setUserId(newComment.getUser());
	        comment = commentRepository.save(comment);
	        return comment;
	}


}
