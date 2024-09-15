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
import org.springframework.web.multipart.MultipartFile;

import rs.ac.uns.ftn.svtvezbe07.model.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.svtvezbe07.model.dto.UserDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.PasswordDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.ReportDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.UserTokenState;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Banned;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.FriendRequest;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Report;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Roles;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.GroupRepository;
import rs.ac.uns.ftn.svtvezbe07.repository.ImageRepository;
import rs.ac.uns.ftn.svtvezbe07.repository.UserRepository;
import rs.ac.uns.ftn.svtvezbe07.security.TokenUtils;
import rs.ac.uns.ftn.svtvezbe07.service.BannedService;
import rs.ac.uns.ftn.svtvezbe07.service.FriendRequestService;
import rs.ac.uns.ftn.svtvezbe07.service.GroupRequestService;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
import rs.ac.uns.ftn.svtvezbe07.service.ReportService;
import rs.ac.uns.ftn.svtvezbe07.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://4200", maxAge = 3600)
public class UserController {

    @Autowired
    UserService userService;
    
    @Autowired
    ImageRepository imageRepository;
    
	@Autowired
    FriendRequestService friendRequestService;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    BannedService bannedService;
    
    @Autowired
    GroupRepository groupRepository;
    
    @Autowired
    PostService postService;
    
