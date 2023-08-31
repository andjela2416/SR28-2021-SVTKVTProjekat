package rs.ac.uns.ftn.svtvezbe07.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.ReactionType;
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
    @GetMapping("/user/comments/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'GROUPADMIN')")
    public ResponseEntity<List<Comment>> getUserCommentsAndRepliesForPost(@PathVariable Long postId) {
        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());

        Post post = postService.findPost(postId);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Comment> allComms= commentService.getAll();
        
        List<Comment> userCommentsAndRepliesForPost = new ArrayList<>();

        for (Comment comment : post.getComments()) {
            if (comment.getUserId().equals(currentUser)) {
                userCommentsAndRepliesForPost.add(comment);
                List<Comment> userReplies = getUserRepliesForPost(comment);
                userCommentsAndRepliesForPost.addAll(userReplies);
            }
        }

        return new ResponseEntity<>(userCommentsAndRepliesForPost, HttpStatus.OK);
    }

    public List<Comment> getUserRepliesForPost(Comment parentComment) {
        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
        logger.info("gyyyyzh");
        List<Comment> userRepliesForPost = new ArrayList<>();

        for (Comment reply : parentComment.getRepliesComment()) {
            if (reply.getUserId().equals(currentUser)) {
            	logger.info("gyyyh");
                userRepliesForPost.add(reply);
                List<Comment> userReplies = getUserRepliesForPost(reply);
                userRepliesForPost.addAll(userReplies);
            }
        }

        return userRepliesForPost;
    }



    @GetMapping("/all/post")
    //@PreAuthorize("hasRole('USER')")
    //@CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Set<Comment>> getAllPostComments(@RequestParam Long id){
    	Post post=postService.findPost(id);
    	Set<Comment> posts = post.getComments();
    	return new ResponseEntity<>(posts, HttpStatus.OK);

    }
    
    @GetMapping("/commentReplies")
    //@PreAuthorize("hasRole('USER')")
    //@CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Set<Comment>> getAllCommentReplies(@RequestParam Long id){
    	Comment com=commentService.findComment(id);
    	Set<Comment> replies = com.getRepliesComment();
    	return new ResponseEntity<>(replies, HttpStatus.OK);

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
	        post.setText(newPost.getText());
	        post.setTimestamp(LocalDateTime.now());
	        post.setPost(po);
	       // Long id = Long.valueOf(currentUser.getId());
	        post.setUserId(currentUser);
	        post.setDeleted(false);
	        post.setLikes(0);
	        post.setDislikes(0);
	        post.setHearts(0);
	        post.setShowReplies(false);
	        
	        
	    Comment createdPost = commentService.save(post);//postService.createPost(newPost);
        if(createdPost == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        po.getComments().add(createdPost);
        postService.save(po);
        
        Comment c=commentService.findCommentByText(post.getText());

        return new ResponseEntity<>(c, HttpStatus.CREATED);
    }

	@DeleteMapping("/delete")
	@Transactional
	@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
	//@CrossOrigin()
	public ResponseEntity<Comment> delete(@RequestParam("id") Long id) {
		Comment comm = commentService.findComment(id);
		
		Post p=comm.getPost();
		if(p!=null) {
			p.getComments().remove(comm);
		    postService.save(p);
		}
		
		List<Comment> all= commentService.getAll();
		for (Comment c:all) {
			Set<Comment> replies=c.getRepliesComment();
			for (Comment r:replies) {
				if(r.getId().equals(id)) {
					c.getRepliesComment().remove(comm);
					commentService.save(comm);
					postService.save(p);
				}
			}
		}
	    
	    comm.setDeleted(true);    
	    commentService.save(comm);
	    
	    return new ResponseEntity<>(comm, HttpStatus.OK);
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

	        Comment c=commentService.findComment(edit.getId());
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
	        //Post p =postService.findPost(l);
	        Comment reply = new Comment();
	        reply.setText(replyDTO.getText());
	        reply.setTimestamp(LocalDateTime.now());
	        reply.setDeleted(false);
	        //reply.setPost(p);
	        reply.setUserId(currentUser);
	        reply.setLikes(0);
	        reply.setDislikes(0);
	        reply.setHearts(0);

	        // Dodavanje odgovora parent komentaru
	        parentComment.getRepliesComment().add(reply);
	        commentService.save(reply);
	        commentService.save(parentComment);

	        return new ResponseEntity<>(reply, HttpStatus.OK);
	 
	}

	@GetMapping("/sort")
	public ResponseEntity<List<Comment>> getComments(@RequestParam("sort") String sort, @RequestParam("id") Long id) {
		Post post=postService.findPost(id);
		Set<Comment> commentsList = post.getComments();
		List<Comment> comments = new ArrayList<>(commentsList);
	    logger.info("Sortiranje "+sort);
	    if (sort.equals("likesR")) {
	        comments.sort(Comparator.comparingInt(comment -> countReactions(comment.getReactions(), ReactionType.LIKE)));
	    } else if (sort.equals("dislikesR")) {
	        comments.sort(Comparator.comparingInt(comment -> countReactions(comment.getReactions(), ReactionType.DISLIKE)));
	    } else if (sort.equals("heartsR")) {
	    	logger.info(sort+"u uslovu je");
	        comments.sort(Comparator.comparingInt(comment -> countReactions(comment.getReactions(), ReactionType.HEART)));
	    } else if (sort.equals("likesO")) {
	        comments.sort(Comparator.comparingInt(comment -> countReactions(((Comment) comment).getReactions(), ReactionType.LIKE)).reversed());
	    } else if (sort.equals("dislikesO")) {
	        comments.sort(Comparator.comparingInt(comment -> countReactions(((Comment) comment).getReactions(), ReactionType.DISLIKE)).reversed());
	    } else if (sort.equals("heartsO")) {
	        comments.sort(Comparator.comparingInt(comment -> countReactions(((Comment) comment).getReactions(), ReactionType.HEART)).reversed());
	    } else if (sort.equals("new")) {
	        comments.sort(Comparator.comparing(Comment::getTimestamp).reversed());
	    } else if (sort.equals("old")) {
	        comments.sort(Comparator.comparing(Comment::getTimestamp));
	    } else {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	    return new ResponseEntity<>(comments, HttpStatus.OK);
	}
	public int countReactions(Set<Reaction> reactions, ReactionType reactionType) {
	    int count = 0;
	    for (Reaction reaction : reactions) {
	        if (reaction.getType().equals(reactionType)) {
	            count++;
	        }
	    }
	    return count;
	}
	

	public int countLikes(Set<Reaction> reactions) {
	    int count = 0;
	    for (Reaction reaction : reactions) {
	        if (reaction.getType().equals("like")) {
	            count++;
	        }
	    }
	    return count;
	}

	public int countDislikes(Set<Reaction> reactions) {
	    int count = 0;
	    for (Reaction reaction : reactions) {
	        if (reaction.getType().equals("dislike")) {
	            count++;
	        }
	    }
	    return count;
	}

	public int countHearts(Set<Reaction> reactions) {
	    int count = 0;
	    for (Reaction reaction : reactions) {
	        if (reaction.getType().equals("heart")) {
	            count++;
	        }
	    }
	    return count;
	}
	@GetMapping("/comment-reaction-count")
	public ResponseEntity<Map<String, Integer>> getCommentReactionCounts(@RequestParam("id") Long id) {
	    Comment comment = commentService.findComment(id);

	    Map<String, Integer> reactionCounts = new HashMap<>();
	    int commentLikes = countLikes(comment.getReactions());
	    int commentDislikes = countDislikes(comment.getReactions());
	    int commentHearts = countHearts(comment.getReactions());

	    reactionCounts.put("commentLikes", commentLikes);
	    reactionCounts.put("commentDislikes", commentDislikes);
	    reactionCounts.put("commentHearts", commentHearts);

	    return new ResponseEntity<>(reactionCounts, HttpStatus.OK);
	}


	
}
