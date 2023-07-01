package rs.ac.uns.ftn.svtvezbe07.controller;

import java.time.LocalDateTime;
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
import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.repository.ImageRepository;
import rs.ac.uns.ftn.svtvezbe07.service.CommentService;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
import rs.ac.uns.ftn.svtvezbe07.service.UserService;

@RestController
@RequestMapping("api/comments")
@CrossOrigin(origins = "http://4200", maxAge = 3600)
public class CommentController {
 	@Autowired
    CommentService commentService;
 	
 	@Autowired
    PostService postService;
 	
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
    public ResponseEntity<List<Comment>> getAll(){
    	
        return new ResponseEntity<>(commentService.getAll(), HttpStatus.OK);
    }
    
    @GetMapping("/all/user")
    //@PreAuthorize("hasRole('USER')")
    //@CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Comment>> getAllComments(){
    	 rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
    	    if (currentUser != null) {
    	        List<Comment> posts = commentService.findAllByUserId(currentUser);
    	        return new ResponseEntity<>(posts, HttpStatus.OK);
    	    } else {
    	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    	    }
    }
    

    @GetMapping("/all/post")
    //@PreAuthorize("hasRole('USER')")
    //@CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Comment>> getAllPostComments(@RequestParam Long id){
    	Post post=postService.findPost(id);
    	List<Comment> posts = commentService.findAllByPost(post);
    	return new ResponseEntity<>(posts, HttpStatus.OK);

    }

   /* @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.postService.findAll();
    }
*/

    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getOne(@PathVariable Long id) throws Exception{
    	//throw new Exception("vgf");
    	//Long id2 = Long.parseLong(id);
    	//int id3 =Integer.parseInt(id);
       Comment post = commentService.findComment(id);
        if(post==null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<Comment> create(@RequestBody @Validated CommentDTO newPost) throws Exception{
//			if (newPost!=null) {
//				throw new Exception("vg");
//			}
//			if (newPost==null) {
//				throw new Exception("gv");
//			}
		Long p=newPost.getPost();
		
		logger.info("pozvano");
		
			Post po = postService.findPost(p);
		 	rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
   	  
    	 	Comment post = new Comment();
    	 	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        User u = (User) auth.getPrincipal();
	        rs.ac.uns.ftn.svtvezbe07.model.entity.User user= userService.findByUsername(u.getUsername());
	        post.setText(newPost.getText());
	        post.setTimestamp(LocalDateTime.now());
	        post.setPost(po);
	       // Long id = Long.valueOf(currentUser.getId());
	        post.setUserId(currentUser);
	        post.setDeleted(false);
	        

	        
	    Comment createdPost = commentService.save(post);//postService.createPost(newPost);
        if(createdPost == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        Comment c=commentService.findCommentByText(post.getText());

        return new ResponseEntity<>(c, HttpStatus.CREATED);
    }

	@DeleteMapping("/delete")
	@Transactional
	@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
	//@CrossOrigin()
	public ResponseEntity<Comment> delete(@RequestParam("id") Long id) {
	    Comment post = commentService.findComment(id);
	   // post.setDeleted(true);
	    commentService.delete(id);
	    return new ResponseEntity<>(post, HttpStatus.OK);
	}
                                      
	@PutMapping("/edit")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
	public ResponseEntity<Comment> edit(@RequestBody CommentDTO editPost) throws Exception {
//	    try {
	    	rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
	     	  
	        Comment edit = commentService.findComment(editPost.getId());
	        if(edit==null) {
	        	throw new Exception("d");
	        }
	        //Long l = editPost.getPost();
	        //Post p =postService.findPost(l);
	        if (!editPost.getReplies().isEmpty()) {
	            Set<Comment> images = new HashSet<>();
	            for (Comment imageDTO : editPost.getReplies()) {
	             
	                    Comment image = new Comment();
	                    image.setTimestamp(imageDTO.getTimestamp());
	                   // image.setUserId(currentUser);//imageDTO.getUserId());
	                    image.setPost(edit.getPost());
	                    image.setDeleted(imageDTO.isDeleted());
	                    image.setText(imageDTO.getText());
	                        images.add(image);
	
	                
	            }
	            edit.setRepliesComment(images); // Dodajemo nove slike
	        }
	        edit.setText(editPost.getText());
	        edit.setTimestamp(LocalDateTime.now());
	        edit.setUserId(currentUser);
	        edit.setDeleted(editPost.isDeleted());
  
	        commentService.save(edit);

	        Comment c=commentService.findCommentByText(edit.getText());
	        return new ResponseEntity<>(c, HttpStatus.OK);
//	    } catch (Exception e) {
//	    	logger.info("error: "+e.getMessage());
//		    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	    }
	}
	
	@PostMapping("/{commentId}/reply")
	@CrossOrigin(origins = "http://4200")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'GROUPADMIN')")
	public ResponseEntity<Comment> addReplyToComment(@PathVariable Long commentId, @RequestBody CommentDTO replyDTO) throws Exception {
	    
	        Comment parentComment = commentService.findComment(commentId);
	        if (parentComment == null) {
	            throw new Exception("Parent comment not found");
	        }

	        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
	        Long l = replyDTO.getPost();
	        Post p =postService.findPost(l);
	        Comment reply = new Comment();
	        reply.setText(replyDTO.getText());
	        reply.setTimestamp(LocalDateTime.now());
	        reply.setDeleted(false);
	        reply.setPost(p);
	        reply.setUserId(currentUser);

	        // Dodavanje odgovora parent komentaru
	        parentComment.getRepliesComment().add(reply);
	        commentService.save(reply);
	        commentService.save(parentComment);

	        return new ResponseEntity<>(reply, HttpStatus.OK);
	 
	}

	
}
