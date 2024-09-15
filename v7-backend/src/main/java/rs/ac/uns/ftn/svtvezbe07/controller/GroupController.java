package rs.ac.uns.ftn.svtvezbe07.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;
import rs.ac.uns.ftn.svtvezbe07.model.dto.GroupDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.GroupRequestDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.ReportDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Report;
import rs.ac.uns.ftn.svtvezbe07.model.entity.ReportReason;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Roles;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Banned;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe07.repository.BannedRepository;
import rs.ac.uns.ftn.svtvezbe07.repository.ImageRepository;
import rs.ac.uns.ftn.svtvezbe07.repository.UserRepository;
import rs.ac.uns.ftn.svtvezbe07.service.BannedService;
import rs.ac.uns.ftn.svtvezbe07.service.CommentService;
import rs.ac.uns.ftn.svtvezbe07.service.GroupRequestService;
import rs.ac.uns.ftn.svtvezbe07.service.GroupService;
import rs.ac.uns.ftn.svtvezbe07.service.PostService;
import rs.ac.uns.ftn.svtvezbe07.service.ReportService;
import rs.ac.uns.ftn.svtvezbe07.service.UserService;
@RestController
@RequestMapping("api/groups")
@CrossOrigin(origins = "http://4200", maxAge = 3600)
public class GroupController {
 	@Autowired
    GroupService groupService;
 	
	@Autowired
    GroupRequestService groupRequestService;
	
 	@Autowired
 	private UserController userController;

 	private static final Logger logger = LogManager.getLogger(Log4jExample.class);
 	@Autowired
    UserService userService;
 	@Autowired
    ReportService reportService;
 	@Autowired
    PostService postService;
 	@Autowired
    BannedService bannedService;
 	@Autowired
    CommentService commService;
 	@Autowired
 	private ImageRepository imageRepository;
 	@Autowired
 	private UserRepository userRepository;
 	@Autowired
 	private BannedRepository bannedRepository;
 	
 	 @GetMapping("/random")
     public ResponseEntity<List<Post>> getRandomPosts() {
         logger.info("helooo");
         List<Post> allPosts = postService.getAll();
        
         rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
  		
         List<rs.ac.uns.ftn.svtvezbe07.model.entity.User> friends = userRepository.getFriends(currentUser.getId());

         //grupe koje je on napravio
         Set<Group> userGroups = new HashSet<>();
         List<Group> allGroups = groupService.getAll();

         for (Group g:allGroups){
        	 	boolean userIsAdmin = userIsAdmin(currentUser,g);
        	    boolean userBelongsToGroup = userBelongsToGroup(currentUser, g);
        	    boolean userIsAddedAdmin = userIsAddedAdmin(currentUser, g);

        	    if ((userBelongsToGroup || userIsAddedAdmin || userIsAdmin) && !g.isSuspended() && !g.isDeleted()) {
        	        userGroups.add(g);
        	    }
         }
         
         
         List<Post> allGroupAndPublicPosts = new ArrayList<>();
         
         for (Post post : allPosts) {
             if (post.getGroup() != null && userGroups.contains(post.getGroup()) && post.isDeleted()==false) {
                 allGroupAndPublicPosts.add(post);
             } else if (post.getGroup() == null && post.isDeleted()==false) {
            	 for(rs.ac.uns.ftn.svtvezbe07.model.entity.User u : friends) {
            		 System.out.println(u.getId());
            		 if(post.getPostedBy().getId().equals(u.getId()))
            			 {
            			 allGroupAndPublicPosts.add(post);
            			 };
            	 }      
             }if(post.getGroup() == null && post.isDeleted()==false && post.getPostedBy().getId().equals(currentUser.getId())) {        	 
            	 allGroupAndPublicPosts.add(post);
            	 System.out.println(post.getId());
             }
         }
         Random random = new Random();
         for (int i = allGroupAndPublicPosts.size() - 1; i > 0; i--) {
             int j = random.nextInt(i + 1);
             Post temp = allGroupAndPublicPosts.get(i);
             allGroupAndPublicPosts.set(i, allGroupAndPublicPosts.get(j));
             allGroupAndPublicPosts.set(j, temp);
         }
         logger.info("Vracena random lista postova");
         return new ResponseEntity<>(allGroupAndPublicPosts, HttpStatus.OK);
     }
 	 
     @GetMapping("/all/{idUser}/users")
     //@PreAuthorize("hasRole('ADMIN')")
     public ResponseEntity<Set<Group>> getAll(@PathVariable Integer idUser) throws Exception{
    	 try {
    	 java.util.Optional<rs.ac.uns.ftn.svtvezbe07.model.entity.User> u =userService.findById(idUser);
    	 Set<Group> userGroups = u.get().getGroups();
         List<Group> allGroups = groupService.getAll();

         for (Group g:allGroups){
        	    boolean userIsAdmin = userIsAdmin(u.get(),g);
        	    boolean userBelongsToGroup = userBelongsToGroup(u.get(), g);
        	    boolean userIsAddedAdmin = userIsAddedAdmin(u.get(), g);

        	    if ((userBelongsToGroup || userIsAddedAdmin || userIsAdmin) && !g.isSuspended() && !g.isDeleted()) {
        	        userGroups.add(g);
        	    }
         }
         logger.info("Vracena lista grupa usera");
         return new ResponseEntity<>(userGroups, HttpStatus.OK);}
    	 catch(Exception e) {
    		 throw new Exception(e.getMessage());
    	 }
     }

