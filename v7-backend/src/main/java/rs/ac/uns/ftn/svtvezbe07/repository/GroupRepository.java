package rs.ac.uns.ftn.svtvezbe07.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{
	   Optional<Group> findGroupByName(String n);
	   Group findGroupById(Long l);
	   List<Group> findAllByGroupAdmin(User user);
	   void deleteById(Long id);

}
