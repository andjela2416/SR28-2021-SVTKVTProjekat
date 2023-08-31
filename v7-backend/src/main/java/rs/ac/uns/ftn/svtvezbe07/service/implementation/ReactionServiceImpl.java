package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.ReactionRepository;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
import rs.ac.uns.ftn.svtvezbe07.service.ReactionService;
@Service
public class ReactionServiceImpl implements ReactionService{
	@Autowired
    private ReactionRepository reactionRepository;

 	private PostService postService;

    @Override
    public List<Reaction> getAll() {
        return this.reactionRepository.findAll();
    }
    

	public Reaction save(Reaction reaction) {
		return this.reactionRepository.save(reaction);
	}


//	@Override
//	public List<Reaction> findAllByUser(User user) {
//	    return reactionRepository.findAllByUser(user);
//	}
	

//	@Override
//	public List<Reaction> findAllByPost(Post user) {
//	    return reactionRepository.findAllByPost(user);
//	}


	
	/*public Optional<Reaction> getByIdInt(Integer id) {
		return ReactionRepository.findReactionById(id);
	}*/
	
  public Reaction findReactionByTimestamp(LocalDateTime content) {
  Optional<Reaction> reaction = reactionRepository.findReactionByTimestamp(content);
  if(!reaction.isEmpty()){
      return reaction.get();
  }

  return null;
}

//    public Reaction findReactionByType(String content) {
//        Optional<Reaction> reaction = reactionRepository.findReactionByType(content);
//        if(!reaction.isEmpty()){
//            return reaction.get();
//        }
//
//        return null;
//    }

    public Reaction findReaction(Long id) {
        Optional<Reaction> reaction = reactionRepository.findById(id);
        if(!reaction.isEmpty()){
            return reaction.get();
        }

        return null;
    }
    
    public Reaction findReactionById(Integer id) {
        Reaction reaction = reactionRepository.findReactionById(id);
        if(reaction!=null){
            return reaction;
        }

        return null;
    }
    @Override
    public void delete(Long idInt) {
        this.reactionRepository.deleteById(idInt);
    }

	@Override
	public Reaction createReaction(rs.ac.uns.ftn.svtvezbe07.model.dto.ReactionDTO newReaction) {
		 Reaction Reaction = new Reaction();
	        Reaction.setType(newReaction.getType());
	        Reaction.setTimestamp(LocalDateTime.now());
	        //Reaction.setReaction(newReaction.getReaction());
//	        Reaction.setComment(newReaction.getComment());
	        Long l = newReaction.getPost();
	        //Post p =postService.findPost(l);
	        //Reaction.setPost(l);
//	        Reaction.setUser(newReaction.getUser());
	        Reaction = reactionRepository.save(Reaction);
	        return Reaction;
	}

}
