package rs.ac.uns.ftn.svtvezbe07.controller;

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
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
import rs.ac.uns.ftn.svtvezbe07.service.UserService;


@RestController
@RequestMapping("api/posts")
public class PostController {
	 	@Autowired
	    PostService postService;
	 	
	 	@Autowired
	 	private UserController userController;

	 	
	 	@Autowired
	    UserService userService;

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
	
	    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
	    }
	    
	 
		@PostMapping("/create")
	    public ResponseEntity<PostDTO> create(@RequestBody @Validated PostDTO newPost) throws Exception{
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
		        if (newPost.getImages() != null) {	 
		            Set<Image> images = new HashSet<>();
		            for (Image imageDTO : newPost.getImages()) {
		                Image image = new Image();
		                image.setPath(imageDTO.getPath());
		                image.setPost(post);
		                image.setUser(user);
		                images.add(image);
		            }
		            post.setImages(images);
		           // throw new Exception(newPost.getImages().toString());
		        }
		       
		        post.setPostedBy(user);

		        
		        
		    Post createdPost = postService.save(post);//postService.createPost(newPost);

	        if(createdPost == null){
	            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
	        }
	        
	        PostDTO postDTO = new PostDTO(createdPost);
	       
	        return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
	    }
	
		@DeleteMapping("/delete")
		@Transactional
		@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
		//@CrossOrigin()
		public ResponseEntity<PostDTO> delete(@RequestParam("id") Long id) {
		    PostDTO postDTO=new PostDTO(postService.findPost(id));
			postService.delete(id);
		    return new ResponseEntity<>(postDTO, HttpStatus.OK);
		}

        
	    @PutMapping("/edit")
	    public ResponseEntity<PostDTO> edit(@RequestBody @Validated PostDTO editPost){
	        Post edit = postService.findPost(editPost.getId());
	        edit.setContent(editPost.getContent());
	        if (editPost.getImages() != null) {
	            Set<Image> images = new HashSet<>();
	            for (Image imageDTO : editPost.getImages()) {
	                Image image = new Image();
	                image.setPath(imageDTO.getPath());
	                image.setPost(edit);
	                image.setUser(edit.getPostedBy());
	                images.add(image);
	            }
	            edit.setImages(images);
	        }
	        postService.save(edit);

	        PostDTO postDTO = new PostDTO(edit);
	        return  new ResponseEntity<>(postDTO, HttpStatus.CREATED);
	    }
}
