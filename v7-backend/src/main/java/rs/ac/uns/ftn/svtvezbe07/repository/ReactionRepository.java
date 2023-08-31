package rs.ac.uns.ftn.svtvezbe07.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long>{
	   Optional<Reaction> findReactionByTimestamp(LocalDateTime t);
	   Reaction findReactionById(Integer id);
//	   List<Reaction> findAllByUser(User user);
	   void deleteById(Long id);
//	   List<Reaction> findAllByPost(Post user);
}
