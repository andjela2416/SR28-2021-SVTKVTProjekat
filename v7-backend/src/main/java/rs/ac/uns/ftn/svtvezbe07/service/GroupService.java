package rs.ac.uns.ftn.svtvezbe07.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import rs.ac.uns.ftn.svtvezbe07.model.dto.GroupDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public interface GroupService {

	List<Group> getAll();
    
    Group save (Group g);

	Group findByGroup(Group g);

	Group findGroup(Long id);

	Group createGroup(GroupDTO newGroup);
	
    Group findGroupByName(String content);

    void delete(Long id);
    
    Group suspend(Group g);

    List<Group> findAllByUser(User user) ;
    
    Set<Post> getGroupPosts(Long id);
    
}
