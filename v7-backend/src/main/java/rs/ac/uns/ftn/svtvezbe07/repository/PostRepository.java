package rs.ac.uns.ftn.svtvezbe07.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	   Optional<Post> findPostByContent(String content);
	   Post findPostById(Long id);
	   List<Post> findAllByPostedBy(User user);
	   void deleteById(Long id);
	   List<Post> findAllByOrderByCreationDateAsc ();
	   List<Post> findAllByOrderByCreationDateDesc();

}
