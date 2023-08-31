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
import rs.ac.uns.ftn.svtvezbe07.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.GroupRequestRepository;
import rs.ac.uns.ftn.svtvezbe07.service.GroupRequestService;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
@Service
public class GroupRequestServiceImpl implements GroupRequestService{
	@Autowired
    private GroupRequestRepository GroupRequestRepository;

 	private static final Logger logger = LogManager.getLogger(Log4jExample.class);
	
 	private PostService postService;

    @Override
    public List<GroupRequest> getAll() {
        return this.GroupRequestRepository.findAll();
    }
    

	public GroupRequest save(GroupRequest GroupRequest) {
		logger.info("pozv save");
		return this.GroupRequestRepository.save(GroupRequest);
	}


//	@Override
//	public List<GroupRequest> findAllByUser(User user) {
//	    return GroupRequestRepository.findAllByUser(user);
//	}
	

	@Override
	public List<GroupRequest> findAllByGroup(Long g) {
	    return GroupRequestRepository.findAllByGroupId(g);
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

    public GroupRequest findGroupRequest(Long id) {
        Optional<GroupRequest> GroupRequest = GroupRequestRepository.findById(id);
        if(!GroupRequest.isEmpty()){
            return GroupRequest.get();
        }

        return null;
    }
    
    public GroupRequest findGroupRequestById(Long id) {
        GroupRequest GroupRequest = GroupRequestRepository.findGroupRequestById(id);
        if(GroupRequest!=null){
            return GroupRequest;
        }

        return null;
    }
    @Override
    public void delete(Long idInt) {
        this.GroupRequestRepository.deleteById(idInt);
    }

	@Override
	public GroupRequest createGroupRequest(rs.ac.uns.ftn.svtvezbe07.model.dto.GroupRequestDTO newGroupRequest) {
		 GroupRequest GroupRequest = new GroupRequest();
	        GroupRequest.setCreatedAt(LocalDateTime.now());
	        GroupRequest.setApproved(false);
	        //GroupRequest.setGroupRequest(newGroupRequest.getGroupRequest());
//	        GroupRequest.setComment(newGroupRequest.getComment());
	        //Post p =postService.findPost(l);
	        //GroupRequest.setPost(l);
//	        GroupRequest.setUser(newGroupRequest.getUser());
	        GroupRequest = GroupRequestRepository.save(GroupRequest);
	        return GroupRequest;
	}


//	@Override
//	public List<GroupRequest> findAllByUser(User user) {
//		// TODO Auto-generated method stub
//		return this.GroupRequestRepository.findGroupRequestByuser_id(user);
//	}





}
