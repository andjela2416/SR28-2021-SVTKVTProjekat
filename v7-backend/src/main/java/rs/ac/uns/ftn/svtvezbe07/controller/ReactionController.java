package rs.ac.uns.ftn.svtvezbe07.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.svtvezbe07.model.dto.CommentDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.ReactionDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.ReactionType;
import rs.ac.uns.ftn.svtvezbe07.repository.ImageRepository;
import rs.ac.uns.ftn.svtvezbe07.service.CommentService;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
import rs.ac.uns.ftn.svtvezbe07.service.ReactionService;
import rs.ac.uns.ftn.svtvezbe07.service.UserService;


@RestController
@RequestMapping("api/reactions")
@CrossOrigin(origins = "http://4200", maxAge = 3600)
public class ReactionController {
	@Autowired
    ReactionService reactionService;
 	
 	@Autowired
    PostService postService;
 	
 	@Autowired
    CommentService commentService;
 	
 	@Autowired
 	private UserController userController;

 	private static final Logger logger = LogManager.getLogger(Log4jExample.class);
 	@Autowired
    UserService userService;
 		
 	@Autowired
 	private ImageRepository imageRepository;

 	@Transactional
 	public void deleteImage(Long imageId) {
 	    imageRepository.deleteById(imageId);
 	}


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reaction>> getAll(){
    	
        return new ResponseEntity<>(reactionService.getAll(), HttpStatus.OK);
    }
    
//    @GetMapping("/all/user")
//    //@PreAuthorize("hasRole('USER')")
//    //@CrossOrigin(origins = "http://localhost:4200")
//    public ResponseEntity<List<Reaction>> getAllReactions(){
//    	 rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
//    	    if (currentUser != null) {
//    	        List<Reaction> posts = reactionService.findAllByUser(currentUser);
//    	        return new ResponseEntity<>(posts, HttpStatus.OK);
//    	    } else {
//    	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    	    }
//    }
    

//    @GetMapping("/all/post")
//    //@PreAuthorize("hasRole('USER')")
//    //@CrossOrigin(origins = "http://localhost:4200")
//    public ResponseEntity<List<Reaction>> getAllPostReactions(@RequestParam Long id){
//    	Post post=postService.findPost(id);
//    	List<Reaction> posts = reactionService.findAllByPost(post);
//    	return new ResponseEntity<>(posts, HttpStatus.OK);
//
//    }

