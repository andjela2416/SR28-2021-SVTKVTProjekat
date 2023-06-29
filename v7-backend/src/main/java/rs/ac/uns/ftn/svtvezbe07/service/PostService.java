package rs.ac.uns.ftn.svtvezbe07.service;

import java.util.List;
import java.util.Optional;

import rs.ac.uns.ftn.svtvezbe07.model.dto.PostDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public interface PostService {

    List<Post> getAll();
    
    Post save (Post post);

	Post findPostById(Integer id);

	Post findPost(Long id);

	Post createPost(PostDTO newPost);
	
    Post findPostByContent(String content);

    void delete(Long id);

    List<Post> findAllByUserId(User user) ;
}
