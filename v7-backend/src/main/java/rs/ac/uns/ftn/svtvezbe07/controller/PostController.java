package rs.ac.uns.ftn.svtvezbe07.controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

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

import rs.ac.uns.ftn.svtvezbe07.model.dto.PostDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe07.model.entity.ReactionType;
import rs.ac.uns.ftn.svtvezbe07.repository.CommentRepository;
import rs.ac.uns.ftn.svtvezbe07.repository.ImageRepository;
import rs.ac.uns.ftn.svtvezbe07.repository.ReactionRepository;
import rs.ac.uns.ftn.svtvezbe07.service.GroupService;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
import rs.ac.uns.ftn.svtvezbe07.service.ReactionService;
import rs.ac.uns.ftn.svtvezbe07.service.UserService;
@RestController
@RequestMapping("api/posts")
@CrossOrigin(origins = "http://4200", maxAge = 3600)
public class PostController {
	 	@Autowired
	    PostService postService;
	 	
	 	@Autowired
	 	private UserController userController;

	 	private static final Logger logger = LogManager.getLogger(Log4jExample.class);
	 	@Autowired
	    UserService userService;
	 	
	 	@Autowired
	    GroupService groupService;
	 		
	 	@Autowired
	 	private ImageRepository imageRepository;
	 	
	 	@Autowired
	 	private ReactionService reactionService;
	 	
	 	@Autowired
	 	private CommentRepository commRepository;

	 	@Transactional
	 	public void deleteImage(Long imageId) {
	 	    imageRepository.deleteById(imageId);
	 	}


