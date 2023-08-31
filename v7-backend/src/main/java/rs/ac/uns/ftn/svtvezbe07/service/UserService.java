package rs.ac.uns.ftn.svtvezbe07.service;

import rs.ac.uns.ftn.svtvezbe07.model.dto.UserDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    User findByUsername(String username) ;

    User createUser(UserDTO userDTO);

    List<User> findAll();

    User save (User user);
    
    public Set<Group> getUserGroups(Integer userId)
;

}

