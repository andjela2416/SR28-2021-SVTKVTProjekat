package rs.ac.uns.ftn.svtvezbe07.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	   Optional<Comment> findCommentByText(String content);
	   Comment findCommentById(Long id);
	   List<Comment> findAllByUserId(User user);
	   void deleteById(Long id);
	   List<Comment> findAllByPost(Post user);
}