	    @GetMapping("/all")
	    //@PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<List<Post>> getAll(){
	    	List<Post> l=postService.getAll();
	    	List<Reaction> r=reactionService.getAll();
	    	List<Post> list=new ArrayList<Post>();
	    	for (Post g:l) {
//	    		for (Reaction reaction : r) {
//	    	        if (reaction.getPost().getId().equals(g.getId()) && !g.getReactions().contains(reaction)) {
//	    	            g.getReactions().add(reaction);
//	    	            
//	    	        }
//	    	        postService.save(g);
//	    	    }
	    		if (!g.isDeleted()) {
	    			list.add(g);
	    		}
	    	}
	        return new ResponseEntity<>(list, HttpStatus.OK);
	    }
	    
	    @GetMapping("/all/user")
	    //@PreAuthorize("hasRole('USER')")
	    //@CrossOrigin(origins = "http://localhost:4200")
	    public ResponseEntity<List<Post>> getAllUsers(){
	    	List<Post> l=postService.getAll();
	    	List<Reaction> r=reactionService.getAll();
	    	for (Post g:l) {
//	    		for (Reaction reaction : r) {
//	    	        if (reaction.getPost().getId().equals(g.getId()) && !g.getReactions().contains(reaction)) {
//	    	            g.getReactions().add(reaction);
//	    	            postService.save(g);
//	    	        }
//	    	    }
	    	}
	    	
	    	 rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
	    	    if (currentUser != null) {
	    	    	List<Post> Groups = postService.findAllByUserId(currentUser);
	      	        List<Post> list=new ArrayList<Post>();
	      	    	for (Post g:Groups) {
	      	    		if ( !g.isDeleted()) {
	      	    			list.add(g);
	      	    		}
	      	    	}
	    	        return new ResponseEntity<>(list, HttpStatus.OK);
	    	    } else {
	    	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    	    }
	    }

	   /* @GetMapping("/all")
	    @PreAuthorize("hasRole('ADMIN')")
	    public List<User> loadAll() {
	        return this.postService.findAll();
	    }
*/
	    @GetMapping("/all/sorted")
	  //@PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<List<Post>> getAllSorted(@RequestParam(required = false, defaultValue = "new") String sort) {
	      logger.info("pozvano sorted"+ sort);
	    	List<Post> posts;
	      
	      if (sort.equals("old")) {
	          posts = postService.getAllSortedByDate(true);
	      } else if (sort.equals("new")) {
	          posts = postService.getAllSortedByDate(false);
	      } else {
	          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
	      List<Post> l=new ArrayList<Post>();
	      for (Post p:posts) {
	    	  if (!p.isDeleted()) {
	    		  l.add(p);
	    		  
	    	  }
	      }
	      
	      return new ResponseEntity<>(l, HttpStatus.OK);
	  }

	
	    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
	    @GetMapping("/{id}")
	    public ResponseEntity<Post> getOne(@PathVariable Long id) throws Exception{
	    	//throw new Exception("vgf");
	    	//Long id2 = Long.parseLong(id);
	    	//int id3 =Integer.parseInt(id);
	       Post post = postService.findPost(id);
	       if(post!=null ){
	        	if(!post.isDeleted()) {
	            return new ResponseEntity<>(post, HttpStatus.OK);}
	        }
	        return new ResponseEntity<>(null, HttpStatus.OK);
	        
//	        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
//	    	
//	    	 if (currentUser != null) {
//	    	        List<Post> posts = postService.findAllByUserId(currentUser);
//	    	        Post post = postService.findPost(id); 
//	    	       
//	    	        if(post!=null){
//	    	        	 for ( Post p : posts) {
//	 	    	        	if (p.equals(post))
//	 	    	        	{
//	 	    	        		return new ResponseEntity<>(post, HttpStatus.OK);
//	 	    	           }
//	    	             }
//	    	             return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//	    	         } else {
//	    	             return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//	    	         }
//	    	     } else {
//	    	         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//	    	     }
	    }
	    
	 
	    
	 
		@PostMapping("/create")
	    public ResponseEntity<Post> create(@RequestBody @Validated PostDTO newPost) throws Exception{
//				if (newPost!=null) {
//					throw new Exception("vg");
//				}.,
//				if (newPost==null) {
//					throw new Exception("gv");
//				}
	    	 	Post post = new Post();
	    	 	
	    	 	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		        User u = (User) auth.getPrincipal();
		        rs.ac.uns.ftn.svtvezbe07.model.entity.User user= userService.findByUsername(u.getUsername());
		        post.setContent(newPost.getContent());
		        post.setCreationDate(LocalDateTime.now());
		        if (!newPost.getImages().isEmpty()) {
		            Set<Image> images = new HashSet<>();
		            for (Image imageDTO : newPost.getImages()) {
		             
		                    Image image = new Image();
		                    image.setPath(imageDTO.getPath());
		                    image.setPost(post);
		                    image.setUser(post.getPostedBy());
		                    if (!image.getPath().equals("")) {
		                        images.add(image);
		                    }
		                
		            }
		            post.setImages(images); // Dodajemo nove slike
		        }
		        post.setDeleted(false);
		        post.setLikes(0);
		        post.setDislikes(0);
		        post.setHearts(0);
		        post.setCommentsForPost(false);
//		     // Prolazak kroz slike
//		        if (!newPost.getImages().isEmpty()) {
//		            Set<Image> images = new HashSet<>();
//		            for (Image imageDTO : newPost.getImages()) {
//		                // Provera da li slika već postoji u setu
//		                boolean imageExists = false;
//		                for (Image existingImage : images) {
//		                    if (existingImage.getPath().equals(imageDTO.getPath())) {
//		                        imageExists = true;
//		                        break;
//		                    }
//		                }
//
//		                // Dodavanje slike samo ako ne postoji već u setu
//		                if (!imageExists && !imageDTO.getPath().equals("")) {
//		                    Image image = new Image();
//		                    image.setPath(imageDTO.getPath());
//		                    image.setPost(post);
//		                    image.setUser(user);
//		                    images.add(image);
//		                } else {
//		                    deleteImage(imageDTO.getId());
//		                }
//		            }
//		            post.setImages(images);
//		        }

		        post.setPostedBy(user);

		        
		    Post createdPost = postService.save(post);//postService.createPost(newPost);
	        if(createdPost == null){
	            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
	        }

        Post p=postService.findPostByContent(post.getContent());
        for (Image i:p.getImages()) {
	    	logger.info("id slika kad se kreira post"+i.getId());
	    }

	        return new ResponseEntity<>(p, HttpStatus.CREATED);
	    }
	
		@DeleteMapping("/delete/{id}")
		@Transactional
		@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
		//@CrossOrigin()
		public ResponseEntity<Post> delete(@PathVariable Long id) throws Exception {
		    PostDTO postDTO=new PostDTO(postService.findPost(id));	    
		
		    Post post = postService.findPost(id);
		 
		    Set<Image> images = post.getImages();
		    Set<Comment> comments = post.getComments();
		    Set<Reaction> r = post.getReactions();
//		    int counter = 0;
//		    int maxIterations = images.size(); // Promenite broj na željeni broj iteracija
//		    logger.info(images.size());
		    for (Image image : images) {
//		    	logger.info(image.getId());
//		        if (counter >= maxIterations) {
//		            break; // Izlazak iz petlje ako je broj iteracija dostignut
//		        }
//		        imageRepository.deleteById(image.getId());
		        //deleteImage(image.getId());
//		        counter++;
		    	image.setPost(null); 
		        imageRepository.save(image);
		    }
		    for (Comment c:comments) {
		    	c.setPost(null);
		    	c.setDeleted(true);
		    	commRepository.save(c);
		    }
		    for (Reaction c:r) {
		    	c.setPost(null);
		    	c.setDeleted(true);
		    	reactionService.save(c);
		    }
		    //Group g = groupService.findGroupById(post.getGroup().getId());
		    

			postService.delete(id);
		    return new ResponseEntity<>(new Post(), HttpStatus.OK);
		}
		@PostMapping("/createInGroup")
		public ResponseEntity<Post> createPostInGroup(@Validated @RequestBody PostDTO postDTO) {
		    rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
		    if (currentUser != null) {
		        Group group = groupService.findGroup(postDTO.getGroup().getId());
		        if (group != null) {
		            Post post = new Post();
		            post.setGroup(group);
		            post.setPostedBy(currentUser);
		            post.setContent(postDTO.getContent());
		            post.setCreationDate(LocalDateTime.now());
		            post.setDeleted(false);
		            if (!postDTO.getImages().isEmpty()) {
			            Set<Image> images = new HashSet<>();
			            for (Image imageDTO : postDTO.getImages()) {
			             
			                    Image image = new Image();
			                    image.setPath(imageDTO.getPath());
			                    image.setPost(post);
			                    image.setUser(currentUser);
			                    if (!image.getPath().equals("")) {
			                        images.add(image);
			                    }
			                
			            }
			            post.setImages(images); // Dodajemo nove slike
			        }
		            post.setLikes(0);
			        post.setDislikes(0);
			        post.setHearts(0);
			        post.setCommentsForPost(false);
			        

		          // Post createdPost = postService.createPost(post);
		            postService.save(post);
		            group.getPosts().add(post);
		            groupService.save(group);
		            Post p = postService.findPost(post.getId());
		            return new ResponseEntity<>(p, HttpStatus.CREATED);
		        } else {
		            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		        }
		    } else {
		        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		    }
		}
		
