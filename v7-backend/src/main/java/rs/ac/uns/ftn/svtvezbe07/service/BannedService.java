package rs.ac.uns.ftn.svtvezbe07.service;

import java.util.List;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Banned;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public interface BannedService {
	List<Banned> getAll();
    
	Banned save (Banned post);

  	Banned findBannedById(Long id);

  	Banned createBanned(Banned newPost);
	
    void delete(Long id);

//    List<Banned> findAllByUser(User user) ;
   
}