	private boolean userBelongsToGroup(rs.ac.uns.ftn.svtvezbe07.model.entity.User user, Group group) {
 	    Set<rs.ac.uns.ftn.svtvezbe07.model.entity.User> groupMembers = group.getMembers();
 	    return groupMembers.contains(user);
 	}
	
	private boolean userIsAddedAdmin(rs.ac.uns.ftn.svtvezbe07.model.entity.User user, Group group) {
 	    Set<rs.ac.uns.ftn.svtvezbe07.model.entity.User> groupadm = group.getAddedGroupAdmins();
 	    return groupadm.contains(user);
 	}
	
	private boolean userIsAdmin(rs.ac.uns.ftn.svtvezbe07.model.entity.User user, Group group) {
 	    rs.ac.uns.ftn.svtvezbe07.model.entity.User groupadm =  group.getGroupAdmin();
 	    return groupadm.equals(user);
 	}

	@GetMapping("/reports")
	public ResponseEntity<List<Report>> getAllReports() {
	    List<Report> allReports = reportService.getAll();
	    
	    List<Report> filteredReports = allReports.stream()
	            .filter(report -> !report.getAccepted()) 
	            .collect(Collectors.toList());
	    
	    logger.info("Vracena lista reportova");
	    return new ResponseEntity<>(filteredReports, HttpStatus.OK);
	}
	
	@GetMapping("/banns")
	@PreAuthorize("hasAnyRole( 'ADMIN')")
	public ResponseEntity<List<Banned>> getAllBanns() {
	    List<Banned> allBanns = bannedService.getAll();
	    
	    List<Banned> filteredBanns = allBanns.stream()
	            .filter(bann -> bann.getByGroupAdmin()==null) 
	            .collect(Collectors.toList());
	    logger.info("Vracena lista banova");
	    return new ResponseEntity<>(filteredBanns, HttpStatus.OK);
	}
	
	
	@GetMapping("/isBanned/{id}")
	public ResponseEntity<Boolean> isUserBannedInGroup(@PathVariable Long id) {
	    List<Banned> allBanns = bannedService.getAll();
	    rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
		
	    List<Banned> filteredBanns = allBanns.stream()
	            .filter(bann -> bann.getTowards().getId() == currentUser.getId())
	            .collect(Collectors.toList());
	    
	    List<Banned> filteredBanns2 = filteredBanns.stream()
	            .filter(bann -> bann.getGroup() != null)
	            .collect(Collectors.toList());
	    
	    Banned banned = filteredBanns2.stream()
	    	    .filter(bann -> bann.getGroup().getId() == id )
	    	    .findFirst()
	    	    .orElse(null);

	    
	    if(banned!=null) {
	    		return new ResponseEntity<>(true, HttpStatus.OK);
	    }
	    logger.info("Vracen odgovor da li je user banovan u grupi");
	    return new ResponseEntity<>(false, HttpStatus.OK);
	}
	
	@GetMapping("/banns2")
	@PreAuthorize("hasAnyRole( 'GROUPADMIN','ADMIN')")
	public ResponseEntity<List<Banned>> getAllBannsByGroupAdmin() {
		rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
	    List<Banned> allBanns = bannedService.getAll();
	    
	    List<Banned> filteredBanns = allBanns.stream()
	            .filter(bann -> bann.getByGroupAdmin()!=null) 
	            .collect(Collectors.toList());
	    
	    List<Banned> filteredBanns2 = filteredBanns.stream()
	            .filter(bann -> bann.getByGroupAdmin().getId()==currentUser.getId()) 
	            .collect(Collectors.toList());
	    logger.info("Vracena lista banova od strane admina grupe");
	    return new ResponseEntity<>(filteredBanns2, HttpStatus.OK);
	}


 	 
 	 
 	 @GetMapping("/reportsGrAdmin")
     public ResponseEntity<List<Report>> getAllReports2() throws Exception {
 		rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
 	    Set<Group> userGroups = currentUser.getGroups();
 	    
 	    List<Group> gr=groupService.getAll();
 	    for(Group g:gr) {
 	    	if(g.getAddedGroupAdmins().contains(currentUser)) {
 	    		userGroups.add(g);
 	    	}
 	    }

 	    List<Report> allReports = reportService.getAll();
 	    List<Report> filteredReports = new ArrayList<>();

 	    for (Report report : allReports) {
 	        if (belongsToUserGroup(report, userGroups)) {
 	            filteredReports.add(report);
 	        }
 	    }
 	    List<Report> filteredReports2 = filteredReports.stream()
	            .filter(report -> !report.getAccepted()) 
	            .collect(Collectors.toList());
 	    logger.info("Vracena lista reportova ");
 	    return new ResponseEntity<>(filteredReports2, HttpStatus.OK);
     }
 	 
 	private boolean belongsToUserGroup(Report report, Set<Group> userGroups) throws Exception {
 	    if (report.getReported2() != null) {
 	        Post post = report.getReported2();
 	        if (post.getGroup() != null && userGroups.contains(post.getGroup())) {
 	            return true;
 	        }
 	    }
 	    
 	    if (report.getReported3() != null) {
 	        Comment comment = report.getReported3();
 	        Post parentPost = comment.getPost();
 	        if (parentPost != null && parentPost.getGroup() != null && userGroups.contains(parentPost.getGroup())) {
 	        	return true;
 	        }
 	    }
 	    
 	    return false;
 	}
 	