//		@GetMapping("/user-comments")
//		@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
//		public ResponseEntity<List<Post>> getPostsWithUserComments() {
//		    rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
//
//		    if (currentUser != null) {
//		        List<Post> postsWithUserComments = new ArrayList<>();
//
//		        // Prvo dohvatite sve komentare i odgovore koje je ulogovani korisnik ostavio
//		        List<Comment> userComments = commRepository.findAllByUserId(currentUser);
//
//		        // Za svaki komentar koji je korisnik ostavio, dohvatite post
//		        for (Comment comment : userComments) {
//		            Post post = postService.findPost(comment.getPost().getId());
//		            if (post != null && !post.isDeleted() && !postsWithUserComments.contains(post)) {
//		                postsWithUserComments.add(post);
//		            }
//		        }
//
//		        return new ResponseEntity<>(postsWithUserComments, HttpStatus.OK);
//		    } else {
//		        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		    }
//		}
		
		@GetMapping("/post-reaction-count")
		public ResponseEntity<Map<String, Integer>> getPostReactionCounts(@RequestParam("id") Long id) {
		    Post post = null;//postService.findPost(id);
		    List<Post> posts=postService.getAll();
		    for(Post p:posts) {
		    	if(p.getId().equals(id)) {
		    		post=p;
		    	}
		    }
		    List<Reaction> r=reactionService.getAll();
		    
		    Map<String, Integer> reactionCounts = new HashMap<>();
		    int totalLikes = countLikes(post.getReactions());
		    int totalDislikes = countDislikes(post.getReactions());
		    int totalHearts = countHearts(post.getReactions());

		    reactionCounts.put("likes", totalLikes);
		    reactionCounts.put("dislikes", totalDislikes);
		    reactionCounts.put("hearts", totalHearts);

		    return new ResponseEntity<>(reactionCounts, HttpStatus.OK);
		}

		public int countLikes(Set<Reaction> reactions) {
		    int count = 0;
		    for (Reaction reaction : reactions) {
		    	logger.info(reaction.getType());
		        if (reaction.getType().equals(ReactionType.LIKE)) {
		        	logger.info("lolana");
		            count++;
		        }
		    }
		    return count;
		}

		public int countDislikes(Set<Reaction> reactions) {
		    int count = 0;
		    for (Reaction reaction : reactions) {
		        if (reaction.getType().equals(ReactionType.DISLIKE)) {
		            count++;
		        }
		    }
		    return count;
		}

		public int countHearts(Set<Reaction> reactions) {
		    int count = 0;
		    for (Reaction reaction : reactions) {
		        if (reaction.getType().equals(ReactionType.HEART)) {
		            count++;
		        }
		    }
		    return count;
		}

		
		@PutMapping("/edit")
		@CrossOrigin(origins = "http://4200")
		@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
		public ResponseEntity<Post> edit(@RequestBody PostDTO editPost) throws Exception {
		    logger.info("ddd"+editPost.getContent(), editPost.getImages(), editPost.getId());
		    
		        Post edit = postService.findPost(editPost.getId());
		        if(edit==null) {
		        	throw new Exception("d");
		        }
		        logger.info(edit.getContent(), edit.getImages(), edit.getId());
		        edit.setContent(editPost.getContent());
//		        for (Image i:edit.getImages()) {
//		        	deleteImage(i.getId());
//		        }
//		        edit.getImages().clear(); 

		        if (!editPost.getImages().isEmpty()) {
		            Set<Image> images = new HashSet<>();
		            for (Image imageDTO : editPost.getImages()) {
		             
		                    Image image = new Image();
		                    image.setPath(imageDTO.getPath());
		                    image.setPost(edit);
		                    image.setUser(edit.getPostedBy());
		                    if (!image.getPath().equals("")) {
		                        images.add(image);
		                    }
		                
		            }
		            edit.setImages(images);
		        }

		        edit.setDeleted(false);
		        logger.info("edit: " + edit.getContent() + edit.getImages());
		        postService.save(edit);


		        Post p=postService.findPostByContent(edit.getContent());
		        for (Image i:p.getImages()) {
			    	logger.info("slike editovanog posta"+i.getId());
			    }	        return new ResponseEntity<>(p, HttpStatus.OK);
		   
		}
		

}