package rs.ac.uns.ftn.svtvezbe07.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.svtvezbe07.model.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.svtvezbe07.model.dto.UserDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.PasswordDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.UserTokenState;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Banned;
import rs.ac.uns.ftn.svtvezbe07.model.entity.FriendRequest;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.GroupRepository;
import rs.ac.uns.ftn.svtvezbe07.repository.UserRepository;
import rs.ac.uns.ftn.svtvezbe07.security.TokenUtils;
import rs.ac.uns.ftn.svtvezbe07.service.BannedService;
import rs.ac.uns.ftn.svtvezbe07.service.FriendRequestService;
import rs.ac.uns.ftn.svtvezbe07.service.GroupRequestService;
import rs.ac.uns.ftn.svtvezbe07.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://4200", maxAge = 3600)
public class UserController {

    @Autowired
    UserService userService;
    
	@Autowired
    FriendRequestService friendRequestService;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    BannedService bannedService;
    
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

 	private static final Logger logger = LogManager.getLogger(Log4jExample.class);
    @Autowired
    TokenUtils tokenUtils;

    /* Ili preporucen nacin: Constructor Dependency Injection
    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, TokenUtils tokenUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }
    */


    
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser){
    	
        User createdUser = userService.createUser(newUser);
        
        if(createdUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
    	// Provera da li je korisnik banovan
        User currentUserBan = userService.findByUsername(authenticationRequest.getUsername());
        if (currentUserBan != null) {
        	List<Banned> banned=bannedService.getAll();
        	for(Banned b:banned) {
        		if(b.getTowards().getId()==currentUserBan.getId()) {
        			if(b.getByAdmin()!=null) {
        				// Korisnik je banovan, vraćamo odgovarajući odgovor
        	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
        	                .body(new UserTokenState("Nemate pristup, vaš nalog je banovan.",0));
        			}
        		}
        	}
            
        }
    	
    	
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();
        
        User currentUser = userService.findByUsername(user.getUsername());
        currentUser.setLastLogin(LocalDateTime.now()); // Postavljanje trenutnog vremena kao lastLogin
        userService.save(currentUser);

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.findAll();
    }
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsersAndGroups(@RequestParam String keyword, @RequestParam(required = false) String firstName,
    	    @RequestParam(required = false) String lastName) {
        //Set<User> users =new HashSet<>( userRepository.findByUsernameContaining(keyword));
        Set<User> usersByFirstName = new HashSet<>(userRepository.findByFirstNameEquals(keyword));
        Set<User> usersByLastName = new HashSet<>(userRepository.findByLastNameEquals(keyword));
        List<Group> groups = groupRepository.findByNameContaining(keyword);
        
        Set<User> usersByFirstNameOrLastName = new HashSet<>();
        Set<User> combinedUsers = new HashSet<>();
        
        if (firstName != null && lastName != null) {
            usersByFirstNameOrLastName =new HashSet<>(userRepository.findByFirstNameEqualsAndLastNameEquals(firstName, lastName));
            combinedUsers.addAll(usersByFirstNameOrLastName);
        }
        
        
        //combinedUsers.addAll(users);
        combinedUsers.addAll(usersByFirstName);
        combinedUsers.addAll(usersByLastName); 
        
        Map<String, Object> result = new HashMap<>();
        result.put("users", new ArrayList<>(combinedUsers));
        result.put("groups", groups);
        
        return ResponseEntity.ok(result);
    }



    @GetMapping("/whoami")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    public User user(Principal user) {
    	logger.info("helo");
    	User u=this.userService.findByUsername(user.getName());
//    	Hibernate.initialize(u.getGroups());
    	//User us=userRepository.findByIdWithGroups(u.getId());
//    	Optional<User> us = userRepository.findByIdWithGroups(u.getId());
//    	if (us.isPresent()) {
//    	    return us.get();
//    	} 
    	//logger.info("whoami groups "+us.getGroups().isEmpty());
    	//Hibernate.initialize(u.getGroups());
    	//UserDTO dto=new UserDTO(us);
        return u;
    }
    @GetMapping("/user/{userId}/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    public ResponseEntity<Set<Group>> getUserGroups(@PathVariable Integer userId) {
        Set<Group> groups = userService.getUserGroups(userId);
        return ResponseEntity.ok(groups);
    }
    @PutMapping("/password-change")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    public ResponseEntity<UserDTO> changePassword(@RequestBody @Validated PasswordDTO passwords) {
        
        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = user(SecurityContextHolder.getContext().getAuthentication());
  	  
        if (encoder.matches(passwords.getCurrent(), currentUser.getPassword()) && passwords.getConfirm().equals(passwords.getPassword())){
        	currentUser.setPassword(encoder.encode(passwords.getPassword()));
            userService.save(currentUser);
        } else {

            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        UserDTO userDTO = new UserDTO(currentUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);

    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        Optional<User> optionalUser = userService.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @PostMapping(consumes = "application/json")
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {

		User user = new User();
		
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setEmail(userDTO.getEmail());
		user.setLastLogin(userDTO.getLastLogin());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setDisplayName(userDTO.getDisplayName());
		user.setDescription(userDTO.getDescription());
		user.setProfilePhoto(userDTO.getProfilePhoto());

		user = userService.save(user);
		return new ResponseEntity<>(new UserDTO(user), HttpStatus.CREATED);
	}
    
    @PostMapping("/edit")
	public ResponseEntity<User> editUser(@RequestBody User user) {

		Optional<User> u = userService.findById(user.getId());
		User us=u.get();

		us.setDisplayName(user.getDisplayName());
		us.setDescription(user.getDescription());
//		us.setProfilePhoto(user.getProfilePhoto());

		User usr = userService.save(us);
		return new ResponseEntity<>(usr, HttpStatus.CREATED);
	}
    
    @Transactional
    @DeleteMapping("/deleteFriendRequest/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FriendRequest> deleteFrRequest(@PathVariable Long id) {
        try {
        	FriendRequest gr=friendRequestService.findFriendRequest(id);
        	gr.setApproved(false);
        	gr.setAt(LocalDateTime.now());
        	gr.setDeleted(true);
            friendRequestService.save(gr);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    @PostMapping("/createRequest/{id}")
    public ResponseEntity<FriendRequest> createFrRequest(@PathVariable  Integer id) throws Exception {
        try {
            Optional<User> u = userService.findById(id);
            User user= u.get();
            if (user != null) {

            	User user2 = user(SecurityContextHolder.getContext().getAuthentication());
            	if (user.getFriends().contains(user2)) {
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                FriendRequest frRequest = new FriendRequest();
                frRequest.setApproved(false);
                frRequest.setDeleted(false);
                frRequest.setCreatedAt(LocalDateTime.now());
                frRequest.setToWho(user);
                frRequest.setFromWho(user2);

                FriendRequest createdFrRequest = friendRequestService.save(frRequest);

                return new ResponseEntity<>(createdFrRequest, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/friendRequests/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    public ResponseEntity<List<FriendRequest>> getAllFrRequests() {
        List<FriendRequest> frRequests = friendRequestService.getAll();

        List<FriendRequest> pendingFrRequests = new ArrayList<>();
        for (FriendRequest request : frRequests) {
            if (request.isApproved() || request.isDeleted()) {
               continue;
            }else { pendingFrRequests.add(request);}
            
        }

        return new ResponseEntity<>(pendingFrRequests, HttpStatus.OK);
    }
    
    @PostMapping("/friendRequests/all/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUPADMIN')")
    public ResponseEntity<List<FriendRequest>> getAllFriendRequests(@PathVariable  Integer id) {
    	Optional<User> u=userService.findById(id);
    	User user=u.get();
        List<FriendRequest> frRequests = friendRequestService.findAllByToWho(user);
        
        List<FriendRequest> pendingFrRequests = new ArrayList<>();
        for (FriendRequest request : frRequests) {
            if (request.isApproved() || request.isDeleted())  {
               continue;
            }else { pendingFrRequests.add(request);}
        }

        return new ResponseEntity<>(pendingFrRequests, HttpStatus.OK);
    }
    
    @PutMapping("/approve/{id}")
    public ResponseEntity<FriendRequest> approveFriendRequest(@PathVariable Long id) throws Exception {
        try {
            FriendRequest frRequest = friendRequestService.findFriendRequestById(id);

            if (frRequest != null) {
            	frRequest.setApproved(true);
            	frRequest.setAt(LocalDateTime.now());
              
                User user1 = frRequest.getToWho();
                User user2 = frRequest.getFromWho();

                if (user1.getFriends().contains(user2) || user2.getFriends().contains(user1) ) {
                	return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
                }

                user1.getFriends().add(user2);
                user2.getFriends().add(user1);

                userService.save(user1);
                userService.save(user2);
                FriendRequest approvedFrRequest=friendRequestService.save(frRequest);
                return new ResponseEntity<>(approvedFrRequest, HttpStatus.OK);
            } else {
            	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/user/{userId}/friends")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    public ResponseEntity<Set<User>> getUserFriends(@PathVariable Integer userId) {
        Optional<User> optionalUser = userService.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<User> friends = user.getFriends();
            return ResponseEntity.ok(friends);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @PutMapping("/removeFriend/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','ADMINGROUP')")
    public ResponseEntity<Void> removeFriend(@PathVariable Integer userId) {
        try {
            User currentUser = user(SecurityContextHolder.getContext().getAuthentication());
            Optional<User> optionalUser = userService.findById(userId);

            if (optionalUser.isPresent()) {
                User userToRemove = optionalUser.get();


                if (currentUser.getFriends().contains(userToRemove)) {
                    currentUser.getFriends().remove(userToRemove);
                    userToRemove.getFriends().remove(currentUser);

                    userService.save(currentUser);
                    userService.save(userToRemove);

                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