 	  @PutMapping("/reports/{id}")
 		@CrossOrigin(origins = "http://4200")
 		@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
 		public ResponseEntity<Report> edit(@RequestBody ReportDTO editPost) throws Exception {
 		  

 		        Report edit = reportService.findReport(editPost.getId());

 		        edit.setAccepted(editPost.getAccepted());

 		        reportService.save(edit);


 		        Report p=reportService.findReport(edit.getId());
 		       logger.info("Prihvacen report");   
 	        return new ResponseEntity<>(p, HttpStatus.OK);
 		
 		}
 		
 	    @CrossOrigin(origins = "http://4200", maxAge = 3600)
 	    @DeleteMapping("/reports/{id}")
 	    public ResponseEntity<Report> deleteReport(@PathVariable Long id) {
 	       /* boolean deleted =*/ reportService.delete(id);
 	       Report post = reportService.findReport(id);
 		   //post.setDeleted(true);
 		    reportService.delete(id);
 		    return new ResponseEntity<>(post, HttpStatus.OK);
// 	        if (deleted) {
// 	            return ResponseEntity.noContent().build();
// 	        } else {
// 	            return ResponseEntity.notFound().build();
// 	        }
 	    }
     @CrossOrigin(origins = "http://4200", maxAge = 3600)
     @GetMapping("/reports/{id}")
     public ResponseEntity<Report> getReportById(@PathVariable Long id) {
         Report report = reportService.findReport(id);
         if (report != null) {
        	 logger.info("Vracena report po ID-u");
             return ResponseEntity.ok(report);
         } else {
             return ResponseEntity.notFound().build();
         }
     }
 	@Transactional
 	public void deleteImage(Long imageId) {
 	    imageRepository.deleteById(imageId);
 	}
 	 @CrossOrigin(origins = "http://4200", maxAge = 3600)
     @PostMapping("/report")
     public ResponseEntity<Report> createReport(@RequestBody ReportDTO report) {
 		 rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
 		 logger.info(report.getReportReason());
 		 if(report.getReportReason().equals(ReportReason.BREAKES_RULES)) {
 			 report.setReportReason(ReportReason.BREAKES_RULES);
 		 }else if(report.getReportReason().equals(ReportReason.COPYRIGHT_VIOLATION)) {
 			 report.setReportReason(ReportReason.COPYRIGHT_VIOLATION);
 		 }else if(report.getReportReason().equals(ReportReason.HARASSMENT)) {
 			 report.setReportReason(ReportReason.HARASSMENT);
 		 }else if(report.getReportReason().equals(ReportReason.HATE)) {
 			 report.setReportReason(ReportReason.HATE);
 		 }else if(report.getReportReason().equals(ReportReason.IMPERSONATION)) {
 			 report.setReportReason(ReportReason.IMPERSONATION);
 		 }else if(report.getReportReason().equals(ReportReason.OTHER)) {
 			 report.setReportReason(ReportReason.OTHER);
 		 }else if(report.getReportReason().equals(ReportReason.SELF_HARM_OR_SUICIDE)) {
 			 report.setReportReason(ReportReason.SELF_HARM_OR_SUICIDE);
 		 }else if(report.getReportReason().equals(ReportReason.SHARING_PERSONAL_INFORMATION)) {
 			 report.setReportReason(ReportReason.SHARING_PERSONAL_INFORMATION);
 		 }else if(report.getReportReason().equals(ReportReason.SPAM)) {
 			 report.setReportReason(ReportReason.SPAM);
 		 }else if(report.getReportReason().equals(ReportReason.TRADEMARK_VIOLATION)) {
 			 report.setReportReason(ReportReason.TRADEMARK_VIOLATION);
 		 }
 		 report.setByUser(currentUser);
 		 if (report.getReported2Id()!=null) {
 			Post p = postService.findPost(report.getReported2Id());
 	 		report.setReported2(p);
 		 }
 		 else if (report.getReported3Id()!=null) {
 			Comment c = commService.findComment(report.getReported3Id());
 	 		report.setReported3(c);}
 		 report.setAccepted(false);
 		Report createdReport=new Report();
 		 if(report.getReported()!=null) {
	 		 List<Banned> banned=bannedService.getAll();
	 		 boolean isBanned=false;
	 		 for (Banned b:banned) {
	 			 if(b.getTowards().getId().equals(report.getReported().getId())) {
	 				 isBanned=true; 
	 				 createdReport=null;
	 				 }
	 		  }
	 		 if(!isBanned) {
	 			createdReport = reportService.createReport(report);
	 		 }
 		 }else {createdReport = reportService.createReport(report);
 		 }
 		logger.info("Napravljen report");
 		return ResponseEntity.status(HttpStatus.OK).body(createdReport);
     }

