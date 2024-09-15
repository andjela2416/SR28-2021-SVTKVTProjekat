package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe07.controller.Log4jExample;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.GroupRepository;
import rs.ac.uns.ftn.svtvezbe07.repository.UserRepository;
import rs.ac.uns.ftn.svtvezbe07.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

 	@Autowired
    private GroupRepository groupRepository;


	@Autowired
    private UserRepository userRepository;


    @Override
    public List<Group> getAll() {
        return this.groupRepository.findAll();
    }
	private static final Logger logger = LogManager.getLogger(Log4jExample.class);

	public Group save(Group g) {
		return groupRepository.save(g);
	}
	public Set<Post> getGroupPosts(Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group != null) {
            return group.getPosts();
        }
        return null;
    }


	@Override
	public List<Group> findAllByUser(User user) {
	    return groupRepository.findAllByGroupAdmin(user);
	}

	
	/*public Optional<Group> getByIdInt(Integer id) {
		return GroupRepository.findGroupById(id);
	}*/
	


    @Override
    public Group findGroupByName(String content) {
        Optional<Group> Group = groupRepository.findGroupByName(content);
        if(!Group.isEmpty()){
            return Group.get();
        }

        return null;
    }

    public Group findGroup(Long id) {
        Optional<Group> Group = groupRepository.findById(id);
        if(!Group.isEmpty()){
            return Group.get();
        }

        return null;
    }
    
    public Group findByGroup(Group g) {
        Group Group = groupRepository.findGroupByGroup(g);
        if(Group!=null){
            return Group;
        }

        return null;
    }
    @Override
    public void delete(Long idInt) {
    	
    	 Optional<Group> optionalGroup = groupRepository.findById(idInt);
    	   
    	   if (optionalGroup.isPresent()) {
    		  // userRepository.deleteFromGroups(idInt);
    	       Group g= groupRepository.findGroupById(idInt);
    	       g.setDeleted(true);
    	       groupRepository.save(g);
    	       //groupRepository.deleteById(idInt);
    	       logger.info("Grupa uspešno obrisana."+g.isDeleted());
    	   } else {
    	       logger.info("Grupa sa ID-em " + idInt + " ne postoji.");
    	   }
    	   
    	 List<Group> groups= groupRepository.findAll();
    	 for (Group g : groups) {
    		 logger.info("Grupaa"+g.isDeleted());
    	 }
    }
    
    public Group suspend(Group g) {
    	Long l=Long.valueOf(g.getId());
        Group groupOptional = groupRepository.findGroupById(l);
        logger.info("LOLA11"+g.getSuspendedReason());
        if (groupOptional!=null) {
        	groupOptional.setSuspended(true);
        	groupOptional.setSuspendedReason(g.getSuspendedReason());
            Group updatedGroup = groupRepository.save(groupOptional);
            return updatedGroup;
        }
        
        return null;
    }


	@Override
	public Group createGroup(rs.ac.uns.ftn.svtvezbe07.model.dto.GroupDTO newGroup) {
		 Group Group = new Group();
	        Group.setDescription(newGroup.getDescription());
	        Group.setCreationDate(LocalDateTime.now());
	        Group.setName(newGroup.getName());
	        Group.setDeleted(false);
	        Group.setSuspended(false);
	        Group = groupRepository.save(Group);
	        return Group;
	}

}
