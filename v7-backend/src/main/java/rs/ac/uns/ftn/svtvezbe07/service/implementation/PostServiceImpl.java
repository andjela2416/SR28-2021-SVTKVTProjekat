package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.PostRepository;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	 	@Autowired
	    private PostRepository postRepository;



	    @Override
	    public List<Post> getAll() {
	        return this.postRepository.findAll();
	    }
	    

		public Post save(Post post) {
			return postRepository.save(post);
		}

		@Override
		public List<Post> findAllByUserId(User user) {
		    return postRepository.findAllByPostedBy(user);
		}

		
		/*public Optional<Post> getByIdInt(Integer id) {
			return postRepository.findPostById(id);
		}*/
		


	    @Override
	    public Post findPostByContent(String content) {
	        Optional<Post> post = postRepository.findPostByContent(content);
	        if(!post.isEmpty()){
	            return post.get();
	        }

	        return null;
	    }

	    public Post findPost(Long id) {
	        Optional<Post> post = postRepository.findById(id);
	        if(!post.isEmpty()){
	            return post.get();
	        }

	        return null;
	    }
	    
	    public Post findPostById(Integer id) {
	        Post post = postRepository.findPostById(id);
	        if(post!=null){
	            return post;
	        }

	        return null;
	    }
	    @Override
	    public void delete(Long idInt) {
	        this.postRepository.deleteById(idInt);
	    }


		@Override
		public Post createPost(rs.ac.uns.ftn.svtvezbe07.model.dto.PostDTO newPost) {
			 Post post = new Post();
		        post.setContent(newPost.getContent());
		        post.setCreationDate(LocalDateTime.now());
		        post = postRepository.save(post);
		        return post;
		}



}