 	@JsonIgnoreProperties({"suspendedReason"})
    @GetMapping("/all")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Group>> getAll(){
    	List<Group> l=groupService.getAll();
    	List<Group> list=new ArrayList<Group>();
    	for (Group g:l) {
    		if (!g.isSuspended() &&  !g.isDeleted()) {
    			list.add(g);
    		}
    	}
    	logger.info("Vraca sve grupe");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
 	@GetMapping("/nisuuclanjeni")
 	public ResponseEntity<List<Group>> getGrupeNisuUclanjeni() {
 	    rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());

 	    List<Group> sveGrupe = groupService.getAll();
 	    List<Group> grupeNisuUclanjeni = new ArrayList<>();

 	    for (Group grupa : sveGrupe) {
 	        if (grupa.getGroupAdmin()!=null && grupa.getGroupAdmin().getId() == currentUser.getId()) {
 	            // Preskoči grupu ako je trenutni korisnik admin
 	            continue;
 	           
 	        }
 	       if (grupa.getAddedGroupAdmins().contains(currentUser)) {
	            // Preskoči grupu ako je trenutni korisnik admin
	            continue;
	           
	        }

 	        if (grupa.isSuspended() || grupa.isDeleted()) {
 	            // Preskoči grupu ako je suspendovana
 	        	
 	            continue;
 	            
 	        }

 	        if (!grupa.getMembers().contains(currentUser)) {
 	            grupeNisuUclanjeni.add(grupa);
 	           
 	        }
 	    }
 	    
 	   logger.info("Vracena listu grupa u kojima user nije");
 	    return new ResponseEntity<>(grupeNisuUclanjeni, HttpStatus.OK);
 	}

    @GetMapping("/uclanjeni")
    @Transactional
 	public ResponseEntity<List<Group>> getGrupeUclanjeni( ) throws Exception {
    	try{
 		rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
     	
 	    List<Group> grupeUclanjeni = new ArrayList<>();
 	    List<Group> sveGrupe=groupService.getAll();
 	    for (Group grupa : sveGrupe) {
 	    
 	        if (grupa.isSuspended() || grupa.isDeleted()) {
	            continue;
	        }
 	    	 
 	       if ((grupa.getGroupAdmin()!=null && grupa.getGroupAdmin().getId() == currentUser.getId()) && !grupa.isDeleted() && !grupa.isSuspended()) {
 	    	  grupeUclanjeni.add(grupa);
	        }

	        if (((grupa.getMembers().contains(currentUser) || grupa.getAddedGroupAdmins().contains(currentUser))) && !grupa.isDeleted() && !grupa.isSuspended()) {
	        	grupeUclanjeni.add(grupa);
	        }
 	    }
 	    logger.info("Vracena lista grupa u kojima se user nalazi");
 	    return new ResponseEntity<>(grupeUclanjeni, HttpStatus.OK);
    }catch(Exception e) {
    	throw new Exception(e.getMessage());
    }
    	
 	}
    
    @GetMapping("/uclanjeni/profil/{id}")
    @Transactional
 	public ResponseEntity<List<Group>> getGrupeUclanjeni2(@PathVariable Integer id  ) throws Exception {
    	try{
 		java.util.Optional<rs.ac.uns.ftn.svtvezbe07.model.entity.User> u=userService.findById(id);
 		rs.ac.uns.ftn.svtvezbe07.model.entity.User user=u.get();
 	    List<Group> grupeUclanjeni = new ArrayList<>();
 	    List<Group> sveGrupe=groupService.getAll();
 	    for (Group grupa : sveGrupe) {
 	    	 
 	        if (grupa.isSuspended() || grupa.isDeleted()) {
	            continue;
	        }
 	    	 
 	       if ((grupa.getGroupAdmin()!=null && grupa.getGroupAdmin().getId() == user.getId()) && !grupa.isDeleted() && !grupa.isSuspended()) {
 	    	  grupeUclanjeni.add(grupa);
	        }

	        if (((grupa.getMembers().contains(user) || grupa.getAddedGroupAdmins().contains(user))) && !grupa.isDeleted() && !grupa.isSuspended()) {
	        	grupeUclanjeni.add(grupa);
	        }
 	    }
 	   logger.info("Vracena lista grupa usera");
 	    return new ResponseEntity<>(grupeUclanjeni, HttpStatus.OK);
    }catch(Exception e) {
    	throw new Exception(e.getMessage());
    }
    	
 	}

    @GetMapping("/admin")
    public ResponseEntity<List<Group>> getGrupe() {
        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());

        List<Group> gr = new ArrayList<>();
        List<Group> sveGrupe = groupService.getAll();

