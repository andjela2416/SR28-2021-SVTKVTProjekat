package rs.ac.uns.ftn.svtvezbe07.service;

import java.time.LocalDateTime;
import java.util.List;

import rs.ac.uns.ftn.svtvezbe07.model.dto.GroupRequestDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.model.entity.GroupRequest;

public interface GroupRequestService {
	List<GroupRequest> getAll();
    
	GroupRequest save (GroupRequest post);

  	GroupRequest findGroupRequestById(Long id);

  	GroupRequest findGroupRequest(Long id);

  	GroupRequest createGroupRequest(GroupRequestDTO newPost);
	
    void delete(Long id);

//    List<GroupRequest> findAllByUser(User user) ;


    
    List<GroupRequest> findAllByGroup(Long user) ;
}
