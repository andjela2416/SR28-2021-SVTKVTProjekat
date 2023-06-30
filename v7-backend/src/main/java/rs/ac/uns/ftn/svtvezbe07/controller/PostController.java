package rs.ac.uns.ftn.svtvezbe07.controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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
import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.repository.ImageRepository;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
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
	 	private ImageRepository imageRepository;

	 	@Transactional
	 	public void deleteImage(Long imageId) {
	 	    imageRepository.deleteById(imageId);
	 	}


	    @GetMapping("/all")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<List<Post>> getAll(){
	    	
	        return new ResponseEntity<>(postService.getAll(), HttpStatus.OK);
	    }
	    
	    @GetMapping("/all/user")
	    //@PreAuthorize("hasRole('USER')")
	    //@CrossOrigin(origins = "http://localhost:4200")
	    public ResponseEntity<List<Post>> getAllUsers(){
	    	 rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
	    	    if (currentUser != null) {
	    	        List<Post> posts = postService.findAllByUserId(currentUser);
	    	        for (Post post : posts) {
	    	            Set<Image> images = post.getImages();
	    	            for (Image image : images) {
	    	                String path = image.getPath();
	    	                logger.info("Putanja slike: " + path);
	    	            }
	    	        }
	    	        return new ResponseEntity<>(posts, HttpStatus.OK);
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
	
	    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
	    @GetMapping("/{id}")
	    public ResponseEntity<Post> getOne(@PathVariable Long id) throws Exception{
	    	//throw new Exception("vgf");
	    	//Long id2 = Long.parseLong(id);
	    	//int id3 =Integer.parseInt(id);
	       Post post = postService.findPost(id);
	        if(post==null){
	            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<>(post, HttpStatus.OK);
	        
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
//				}
//				if (newPost==null) {
//					throw new Exception("gv");
//				}
	    	 	Post post = new Post();
	    	 	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		        User u = (User) auth.getPrincipal();
		        rs.ac.uns.ftn.svtvezbe07.model.entity.User user= userService.findByUsername(u.getUsername());
		        post.setContent(newPost.getContent());
		        post.setCreationDate(LocalDateTime.now());
		        
		     // Prolazak kroz slike
		        if (!newPost.getImages().isEmpty()) {
		            Set<Image> images = new HashSet<>();
		            for (Image imageDTO : newPost.getImages()) {
		                // Provera da li slika već postoji u setu
		                boolean imageExists = false;
		                for (Image existingImage : images) {
		                    if (existingImage.getPath().equals(imageDTO.getPath())) {
		                        imageExists = true;
		                        break;
		                    }
		                }

		                // Dodavanje slike samo ako ne postoji već u setu
		                if (!imageExists && !imageDTO.getPath().equals("")) {
		                    Image image = new Image();
		                    image.setPath(imageDTO.getPath());
		                    image.setPost(post);
		                    image.setUser(user);
		                    images.add(image);
		                } else {
		                    deleteImage(imageDTO.getId());
		                }
		            }
		            post.setImages(images);
		        }

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
	
		@DeleteMapping("/delete")
		@Transactional
		@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
		//@CrossOrigin()
		public ResponseEntity<Post> delete(@RequestParam("id") Long id) {
		    PostDTO postDTO=new PostDTO(postService.findPost(id));	    
		
		    Post post = postService.findPost(id);
		    Set<Image> images = post.getImages();
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

			postService.delete(id);
		    return new ResponseEntity<>(post, HttpStatus.OK);
		}

		@PutMapping("/edit")
		@CrossOrigin(origins = "http://4200")
		@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
		public ResponseEntity<Post> edit(@RequestBody PostDTO editPost) throws Exception {
		    logger.info("ddd"+editPost.getContent(), editPost.getImages(), editPost.getId());
		    try {
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
		            edit.setImages(images); // Dodajemo nove slike
		        }

	  
		        logger.info("edit: " + edit.getContent() + edit.getImages());
		        postService.save(edit);


		        Post p=postService.findPostByContent(edit.getContent());
		        for (Image i:p.getImages()) {
			    	logger.info("slike editovanog posta"+i.getId());
			    }	        return new ResponseEntity<>(p, HttpStatus.OK);
		    } catch (Exception e) {
		    	logger.info("error: "+e.getMessage());
		    	throw new Exception(e.getMessage());
			       
		        // Izmena nije uspela
		        //return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		    }
		}
		

}