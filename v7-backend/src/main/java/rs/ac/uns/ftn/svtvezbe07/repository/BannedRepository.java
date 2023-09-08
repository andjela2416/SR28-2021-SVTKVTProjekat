package rs.ac.uns.ftn.svtvezbe07.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Banned;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

@Repository
public interface BannedRepository extends JpaRepository<Banned, Long>{
	   Banned findBannedById(Long id);
	   Banned findBannedByTowards(User u);
//	   List<Reaction> findAllByUser(User user);
	   void deleteById(Long id);
}