   /* @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.postService.findAll();
    }
*/

    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Reaction> getOne(@PathVariable Long id) throws Exception{
    	//throw new Exception("vgf");
    	//Long id2 = Long.parseLong(id);
    	//int id3 =Integer.parseInt(id);
       Reaction post = reactionService.findReaction(id);
        if(post==null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        logger.info("Vracena reakcija");
        return new ResponseEntity<>(post, HttpStatus.OK);
        
//        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
//    	
//    	 if (currentUser != null) {
//    	        List<Post> posts = postService.findAllByUserId(currentUser);
//    	        Post post = postService.findPost(id); 
//    	       
//    	        if(post!=null){
//    	        	 for ( Post p : posts) {
// 	    	        	if (p.equals(post))
// 	    	        	{
// 	    	        		return new ResponseEntity<>(post, HttpStatus.OK);
// 	    	           }
//    	             }
//    	             return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//    	         } else {
//    	             return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//    	         }
//    	     } else {
//    	         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    	     }
    }
    
 
	@PostMapping("/create")
    public ResponseEntity<Reaction> create(@RequestBody @Validated ReactionDTO newR) throws Exception{
		
		 	rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
		 	Long l = newR.getPost();
	        Long lo = newR.getComment();
	        
	        ReactionType newReactionType = newR.getType();
	        List<Reaction> postReactions = new ArrayList<>();
	        
	        if (l != null) {
	            Post targetPost = postService.findPost(l);
	            postReactions.addAll(targetPost.getReactions());
	        }
	        
	        if (lo != null) {
	            Comment targetComment = commentService.findComment(lo);
	            postReactions.addAll(targetComment.getReactions());
	        }
	        
	        boolean hasExistingReaction = postReactions.stream()
	            .anyMatch(reaction -> reactionMatchesTypeAndUser(reaction, newReactionType, currentUser));

	        if (hasExistingReaction) {
	           
	            return new ResponseEntity<>(null,HttpStatus.OK);
	        }
	        
	        
		 	Reaction post = new Reaction();
		 	post.setTimestamp(LocalDateTime.now());
	     
	        post.setUser(currentUser);
	        post.setType(newR.getType());
	        
	        if(lo!=null) {
	        	Comment c= commentService.findComment(lo);
	        	post.setComment(c);
	        }

	        
	    	if(l!=null) {
		 		Post p= postService.findPost(l);
		 		post.setPost(p);
		 	}
	        
	    Reaction createdReac = reactionService.save(post);//postService.createPost(newPost);
        if(createdReac == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        if(l!=null) {
        	Post azuriraj = postService.findPost(l);
            azuriraj.getReactions().add(createdReac);
            if(createdReac.getType().equals(ReactionType.LIKE)) {
            	azuriraj.setLikes(azuriraj.getLikes() + 1);
            }else if(createdReac.getType().equals(ReactionType.DISLIKE)) {
            	azuriraj.setDislikes(azuriraj.getDislikes() + 1);
            }else if(createdReac.getType().equals(ReactionType.HEART)) {
            	azuriraj.setHearts(azuriraj.getHearts() + 1);
            }
            postService.save(azuriraj);
        }
        if(lo!=null) {
        	Comment azuriraj = commentService.findComment(lo);
            azuriraj.getReactions().add(createdReac);
            if(createdReac.getType().equals(ReactionType.LIKE)) {
            	azuriraj.setLikes(azuriraj.getLikes() + 1);
            }else if(createdReac.getType().equals(ReactionType.DISLIKE)) {
            	azuriraj.setDislikes(azuriraj.getDislikes() + 1);
            }else if(createdReac.getType().equals(ReactionType.HEART)) {
            	azuriraj.setHearts(azuriraj.getHearts() + 1);
            }
            commentService.save(azuriraj);
        }
        
        Reaction r=reactionService.findReactionByTimestamp(post.getTimestamp());
        logger.info("Napravljena reakcija");
        return new ResponseEntity<>(r, HttpStatus.CREATED);
    }

	private boolean reactionMatchesTypeAndUser(Reaction reaction, ReactionType type,rs.ac.uns.ftn.svtvezbe07.model.entity.User user) {
	    return reaction.getType() == type && reaction.getUser() != null && reaction.getUser().getId().equals(user.getId());
	}
	
	
	@DeleteMapping("/delete")
	@Transactional
	@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
	//@CrossOrigin()
	public ResponseEntity<Reaction> delete(@RequestParam("id") Long id) {
		Reaction r = reactionService.findReaction(id);
		Post l = r.getPost();
		if(l!=null) {
            l.getReactions().remove(r);
            if(r.getType().equals(ReactionType.LIKE)) {
            	l.setLikes(l.getLikes() - 1);
            }else if(r.getType().equals(ReactionType.DISLIKE)) {
            	l.setDislikes(l.getDislikes() - 1);
            }else if(r.getType().equals(ReactionType.HEART)) {
            	l.setHearts(l.getHearts() - 1);
            }
            postService.save(l);
        }
        Comment lo = r.getComment();
        if(lo!=null) {
            lo.getReactions().remove(r);
            if(r.getType().equals(ReactionType.LIKE)) {
            	lo.setLikes(lo.getLikes() - 1);
            }else if(r.getType().equals(ReactionType.DISLIKE)) {
            	lo.setDislikes(lo.getDislikes() - 1);
            }else if(r.getType().equals(ReactionType.HEART)) {
            	lo.setHearts(lo.getHearts() - 1);
            }
            commentService.save(lo);
        }
		
	    r.setDeleted(true);
	    reactionService.save(r);
	    logger.info("Obrisana reakcija");
	    return new ResponseEntity<>(r, HttpStatus.OK);
	}
                                      
	@PutMapping("/edit")
	@CrossOrigin(origins = "http://4200", maxAge = 3600)
	@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
	public ResponseEntity<Reaction> edit(@RequestBody ReactionDTO editPost) throws Exception {
//	    try {
	    	rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
	     	  
	        Reaction edit = reactionService.findReaction(editPost.getId());
	        if(edit==null) {
	        	throw new Exception("d");
	        }
	        //Long l = editPost.getPost();
	        //Post p =postService.findPost(l);
	        edit.setType(editPost.getType());
	        edit.setTimestamp(LocalDateTime.now());
//	        edit.setUser(currentUser);
  
	        reactionService.save(edit);

	        Reaction c=reactionService.findReactionByTimestamp(edit.getTimestamp());
	        return new ResponseEntity<>(c, HttpStatus.OK);
//	    } catch (Exception e) {
//	    	logger.info("error: "+e.getMessage());
//		    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	    }
	}
	


}
