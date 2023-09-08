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
 	private BannedRepository bannedRepository;
 	
 	 @GetMapping("/random")
     public ResponseEntity<List<Post>> getRandomPosts() {
         logger.info("helooo");
         List<Post> allPosts = postService.getAll();
        
         rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
  		
         //grupe koje je on napravio
         Set<Group> userGroups = currentUser.getGroups();
         
         List<Group> allGroups = groupService.getAll();

         for (Group g:allGroups){
        	 boolean a=userBelongsToGroup(currentUser,g);
        	 if(a==true) {
        		 userGroups.add(g);
        	 }
         }
         
         List<Post> allGroupAndPublicPosts = new ArrayList<>();

         
         for (Post post : allPosts) {
             if (post.getGroup() != null && userGroups.contains(post.getGroup()) && post.isDeleted()==false) {
                 allGroupAndPublicPosts.add(post);
             } else if (post.getGroup() == null && post.isDeleted()==false) {
                 allGroupAndPublicPosts.add(post);
             }
         }
         Random random = new Random();
         for (int i = allGroupAndPublicPosts.size() - 1; i > 0; i--) {
             int j = random.nextInt(i + 1);
             Post temp = allGroupAndPublicPosts.get(i);
             allGroupAndPublicPosts.set(i, allGroupAndPublicPosts.get(j));
             allGroupAndPublicPosts.set(j, temp);
         }

         return new ResponseEntity<>(allGroupAndPublicPosts, HttpStatus.OK);
     }

	private boolean userBelongsToGroup(rs.ac.uns.ftn.svtvezbe07.model.entity.User user, Group group) {
 	    Set<rs.ac.uns.ftn.svtvezbe07.model.entity.User> groupMembers = group.getMembers();
 	    return groupMembers.contains(user);
 	}

	@GetMapping("/reports")
	public ResponseEntity<List<Report>> getAllReports() {
	    List<Report> allReports = reportService.getAll();
	    
	    List<Report> filteredReports = allReports.stream()
	            .filter(report -> !report.getAccepted()) 
	            .collect(Collectors.toList());
	    
	    logger.info(filteredReports);
	    
	    return new ResponseEntity<>(filteredReports, HttpStatus.OK);
	}
	
	@GetMapping("/banns")
	@PreAuthorize("hasAnyRole( 'ADMIN')")
	public ResponseEntity<List<Banned>> getAllBanns() {
	    List<Banned> allBanns = bannedService.getAll();
	    
	    logger.info(allBanns);
	    
	    return new ResponseEntity<>(allBanns, HttpStatus.OK);
	}

 	 
 	 
 	 @GetMapping("/reportsGrAdmin")
     public ResponseEntity<List<Report>> getAllReports2() throws Exception {
 		rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
 	    Set<Group> userGroups = currentUser.getGroups();

 	    List<Report> allReports = reportService.getAll();
 	    List<Report> filteredReports = new ArrayList<>();

 	    for (Report report : allReports) {
 	        if (belongsToUserGroup(report, userGroups)) {
 	            filteredReports.add(report);
 	        }
 	    }
 	    logger.info("greska2"+filteredReports);
 	    List<Report> filteredReports2 = filteredReports.stream()
	            .filter(report -> !report.getAccepted()) 
	            .collect(Collectors.toList());
 	    
 	    return new ResponseEntity<>(filteredReports2, HttpStatus.OK);
     }
 	 
 	private boolean belongsToUserGroup(Report report, Set<Group> userGroups) throws Exception {
 	    if (report.getReported2() != null) {
 	    	logger.info("greska3");
 	        Post post = report.getReported2();
 	       logger.info(post.getGroup());
 	       logger.info(userGroups);
 	        if (post.getGroup() != null && userGroups.contains(post.getGroup())) {
 	        	logger.info("greska4");
 	            return true;
 	        }
 	    }
 	    
 	    if (report.getReported3() != null) {
 	    	logger.info("greska5");
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
 		        if(edit==null) {
 		        	throw new Exception("d");
 		        }

 		        edit.setAccepted(editPost.getAccepted());

 		        reportService.save(edit);


 		        Report p=reportService.findReport(edit.getId());

 	        return new ResponseEntity<>(p, HttpStatus.OK);
 		
 		}
 		
 	    @CrossOrigin(origins = "http://4200", maxAge = 3600)
 	    @DeleteMapping("/reports/{id}")
 	    public ResponseEntity<Report> deleteReport(@PathVariable Long id) {
 	       /* boolean deleted =*/ reportService.delete(id);
 	       Report post = reportService.findReport(id);
 		   // post.setDeleted(true);
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
 		 logger.info(report.getReportReason());
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
 		return ResponseEntity.status(HttpStatus.OK).body(createdReport);
     }

 	@JsonIgnoreProperties({"isDeleted","suspended","suspendedReason"})
    @GetMapping("/all")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Group>> getAll(){
    	List<Group> l=groupService.getAll();
    	List<Group> list=new ArrayList<Group>();
    	for (Group g:l) {
    		if (  !g.isDeleted()) {
    			list.add(g);
    		}
    	}
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
 	@GetMapping("/nisuuclanjeni")
 	public ResponseEntity<List<Group>> getGrupeNisuUclanjeni() {
 	    rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());

 	    List<Group> sveGrupe = groupService.getAll();
 	    List<Group> grupeNisuUclanjeni = new ArrayList<>();

 	    for (Group grupa : sveGrupe) {
 	        if (grupa.getGroupAdmin().getId() == currentUser.getId()) {
 	            // Preskoči grupu ako je trenutni korisnik admin
 	        	logger.info("a");
 	            continue;
 	           
 	        }

 	        if (grupa.isSuspended()) {
 	            // Preskoči grupu ako je suspendovana
 	        	logger.info("b");
 	            continue;
 	            
 	        }

 	        if (!grupa.getMembers().contains(currentUser)) {
 	            grupeNisuUclanjeni.add(grupa);
 	           logger.info("c");
 	        }
 	    }
 	    

 	    return new ResponseEntity<>(grupeNisuUclanjeni, HttpStatus.OK);
 	}

    @GetMapping("/uclanjeni")
    @Transactional
 	public ResponseEntity<List<Group>> getGrupeUclanjeni( ) {
 		rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
     	
 	    List<Group> grupeUclanjeni = new ArrayList<>();
 	    List<Group> sveGrupe=groupService.getAll();
 	    for (Group grupa : sveGrupe) {
 	    	 logger.info(grupa.isDeleted()+"controller");
 	       if (grupa.getGroupAdmin().getId() == currentUser.getId() && grupa.isDeleted()==false) {
 	    	  grupeUclanjeni.add(grupa);
	        }

	        if (grupa.isSuspended() || grupa.isDeleted()) {
	            // Preskoči grupu ako je suspendovana
	            continue;
	        }

	        if (grupa.getMembers().contains(currentUser) && grupa.isDeleted()==false) {
	        	grupeUclanjeni.add(grupa);
	        }
 	    }
 	    return new ResponseEntity<>(grupeUclanjeni, HttpStatus.OK);
 	}

    @GetMapping("/admin")
 	public ResponseEntity<List<Group>> getGrupe( ) {
 		rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
     	
 	    List<Group> gr = new ArrayList<>();
 	    List<Group> sveGrupe=groupService.getAll();
 	    for (Group grupa : sveGrupe) {
 	    	 if (grupa.getGroupAdmin().equals(currentUser)) {
 	            gr.add(grupa);
 	        }
 	    }

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
                return new ResponseEntity<>(desiredGroup, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
                return new ResponseEntity<>(desiredGroup, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

 	
    @GetMapping("/allPosts")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Post>> getAllP(@RequestParam Long  id){

    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = authentication.getName();

         Group group = groupService.findGroup(id);
         if (group != null) {
             rs.ac.uns.ftn.svtvezbe07.model.entity.User groupAdmin = group.getGroupAdmin();
             Set<rs.ac.uns.ftn.svtvezbe07.model.entity.User> groupMembers = group.getMembers();

             if (groupMembers != null) {
                 for (rs.ac.uns.ftn.svtvezbe07.model.entity.User member : groupMembers) {
                     if (member.getUsername().equals(username)) {
                         Set<Post> postSet = group.getPosts(); 

                         List<Post> groupPosts = new ArrayList<>(postSet); 

                         if (groupPosts != null) {
                             return new ResponseEntity<>(groupPosts, HttpStatus.OK);
                         }
                         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                     }
                 }
             }
         }
         rs.ac.uns.ftn.svtvezbe07.model.entity.User groupAdmin = group.getGroupAdmin();
         if (groupAdmin != null && groupAdmin.getUsername().equals(username)) {
        	    List<Post> nova = new ArrayList<>();
        	    Set<Post> groupPosts = group.getPosts();
        	    if (groupPosts != null) {
        	        for (Post p : groupPosts) {
        	            if (!p.isDeleted()) {
        	                nova.add(p);
        	            }
        	        }
        	        return new ResponseEntity<>(nova, HttpStatus.OK);
        	    }
        	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        	}
         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
     }
    
    @GetMapping("/allPosts/your")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Post>> getAllYour(@RequestParam Long  id){

    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = authentication.getName();

         Group group = groupService.findGroup(id);
         if (group != null) {
             rs.ac.uns.ftn.svtvezbe07.model.entity.User groupAdmin = group.getGroupAdmin();
             Set<rs.ac.uns.ftn.svtvezbe07.model.entity.User> groupMembers = group.getMembers();

             if (groupAdmin != null && groupAdmin.getUsername().equals(username)) {
            	    List<Post> nova = new ArrayList<>();
            	    Set<Post> groupPosts = group.getPosts();
            	    if (groupPosts != null) {
            	        for (Post p : groupPosts) {
            	            if (!p.isDeleted()) {
            	                nova.add(p);
            	            }
            	        }
            	        return new ResponseEntity<>(nova, HttpStatus.OK);
            	    }
            	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    //grupe kojima je ulogovani korisnik admin
    //@PreAuthorize("hasRole('USER')")
    //@CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Group>> getAllUsers(){
    	 rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
    	    if (currentUser != null) {
    	        List<Group> Groups = groupService.findAllByUser(currentUser);
    	        List<Group> list=new ArrayList<Group>();
    	    	for (Group g:Groups) {
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

                if (group.getMembers().contains(user) || group.getGroupAdmin().equals(user)) {
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                GroupRequest groupRequest = new GroupRequest();
                groupRequest.setApproved(false);
                groupRequest.setDeleted(false);
                groupRequest.setCreatedAt(LocalDateTime.now());
                groupRequest.setUser_id(user);
                groupRequest.setGroup(group);

                GroupRequest createdGroupRequest = groupRequestService.save(groupRequest);

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
            logger.info(request.isApproved()+""+request.isDeleted()+"ss");
        }

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

        return new ResponseEntity<>(pendingGroupRequests, HttpStatus.OK);
    }
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','ADMINGROUP')")
    public ResponseEntity<GroupRequest> approveGroupRequest(@PathVariable Long id) {
        try {
            GroupRequest groupRequest = groupRequestService.findGroupRequestById(id);
            if (groupRequest != null) {
                groupRequest.setApproved(true);
                groupRequest.setAt(LocalDateTime.now());
                GroupRequest approvedGroupRequest = groupRequestService.save(groupRequest);

                
                Group group = approvedGroupRequest.getGroup();
                rs.ac.uns.ftn.svtvezbe07.model.entity.User user = approvedGroupRequest.getUser_id();

                if (group.getMembers().contains(user) || group.getGroupAdmin().equals(user)) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
                }

                group.getMembers().add(user);
                groupService.save(group);

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
            return new ResponseEntity<>(Group, HttpStatus.OK);}
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);

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
	        
	    Group createdGroup = groupService.save(Group);//GroupService.createGroup(newGroup);
        if(createdGroup == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        Group p=groupService.findByGroup(createdGroup);
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
	    groupService.delete(id);
	    List<Group> groups=groupService.getAll();
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
        groupService.save(edit);
        Group p=groupService.findGroupByName(edit.getName());
        return new ResponseEntity<>(p, HttpStatus.OK);

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
		

        return new ResponseEntity<>(rep, HttpStatus.OK);

	}
	
	@PutMapping("/unban/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> unbanUser(@PathVariable Integer userId) {
	    java.util.Optional<rs.ac.uns.ftn.svtvezbe07.model.entity.User> user = userService.findById(userId);
	    
	    if (user != null) {
	    	Banned banned = bannedRepository.findBannedByTowards(user.get());
	        bannedRepository.delete(banned);
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
	        return new ResponseEntity<>(p, HttpStatus.OK);
	    } catch (Exception e) {
	    	logger.info("error: "+e.getMessage());
	    	throw new Exception(e.getMessage());
		       
	        // Izmena nije uspela
	        //return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
	
	
}
