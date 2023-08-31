package rs.ac.uns.ftn.svtvezbe07.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

@Repository
public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long>{
	   GroupRequest findGroupRequestById(Long id);
//	   List<Reaction> findAllByUser(User user);
	   void deleteById(Long id);
	   List<GroupRequest> findAllByGroupId(Long g);
}