    @Autowired
    ReportService reportService;

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
        logger.info("Registracija uspesna");
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) throws SQLException {
   
//	    	String jdbcUrl = "jdbc:mysql://localhost:3306/jpa?useSSL=false";
//	    	Connection connection = DriverManager.getConnection(jdbcUrl, "root", "root");
//
//    	    String sqlInsert = "INSERT INTO images (path,type,pic_byte) VALUES ('userandj.webp','image/web',?)";
//
//
//    	    PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
//    	    
//    	    String currentDirectory = System.getProperty("user.dir");
//        	System.out.println("Trenutni radni direktorijum: " + currentDirectory);
//        	String uploadDirectory = currentDirectory + File.separator + "uploads";
//        	System.out.println("Putanja do uploads direktorijuma: " + uploadDirectory);
//
//            //String uploadDirectory = "src/main/resources/uploads/";
//
//            String filePath = uploadDirectory + "userandj.webp";
//            logger.info(filePath);
//    	    preparedStatement.setBytes(1, compressBytes(filePath.getBytes()));
//
//    	    if (connection != null) {
//    	        System.out.println("Uspostavljena veza s bazom podataka.");
//    	    }
//    	    
//  
//    	    preparedStatement.executeUpdate();
//
//
//    	    preparedStatement.close();
//    	    connection.close();
   	
    	// Provera da li je korisnik banovan
        User currentUserBan = userService.findByUsername(authenticationRequest.getUsername());
        if (currentUserBan != null) {
        	List<Banned> banned=bannedService.getAll();
        	for(Banned b:banned) {
        		if(b.getTowards().getId()==currentUserBan.getId()) {
        			if(b.getByAdmin()!=null) {
        				// Korisnik je banovan
        				logger.info("Nalog je banovan");
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
        logger.info("Uspesno logovanje");
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
    	logger.info("Nadjeni svi useri");
        return this.userService.findAll();
    }
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsersAndGroups(@RequestParam String keyword, @RequestParam(required = false) String firstName,
    	    @RequestParam(required = false) String lastName) {
        //Set<User> users =new HashSet<>( userRepository.findByUsernameContaining(keyword));
        Set<User> usersByFirstName = new HashSet<>(userRepository.findByFirstNameEquals(keyword));
        Set<User> usersByLastName = new HashSet<>(userRepository.findByLastNameEquals(keyword));
        List<Group> groups = groupRepository.findByNameContaining(keyword);
        
        List<Group> ggroups = groups.stream()
                .filter(group -> !group.isDeleted())
                .filter(group -> !group.isSuspended())
                .collect(Collectors.toList());
        
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
        result.put("groups", ggroups);
        
        logger.info("Pretraga grupa i usera");
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
    	logger.info("Vracen ulogovan user");
        return u;
    }
    @GetMapping("/user/{userId}/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    public ResponseEntity<Set<Group>> getUserGroups(@PathVariable Integer userId) {
        Set<Group> groups = userService.getUserGroups(userId);
        logger.info("Grupe usera");
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
        logger.info("Promenjena lozinka");
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);

    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        Optional<User> optionalUser = userService.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            logger.info("Nadjen user po idu");
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
		logger.info("Sacuvan user");
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
		logger.info("Editovan user");
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
            logger.info("Odbijen zahtev za prijateljstvo");
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
                logger.info("Kreiran je zahtev za prijateljstvo");
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
        logger.info("Vraceni svi zahtevi za prijateljstvo usera");
        return new ResponseEntity<>(pendingFrRequests, HttpStatus.OK);
    }

	@PostMapping("/suspend/{id}")
	@Transactional
	@PreAuthorize("hasAnyRole( 'ADMIN')")
	//@CrossOrigin()
	public ResponseEntity<User> suspend(@PathVariable Integer id) throws Exception {

		boolean korisnikJeVecBanovan = false;
		Optional<User> u = userService.findById(id);
		List <Banned> listaBanovanih=bannedService.getAll();
        User currentUser = user(SecurityContextHolder.getContext().getAuthentication());

		for (Banned b : listaBanovanih) {
		    if (b.getTowards().equals(u)) {
		    	korisnikJeVecBanovan = true;			        
		    } 
		}
		if (!korisnikJeVecBanovan) {
	    	Banned banUser = new Banned();
	        banUser.setByAdmin(currentUser);
	        banUser.setTowards(u.get());
	        bannedService.save(banUser);
	        List<Report> reportiZaKorisnika=reportService.getAll();
	        for(Report re:reportiZaKorisnika) {
	        	if(re.getReported()!=null && re.getReported().equals(u)) {
	        		re.setAccepted(true);
	        		reportService.save(re);
	        	}
	        }
		}
		
		logger.info("User je banovan");
        return new ResponseEntity<>(u.get(), HttpStatus.OK);

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
        logger.info("Vraceni zahtevi za prijateljstvo usera");
        return new ResponseEntity<>(pendingFrRequests, HttpStatus.OK);
    }
    
    @PostMapping("/friendRequests/all2/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUPADMIN')")
    public ResponseEntity<List<FriendRequest>> getAllFriendRequests2(@PathVariable  Integer id) {
    	Optional<User> u=userService.findById(id);
    	User user=u.get();
        List<FriendRequest> frRequests = friendRequestService.findAllByFromWho(user);
        
        List<FriendRequest> pendingFrRequests = new ArrayList<>();
        for (FriendRequest request : frRequests) {
            if (request.isApproved() || request.isDeleted())  {
               continue;
            }else { pendingFrRequests.add(request);}
        }
        logger.info("Vraceni zahtevi za prijateljstvo usera");
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
                logger.info("Prihvacen zahtev za prijateljstvo usera");
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
            logger.info("Vraceni prijatelji usera");
            return ResponseEntity.ok(friends);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @PutMapping("/removeFriend/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUPADMIN')")
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
                    logger.info("Izbacen user iz prijatelja");
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
    @PostMapping("/uploadFirstTry")
    public void uploadImagene(@RequestParam("file") MultipartFile file) throws IOException {
        saveImage(file);
        
    }
    public String saveImageToFolder(MultipartFile file) {
        if (file.isEmpty()) {
            return "Molimo vas da izaberete sliku za upload.";
        }

        try {
            String currentDirectory = System.getProperty("user.dir");
            String uploadDirectory = currentDirectory + File.separator + "src\\main\\resources\\uploads\\";
            Path uploadPath = Paths.get(uploadDirectory);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            byte[] bytes = file.getBytes();
            Path filePath = Paths.get(uploadDirectory, file.getOriginalFilename());
            Files.write(filePath, bytes);
            
            return "Slika je uspešno sačuvana na serveru.";
        } catch (IOException e) {
            return "Došlo je do greške prilikom čuvanja slike.";
        }
    }

    
    public void saveImage(MultipartFile file) throws IOException {
    	String currentDirectory = System.getProperty("user.dir");
    	System.out.println("Trenutni radni direktorijum: " + currentDirectory);
    	String uploadDirectory = currentDirectory + File.separator + "src\\main\\resources\\uploads\\";
    	System.out.println("Putanja do uploads direktorijuma: " + uploadDirectory);

        //String uploadDirectory = "src/main/resources/uploads/";

        String filePath = uploadDirectory + file.getOriginalFilename();
        File dest = new File(filePath);

        file.transferTo(dest);
        System.out.println(dest);
//        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = user(SecurityContextHolder.getContext().getAuthentication());
//    	
//        Image img=new Image();
//        img.setPath(filePath);
//        currentUser.setProfilePhoto(img.getPath());
//        userService.save(currentUser);
    }
    @PostMapping("/uploadPostPhoto")
	public void uplaodImage1(@RequestParam("file") MultipartFile file,@RequestParam("post") Long post) throws Exception {
    	
		System.out.println("Original Image Byte Size - " + file.getBytes().length);
		Post p=postService.findPost(post);
		System.out.println(p.getId());
        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = user(SecurityContextHolder.getContext().getAuthentication());
		
        Image img = new Image(file.getOriginalFilename(), file.getContentType(), compressBytes(file.getBytes()));
        //img.setPost(p);
        img.setUser(currentUser);
        System.out.println("path" + file.getOriginalFilename() + "type " + file.getContentType() + "byte " + compressBytes(file.getBytes()));
        imageRepository.save(img);
        saveImage(file);        
        p.getImages().add(img);
        postService.save(p);
        logger.info("Napravljena,sacuvana slika");
    }
    @PostMapping("/uploadPostPhotoEdit")
    public void uploadImagesEdit(@RequestParam("files") List<MultipartFile> files, @RequestParam("post") Long post) throws Exception {
    	Post p = postService.findPost(post);   
    	System.out.println(p.getImages());

    	Iterator<Image> iterator = p.getImages().iterator();
    	while (iterator.hasNext()) {
    	    Image m = iterator.next();
    	    iterator.remove(); 
    	}

    	p.getImages().clear();
    	postService.save(p);
    	for (MultipartFile file : files) {
    		int numberOfFiles = files.size();
    		System.out.println("Broj fajlova: " + numberOfFiles);

            System.out.println("Original Image Byte Size - " + file.getBytes().length);
            System.out.println(p.getId());
            rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = user(SecurityContextHolder.getContext().getAuthentication());

            Image img = new Image(file.getOriginalFilename(), file.getContentType(), compressBytes(file.getBytes()));
            //img.setPost(p);
            img.setUser(currentUser);
            System.out.println("path: " + file.getOriginalFilename() + " type: " + file.getContentType() + " byte: " + compressBytes(file.getBytes()));
            imageRepository.save(img);
            saveImage(file);
            System.out.println(img.getId());
            p.getImages().add(img);
        }
    	postService.save(p);
    	logger.info("Editovanje slika");
    }

    
    @PostMapping("/uploadProfilePhoto")
  	public void uplaodImage2(@RequestParam("file") MultipartFile file) throws Exception {
  		System.out.println("Original Image Byte Size - " + file.getBytes().length);
//    	
//    	Optional<Image> existingImage = imageRepository.findByImagePath(file.getOriginalFilename());
//
//        if (existingImage.isPresent()) {
//            rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = user(SecurityContextHolder.getContext().getAuthentication());
//            currentUser.setProfilePhotoUpload(file.getOriginalFilename());
//            userService.save(currentUser);
//        } else {
            Image img = new Image(file.getOriginalFilename(), file.getContentType(), compressBytes(file.getBytes()));
            System.out.println("path" + file.getOriginalFilename() + "type " + file.getContentType() + "byte " + compressBytes(file.getBytes()));
            imageRepository.save(img);
            saveImage(file);
            rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = user(SecurityContextHolder.getContext().getAuthentication());
            currentUser.setProfilePhoto(null);
           // currentUser.setProfilePhotoUpload(file.getOriginalFilename());
            currentUser.setProfilePhotoUpload(img);
            userService.save(currentUser);
            logger.info("Profilna slika sacuvana");
//        }

      }

	@GetMapping(path = { "/get/{imageName}/{id}" })
	public Image getImage(@PathVariable("imageName") String imageName,@PathVariable("id") Long id ) throws IOException {

		final Optional<Image> retrievedImage = imageRepository.findByIdAndImagePath(id,imageName);
		Image img = new Image(retrievedImage.get().getImagePath(), retrievedImage.get().getType(),
				decompressBytes(retrievedImage.get().getPicByte()));
		logger.info("Dobavljanje slike");
		return img;
	}

	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();
	}

	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
}
