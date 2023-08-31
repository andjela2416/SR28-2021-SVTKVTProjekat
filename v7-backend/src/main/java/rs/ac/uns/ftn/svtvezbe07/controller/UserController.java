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
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.UserRepository;
import rs.ac.uns.ftn.svtvezbe07.security.TokenUtils;
import rs.ac.uns.ftn.svtvezbe07.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://4200", maxAge = 3600)
public class UserController {

    @Autowired
    UserService userService;
    
    @Autowired
    UserRepository userRepository;

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

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @GetMapping("/whoami")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    public User user(Principal user) {
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
}