        for (Group grupa : sveGrupe) {
            if ((grupa.getGroupAdmin()!=null && grupa.getGroupAdmin().equals(currentUser)) || grupa.getAddedGroupAdmins().contains(currentUser)) {
            	if(!grupa.isDeleted() &&!grupa.isSuspended()) {
            		gr.add(grupa);
            	}
                
            }
        }
        logger.info("Vracena lista grupa");
        return new ResponseEntity<>(gr, HttpStatus.OK);
    }

    @GetMapping("/nisuuclanjeni/{id}")
    public ResponseEntity<Group> getOneN(@PathVariable Long id) throws Exception {
        // Bacanje izuzetka "vgf"
        // throw new Exception("vgf");

        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());

        List<Group> sveGrupe = groupService.getAll();

        Map<Long, Group> grupaMap = sveGrupe.stream().collect(Collectors.toMap(Group::getId, Function.identity()));

        if (grupaMap.containsKey(id)) {
            Group desiredGroup = grupaMap.get(id);
            if (!desiredGroup.getMembers().contains(currentUser) && !desiredGroup.isSuspended() && !desiredGroup.isDeleted()) {
                logger.info("Vracena grupa");
            	return new ResponseEntity<>(desiredGroup, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    @GetMapping("/uclanjeni/{id}")
    public ResponseEntity<Group> getOneU(@PathVariable Long id) throws Exception {
        // Bacanje izuzetka "vgf"
        // throw new Exception("vgf");

        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());

        List<Group> sveGrupe = groupService.getAll();

       
        Map<Long, Group> grupaMap = sveGrupe.stream().collect(Collectors.toMap(Group::getId, Function.identity()));

      
        if (grupaMap.containsKey(id)) {
            Group desiredGroup = grupaMap.get(id);
            if (/*desiredGroup.getMembers().contains(currentUser) &&*/ !desiredGroup.isSuspended() && !desiredGroup.isDeleted()) {
                logger.info("Vracena grupa");
            	return new ResponseEntity<>(desiredGroup, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

 	
    @GetMapping("/allPosts")
  //@PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Post>> getAllP(@RequestParam Long  id){

  	  // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      // String username = authentication.getName();
       rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
       

       Group group = groupService.findGroup(id);
       if (group != null) {
           rs.ac.uns.ftn.svtvezbe07.model.entity.User groupAdmin = group.getGroupAdmin();
           Set<rs.ac.uns.ftn.svtvezbe07.model.entity.User> groupMembers = group.getMembers();
           Set<rs.ac.uns.ftn.svtvezbe07.model.entity.User> addedGroupAdmins = group.getAddedGroupAdmins();

           if (groupMembers != null || addedGroupAdmins != null) {
               for (rs.ac.uns.ftn.svtvezbe07.model.entity.User member : groupMembers) {
                   if (member.getId().equals(currentUser.getId())) {
                       Set<Post> groupPosts = group.getPosts(); 

                       List<Post> posts = new ArrayList<>(); 

                       if (groupPosts != null) {
                    	   for (Post p : groupPosts) {
                               if (!p.isDeleted()) {
                                   posts.add(p);
                               }
                           }
                           logger.info("Vracena lista postova");
                           return new ResponseEntity<>(posts, HttpStatus.OK);
                       }
                       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                   }
               }
               
               for (rs.ac.uns.ftn.svtvezbe07.model.entity.User addedAdmin : addedGroupAdmins) {
                   if (addedAdmin.getId().equals(currentUser.getId())) {
                       List<Post> nova = new ArrayList<>();
                       Set<Post> groupPosts = group.getPosts();
                       
                       if (groupPosts != null) {
                           for (Post p : groupPosts) {
                               if (!p.isDeleted()) {
                                   nova.add(p);
                               }
                           }
                           logger.info("Vracena lista postova");
                           return new ResponseEntity<>(nova, HttpStatus.OK);
                       }
                       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                   }
               }
           }
           if (groupAdmin != null && groupAdmin.getId().equals(currentUser.getId())) {
          	    List<Post> nova = new ArrayList<>();
          	    Set<Post> groupPosts = group.getPosts();
          	    if (groupPosts != null) {
          	        for (Post p : groupPosts) {
          	            if (!p.isDeleted()) {
          	                nova.add(p);
          	            }
          	        }
          	        logger.info("Vracena lista postova");
          	        return new ResponseEntity<>(nova, HttpStatus.OK);
          	    }
          	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          	}
       }
      
       return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
   }

    
    @GetMapping("/allPosts/your")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Post>> getAllYour(@RequestParam Long  id){

    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = authentication.getName();
         rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
         
         
         Group group = groupService.findGroup(id);
         if (group != null) {
             rs.ac.uns.ftn.svtvezbe07.model.entity.User groupAdmin = group.getGroupAdmin();
             Set<rs.ac.uns.ftn.svtvezbe07.model.entity.User> groupAdmins = group.getAddedGroupAdmins();
             if (groupAdmin != null && groupAdmin.getId().equals(currentUser.getId())) {
            	    List<Post> nova = new ArrayList<>();
            	    Set<Post> groupPosts = group.getPosts();
            	    if (groupPosts != null) {
            	        for (Post p : groupPosts) {
            	            if (!p.isDeleted()) {
            	                nova.add(p);
            	            }
            	        }
            	        logger.info("Vracena lista tvojih postova");
            	        return new ResponseEntity<>(nova, HttpStatus.OK);
            	    }
            	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            	}
             if (groupAdmins != null) {
            	 for (rs.ac.uns.ftn.svtvezbe07.model.entity.User addedAdmin : groupAdmins) {
                     if (addedAdmin.getId().equals(currentUser.getId())) {
                         List<Post> nova = new ArrayList<>();
                         Set<Post> groupPosts = group.getPosts();
                         
                         if (groupPosts != null) {
                             for (Post p : groupPosts) {
                                 if (!p.isDeleted()) {
                                     nova.add(p);
                                 }
                             }
                             logger.info("Vracena lista tvojih postova");
                             return new ResponseEntity<>(nova, HttpStatus.OK);
                         }
                         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                     }
                 }
         	}

//
//             if (groupMembers != null) {
//                 for (rs.ac.uns.ftn.svtvezbe07.model.entity.User member : groupMembers) {
//                     if (member.getUsername().equals(username)) {
//                         Set<Post> groupPosts = group.getPosts();
//                         if (groupPosts != null) {
//                             return new ResponseEntity<>(groupPosts, HttpStatus.OK);
//                         }
//                         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                     }
//                 }
//             }
         }

         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
     }
    
    
    @GetMapping("/all/user")
    public ResponseEntity<List<Group>> getAllUsers() {
        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
        if (currentUser != null) {
            List<Group> allGroups = groupService.getAll(); 
            List<Group> userGroups = allGroups.stream()
                .filter(group ->(group.getGroupAdmin()!=null && group.getGroupAdmin().equals(currentUser)) || group.getAddedGroupAdmins().contains(currentUser))
                .filter(group -> !group.isDeleted())
                .filter(group -> !group.isSuspended())
                .collect(Collectors.toList());
            logger.info("Vracena lista grupa usera");
            return new ResponseEntity<>(userGroups, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    

   /* @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.GroupService.findAll();
    }
*/
    @Transactional
    @DeleteMapping("/deleteGroupRequest/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupRequest> deleteGroupRequest(@PathVariable Long id) {
        try {
        	GroupRequest gr=groupRequestService.findGroupRequest(id);
        	gr.setApproved(false);
        	gr.setAt(LocalDateTime.now());
        	gr.setDeleted(true);
            groupRequestService.save(gr);
            logger.info("Odbijen zahtev za grupu");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    @PostMapping("/createRequest/{id}")
    public ResponseEntity<GroupRequest> createGroupRequest(@PathVariable  Long id) {
        try {
            Group group = groupService.findGroup(id);
            if (group != null) {
                
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User u = (User) auth.getPrincipal();
                rs.ac.uns.ftn.svtvezbe07.model.entity.User user = userService.findByUsername(u.getUsername());

                if (group.getMembers().contains(user) || group.getAddedGroupAdmins().contains(user) ||(group.getGroupAdmin()!=null && group.getGroupAdmin().equals(user))) {
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                GroupRequest groupRequest = new GroupRequest();
                groupRequest.setApproved(false);
                groupRequest.setDeleted(false);
                groupRequest.setCreatedAt(LocalDateTime.now());
                groupRequest.setUser_id(user);
                groupRequest.setGroup(group);

                GroupRequest createdGroupRequest = groupRequestService.save(groupRequest);
                logger.info("Napravljen zahtev za grupu");
                return new ResponseEntity<>(createdGroupRequest, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/groupRequests/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    public ResponseEntity<List<GroupRequest>> getAllGroupRequests() {
        List<GroupRequest> groupRequests = groupRequestService.getAll();

        List<GroupRequest> pendingGroupRequests = new ArrayList<>();
        for (GroupRequest request : groupRequests) {
            if (request.isApproved() || request.isDeleted()) {
               continue;
            }else { pendingGroupRequests.add(request);}
          
        }
        logger.info("Vracena lista zahteva za grupu");
        return new ResponseEntity<>(pendingGroupRequests, HttpStatus.OK);
    }
    
    @PostMapping("/groupRequests/all/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUPADMIN')")
    public ResponseEntity<List<GroupRequest>> getAllGroupRequests(@PathVariable  Long id) {
    	Group grupa=groupService.findGroup(id);
        List<GroupRequest> groupRequests = groupRequestService.findAllByGroup(id);
        
        List<GroupRequest> pendingGroupRequests = new ArrayList<>();
        for (GroupRequest request : groupRequests) {
            if (request.isApproved() || request.isDeleted())  {
               continue;
            }else { pendingGroupRequests.add(request);}
        }
        logger.info("Vracena lista zahteva za grupu");
        return new ResponseEntity<>(pendingGroupRequests, HttpStatus.OK);
    }
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUPADMIN')")
    public ResponseEntity<GroupRequest> approveGroupRequest(@PathVariable Long id) {
        try {
            GroupRequest groupRequest = groupRequestService.findGroupRequestById(id);
            if (groupRequest != null) {
                groupRequest.setApproved(true);
                groupRequest.setAt(LocalDateTime.now());
                GroupRequest approvedGroupRequest = groupRequestService.save(groupRequest);

                
                Group group = approvedGroupRequest.getGroup();
                rs.ac.uns.ftn.svtvezbe07.model.entity.User user = approvedGroupRequest.getUser_id();

                if (group.getMembers().contains(user) || group.getAddedGroupAdmins().contains(user) || (group.getGroupAdmin()!=null && group.getGroupAdmin().equals(user))) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
                }

                group.getMembers().add(user);
                groupService.save(group);
                logger.info("Prihvacen zahtev za grupu");
                return new ResponseEntity<>(approvedGroupRequest, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Group> getOne(@PathVariable Long id) throws Exception{
    	//throw new Exception("vgf");
    	//Long id2 = Long.parseLong(id);
    	//int id3 =Integer.parseInt(id);
       Group Group = groupService.findGroup(id);
        if(Group!=null && !Group.isSuspended() ){
        	if(!Group.isDeleted()) {
        		logger.info("Vracena grupa");
            return new ResponseEntity<>(Group, HttpStatus.OK);}
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }
    
 
    
 
	@PostMapping("/create")
    public ResponseEntity<Group> create(@RequestBody @Validated GroupDTO newGroup) throws Exception{
//			if (newGroup!=null) {
//				throw new Exception("vg");
//			}
//			if (newGroup==null) {
//				throw new Exception("gv");
//			}
    	 	Group Group = new Group();
    	 	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        User u = (User) auth.getPrincipal();
	        rs.ac.uns.ftn.svtvezbe07.model.entity.User user= userService.findByUsername(u.getUsername());
	        Group.setName(newGroup.getName());
	        Group.setCreationDate(LocalDateTime.now());
	        Group.setDescription(newGroup.getDescription());
	        Group.setGroupAdmin(user);
	        Group.setSuspended(false);
	        Group.setDeleted(false);
	        if(user.getRole().equals(Roles.USER)) {
	        	user.setRole(Roles.GROUPADMIN);
	        }
	        
	        userService.save(user);
	        
	    Group createdGroup = groupService.save(Group);//GroupService.createGroup(newGroup);
        if(createdGroup == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        Group p=groupService.findByGroup(createdGroup);
        logger.info("Vracena upravo napravljena grupa");
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

	@DeleteMapping("/delete")
	@Transactional
	@PreAuthorize("hasAnyRole('USER', 'GROUPADMIN', 'ADMIN')")
	public ResponseEntity<Group> delete(@RequestParam("id") Long id) {
	    Group group = groupService.findGroup(id);
	    if (group == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	    Set<Post> posts = group.getPosts();

	    for (Post post : posts) {
	        postService.delete(post.getId());
	    }
	    group.setDeleted(true);
	    groupService.save(group);
	    return new ResponseEntity<>(new Group(), HttpStatus.OK);
	}

	
	@DeleteMapping("/suspend")
	@Transactional
	@PreAuthorize("hasAnyRole( 'ADMIN')")
	//@CrossOrigin()
	public ResponseEntity<Group> delete2(@RequestBody GroupDTO g) throws Exception {
		Long l=Long.valueOf(g.getId());
		Group edit = groupService.findGroup(l);
        if(edit==null) {
        	throw new Exception("d");
        }

        edit.setSuspended(true);
        edit.setSuspendedReason(g.getSuspendedReason());
        edit.setGroupAdmin(null);
        edit.setAddedGroupAdmins(null);
        edit.setAddedGroupAdmins(new HashSet<>());
        groupService.save(edit);
        Group p=groupService.findByGroup(edit);
        logger.info("Suspendovana grupa");
        return new ResponseEntity<>(p, HttpStatus.OK);

	}
	
	@PostMapping("/report/suspendByGrAdmin/{idUsera}/{idGrupe}")
	//@PreAuthorize("hasAnyRole( 'GROUPADMIN')")
	//@CrossOrigin()
	public ResponseEntity<Banned> blockByGroupAdmin(@PathVariable Long idUsera,@PathVariable Long idGrupe) throws Exception {
	
		rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
		Banned banUser=new Banned();
		banUser.setByGroupAdmin(currentUser);
		Integer idUsera2=idUsera.intValue();
		java.util.Optional<rs.ac.uns.ftn.svtvezbe07.model.entity.User> u = userService.findById(idUsera2);
		banUser.setTowards(u.get());
		Group g = groupService.findGroup(idGrupe);
		banUser.setGroup(g);
		bannedService.save(banUser);
		g.getMembers().remove(u.get());
		groupService.save(g);
		logger.info("Banovan korisnik u grupi");
		return new ResponseEntity<>(banUser, HttpStatus.OK);
	}
	
	@PostMapping("/report/suspend")
	@Transactional
	@PreAuthorize("hasAnyRole( 'ADMIN','GROUPADMIN')")
	//@CrossOrigin()
	public ResponseEntity<Report> suspend(@RequestBody ReportDTO r,@RequestParam (required=false) Long idGrupe) throws Exception {
		//Long l=Long.valueOf(r.getId());
		Report report = reportService.findReport(r.getId());
		rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
  		
		if(report.getReported()!=null) {
			if(currentUser.getRole().equals(Roles.GROUPADMIN)) {
				//Blokiranje korisnika grupe-->
				rs.ac.uns.ftn.svtvezbe07.model.entity.User u = report.getReported();
				Banned banUser=new Banned();
				banUser.setByGroupAdmin(currentUser);
				banUser.setTowards(u);
				Group g = groupService.findGroup(idGrupe);
				banUser.setGroup(g);
				bannedService.save(banUser);
			}else if(currentUser.getRole().equals(Roles.ADMIN)) {
				//Blokiranje korisnika na nivou cele aplikacije
				boolean korisnikJeVecBanovan = false;
				rs.ac.uns.ftn.svtvezbe07.model.entity.User u = report.getReported();
				List <Banned> listaBanovanih=bannedService.getAll();

				for (Banned b : listaBanovanih) {
				    if (b.getTowards().equals(r.getReported())) {
				    	korisnikJeVecBanovan = true;			        
				    } 
				}
				if (!korisnikJeVecBanovan) {
			    	Banned banUser = new Banned();
			        banUser.setByAdmin(currentUser);
			        banUser.setTowards(u);
			        bannedService.save(banUser);
			        List<Report> reportiZaKorisnika=reportService.getAll();
			        for(Report re:reportiZaKorisnika) {
			        	if(re.getReported()!=null && re.getReported().equals(u)) {
			        		re.setAccepted(true);
			        		reportService.save(re);
			        	}
			        }
				}
			}		
		}
		
		
		if(report.getReported2()!=null) {
			Post p=report.getReported2();
			postService.delete(p.getId());
			postService.save(p);
		}else if(report.getReported3()!=null) {
			Comment c=report.getReported3();
			commService.delete(c.getId());
			commService.save(c);
			postService.save(c.getPost());
		}
		
		report.setAccepted(true);
		Report rep=reportService.save(report);
		logger.info("Prihvacen report");

        return new ResponseEntity<>(rep, HttpStatus.OK);

	}
	
	@PutMapping("/unban/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> unbanUser(@PathVariable Integer userId) {
	    java.util.Optional<rs.ac.uns.ftn.svtvezbe07.model.entity.User> user = userService.findById(userId);
	    
	    if (user != null) {
	    	Banned banned = bannedRepository.findBannedByTowards(user.get());
	        bannedRepository.delete(banned);
	        logger.info("Odbanovan user");
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	@PutMapping("/unban2/{userId}/{groupId}")
	@PreAuthorize("hasAnyRole('GROUPADMIN','ADMIN')")
	public ResponseEntity<Void> unbanUserByGrAdmin(@PathVariable Integer userId,@PathVariable Long groupId) {
	    java.util.Optional<rs.ac.uns.ftn.svtvezbe07.model.entity.User> user = userService.findById(userId);
	    rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
	       
	    if (user != null) {
	    	List<Banned> allBanns=bannedService.getAll();
	    	
	    	List<Banned> filteredBanns = allBanns.stream()
	  	            .filter(bann -> bann.getTowards().getId().equals(user.get().getId())) 
	  	            .collect(Collectors.toList());
	    	
	    	Banned filteredBann = filteredBanns.stream()
	  	            .filter(bann -> bann.getGroup().getId().equals(groupId)) 
	  	            .findFirst()
		    	    .orElse(null);
	    	
	    	if(filteredBann.getByGroupAdmin().equals(currentUser)) {
	    		Group g=filteredBann.getGroup();
		    	g.getMembers().add(user.get());
		    	groupService.save(g);
		    	logger.info("Odbanovan user");
		        bannedRepository.delete(filteredBann);
	    	}
	    	
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	

	@PutMapping("/edit")
	@CrossOrigin(origins = "http://4200")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
	public ResponseEntity<Group> edit(@RequestBody GroupDTO editGroup) throws Exception {
    try {
    		Long l = Long.valueOf(editGroup.getId());
	        Group edit = groupService.findGroup(l);
	        if(edit==null) {
	        	throw new Exception("d");
	        }

	        edit.setName(editGroup.getName());
	        edit.setDescription(editGroup.getDescription());
//	        edit.setSuspended(editGroup.isSuspended());
	        edit.setDeleted(false);
//	        edit.setSuspendedReason(editGroup.getSuspendedReason());
	        groupService.save(edit);
	        


	        Group p=groupService.findGroupByName(edit.getName());
	        logger.info("Editovana grupa");
	        return new ResponseEntity<>(p, HttpStatus.OK);
	    } catch (Exception e) {
		       return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
	
	@PostMapping("/addGroupAdmin/{groupId}/{userId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'GROUPADMIN')")
	public ResponseEntity<Group> addGroupAdminToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
	    try {
	        Group group = groupService.findGroup(groupId);
	        
	        Integer intv=userId.intValue();
	        java.util.Optional<rs.ac.uns.ftn.svtvezbe07.model.entity.User> userToAdd = userService.findById(intv);
	        

	        rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
	        
	        group.getAddedGroupAdmins().add(userToAdd.get());
	        group.getMembers().remove(userToAdd.get());
	        userToAdd.get().setRole(Roles.GROUPADMIN);
	       // userToAdd.get().getGroups().add(group);
	        userService.save(userToAdd.get());
	        groupService.save(group);
	        logger.info("Dodat group admin grupi");
	        return new ResponseEntity<>(group, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PostMapping("/removeGroupAdmin/{groupId}/{userId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'GROUPADMIN')")
	public ResponseEntity<Group> removeGrAdmin(@PathVariable Long groupId, @PathVariable Long userId) throws Exception {
	    try {
	        Group group = groupService.findGroup(groupId);
	        
	        Integer intv=userId.intValue();
	        java.util.Optional<rs.ac.uns.ftn.svtvezbe07.model.entity.User> userToAdd = userService.findById(intv);
	        
	        if(group.getGroupAdmin()!=null && group.getGroupAdmin().getId()==intv) {
	        	group.setGroupAdmin(null);
	        	userToAdd.get().getGroups().remove(group);
	        }
	        else if(group.getAddedGroupAdmins()!=null) {
	        	for(rs.ac.uns.ftn.svtvezbe07.model.entity.User u:group.getAddedGroupAdmins()) {
	        		if(u.getId()==intv) {
	        			group.getAddedGroupAdmins().remove(userToAdd.get());
	        		}
	        	}
	        }     
	        groupService.save(group);
	        
	        List<Group> allGroups = groupService.getAll();
	        Set<rs.ac.uns.ftn.svtvezbe07.model.entity.User> users=new HashSet<>();
	        boolean yes=false;
		     for (Group group2 : allGroups) {
		         if (group2.getGroupAdmin()!=null && group2.getGroupAdmin().getId() == userToAdd.get().getId()) {
		             yes=true;
		         }

		         boolean isUserAddedAdmin = group2.getAddedGroupAdmins().stream()
		                 .anyMatch(admin -> admin.getId() == userToAdd.get().getId());
	
		         if (isUserAddedAdmin) {
		             users.add(userToAdd.get());
		         }
		     }
		     if(!yes && !users.contains(userToAdd.get()) ) {
		    	 userToAdd.get().setRole(Roles.USER);
		     }
	        
	        userService.save(userToAdd.get());
	        if(group.getGroupAdmin()==null && group.getAddedGroupAdmins().isEmpty()) {
	        	group.setDeleted(true);
	        	groupService.save(group);
	        }
	        logger.info("Izbacen group admin");
	        return new ResponseEntity<>(group, HttpStatus.OK);
	    } catch (Exception e) {
	    	throw new Exception(e.getMessage());
	        //return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
}
