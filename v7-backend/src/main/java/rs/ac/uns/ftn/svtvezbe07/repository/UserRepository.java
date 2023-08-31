package rs.ac.uns.ftn.svtvezbe07.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe07.model.dto.UserDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findFirstByUsername(String username);
//    @Query("SELECT u FROM User u JOIN FETCH u.groups WHERE u.id = :userId")
//    User findByIdWithGroups(@Param("userId") Integer userId);
//    @EntityGraph(attributePaths = "groups")
//    Optional<User> findByIdWithGroups(Integer id);
}
