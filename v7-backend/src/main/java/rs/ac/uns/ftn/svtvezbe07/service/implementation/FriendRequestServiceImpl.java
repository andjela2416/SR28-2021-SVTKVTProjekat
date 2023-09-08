package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe07.controller.Log4jExample;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.FriendRequest;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.FriendRequestRepository;
import rs.ac.uns.ftn.svtvezbe07.service.FriendRequestService;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
@Service
public class FriendRequestServiceImpl implements FriendRequestService{
	@Autowired
    private FriendRequestRepository FriendRequestRepository;

 	private static final Logger logger = LogManager.getLogger(Log4jExample.class);
	
 	private PostService postService;

    @Override
    public List<FriendRequest> getAll() {
        return this.FriendRequestRepository.findAll();
    }
    

	public FriendRequest save(FriendRequest FriendRequest) {
		logger.info("pozv save");
		return this.FriendRequestRepository.save(FriendRequest);
	}


//	@Override
//	public List<FriendRequest> findAllByUser(User user) {
//	    return FriendRequestRepository.findAllByUser(user);
//	}
	

	@Override
	public List<FriendRequest> findAllByToWho(User g) {
	    return FriendRequestRepository.findAllByToWho(g);
	}


	
	/*public Optional<GroupRequest> getByIdInt(Integer id) {
		return GroupRequestRepository.findGroupRequestById(id);
	}*/
	
//  public GroupRequest findGroupRequestByTimestamp(LocalDateTime content) {
//  Optional<GroupRequest> GroupRequest = GroupRequestRepository.findGroupRequestByTimestamp(content);
//  if(!GroupRequest.isEmpty()){
//      return GroupRequest.get();
//  }
//
//  return null;
//}

//    public GroupRequest findGroupRequestByType(String content) {
//        Optional<GroupRequest> GroupRequest = GroupRequestRepository.findGroupRequestByType(content);
//        if(!GroupRequest.isEmpty()){
//            return GroupRequest.get();
//        }
//
//        return null;
//    }

    public FriendRequest findFriendRequest(Long id) {
        Optional<FriendRequest> FriendRequest = FriendRequestRepository.findById(id);
        if(!FriendRequest.isEmpty()){
            return FriendRequest.get();
        }

        return null;
    }
    
    public FriendRequest findFriendRequestById(Long id) {
        FriendRequest FriendRequest = FriendRequestRepository.findFriendRequestById(id);
        if(FriendRequest!=null){
            return FriendRequest;
        }

        return null;
    }
    @Override
    public void delete(Long idInt) {
        this.FriendRequestRepository.deleteById(idInt);
    }

	@Override
	public FriendRequest createFriendRequest(rs.ac.uns.ftn.svtvezbe07.model.dto.FriendRequestDTO newFriendRequest) {
		 FriendRequest FriendRequest = new FriendRequest();
	        FriendRequest.setCreatedAt(LocalDateTime.now());
	        FriendRequest.setApproved(false);
	        //FriendRequest.setFriendRequest(newFriendRequest.getGroupRequest());
//	        GroupRequest.setComment(newGroupRequest.getComment());
	        //Post p =postService.findPost(l);
	        //GroupRequest.setPost(l);
//	        GroupRequest.setUser(newGroupRequest.getUser());
	        FriendRequest = FriendRequestRepository.save(FriendRequest);
	        return FriendRequest;
	}


//	@Override
//	public List<GroupRequest> findAllByUser(User user) {
//		// TODO Auto-generated method stub
//		return this.GroupRequestRepository.findGroupRequestByuser_id(user);
//	}





}
