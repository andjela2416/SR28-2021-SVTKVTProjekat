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
import rs.ac.uns.ftn.svtvezbe07.model.entity.Banned;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.BannedRepository;
import rs.ac.uns.ftn.svtvezbe07.service.BannedService;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;

@Service
public class BannedServiceImpl implements BannedService{
	@Autowired
    private BannedRepository BannedRepository;

 	private static final Logger logger = LogManager.getLogger(Log4jExample.class);
	
 	private PostService postService;

    @Override
    public List<Banned> getAll() {
        return this.BannedRepository.findAll();
    }
    

	public Banned save(Banned Banned) {
		logger.info("Pozvan save za banned");
		return this.BannedRepository.save(Banned);
	}


//	@Override
//	public List<Banned> findAllByUser(User user) {
//	    return BannedRepository.findAllByUser(user);
//	}
	

	
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

    public Banned findBanned(Long id) {
        Optional<Banned> Banned = BannedRepository.findById(id);
        if(!Banned.isEmpty()){
            return Banned.get();
        }

        return null;
    }
    
    public Banned findBannedById(Long id) {
        Banned Banned = BannedRepository.findBannedById(id);
        if(Banned!=null){
            return Banned;
        }

        return null;
    }
    @Override
    public void delete(Long idInt) {
    	Banned b=BannedRepository.findBannedById(idInt);
    	//b.setDeleted=true;
        this.BannedRepository.save(b);
    }

	@Override
	public Banned createBanned(Banned newBanned) {
		 Banned Banned = new Banned();
	        Banned.setByAdmin(newBanned.getByAdmin());
	        Banned.setByGroupAdmin(newBanned.getByGroupAdmin());
	        Banned.setGroup(newBanned.getGroup());
	        Banned.setTowards(newBanned.getTowards());
	        //Banned.setBanned(newBanned.getGroupRequest());
//	        GroupRequest.setComment(newGroupRequest.getComment());
	        //Post p =postService.findPost(l);
	        //GroupRequest.setPost(l);
//	        GroupRequest.setUser(newGroupRequest.getUser());
	        Banned = BannedRepository.save(Banned);
	        return Banned;
	}


//	@Override
//	public List<GroupRequest> findAllByUser(User user) {
//		// TODO Auto-generated method stub
//		return this.GroupRequestRepository.findGroupRequestByuser_id(user);
//	}





}
