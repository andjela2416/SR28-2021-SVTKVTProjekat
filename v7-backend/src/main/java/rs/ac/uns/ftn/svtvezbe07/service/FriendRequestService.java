package rs.ac.uns.ftn.svtvezbe07.service;

import java.time.LocalDateTime;
import java.util.List;

import rs.ac.uns.ftn.svtvezbe07.model.dto.FriendRequestDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.FriendRequest;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.model.entity.FriendRequest;

public interface FriendRequestService {
	List<FriendRequest> getAll();
    
	FriendRequest save (FriendRequest post);

  	FriendRequest findFriendRequestById(Long id);

  	FriendRequest findFriendRequest(Long id);

  	FriendRequest createFriendRequest(FriendRequestDTO newPost);
	
    void delete(Long id);

//    List<FriendRequest> findAllByUser(User user) ;
    
    List<FriendRequest> findAllByToWho(User user) ;
    
    List<FriendRequest> findAllByFromWho(User user) ;
}
