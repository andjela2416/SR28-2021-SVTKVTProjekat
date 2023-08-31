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
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe07.repository.ImageRepository;
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
 	private ImageRepository imageRepository;
 	
 	 @GetMapping("/random")
     public ResponseEntity<List<Post>> getRandomPosts() {
         
         List<Post> allPosts = postService.getAll();

         
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = authentication.getName();
         rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userService.findByUsername(username);

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
 		List<Report> l=reportService.getAll();
 		logger.info(l);
        return new ResponseEntity<>(l, HttpStatus.OK);
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
         Report createdReport = reportService.createReport(report);
         return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
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
 	            continue;
 	        }

 	        if (grupa.isSuspended()) {
 	            // Preskoči grupu ako je suspendovana
 	            continue;
 	        }

 	        if (!grupa.getMembers().contains(currentUser)) {
 	            grupeNisuUclanjeni.add(grupa);
 	        }
 	    }

 	    return new ResponseEntity<>(grupeNisuUclanjeni, HttpStatus.OK);
 	}

    @GetMapping("/uclanjeni")
 	public ResponseEntity<List<Group>> getGrupeUclanjeni( ) {
 		rs.ac.uns.ftn.svtvezbe07.model.entity.User currentUser = userController.user(SecurityContextHolder.getContext().getAuthentication());
     	
 	    List<Group> grupeUclanjeni = new ArrayList<>();
 	    List<Group> sveGrupe=groupService.getAll();
 	    for (Group grupa : sveGrupe) {
 	       if (grupa.getGroupAdmin().getId() == currentUser.getId()) {
 	    	  grupeUclanjeni.add(grupa);
	        }

	        if (grupa.isSuspended() || grupa.isDeleted()) {
	            // Preskoči grupu ako je suspendovana
	            continue;
	        }

	        if (grupa.getMembers().contains(currentUser)) {
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
                         List<Post> groupPosts = (List<Post>) group.getPosts();
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

    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    @PostMapping("/createRequest/{id}")
    public ResponseEntity<GroupRequest> createGroupRequest(@PathVariable  Long id) {
        try {
            Group group = groupService.findGroupById(id);
            if (group != null) {
                
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User u = (User) auth.getPrincipal();
                rs.ac.uns.ftn.svtvezbe07.model.entity.User user = userService.findByUsername(u.getUsername());

                if (group.getMembers().contains(user) || group.getGroupAdmin().equals(user)) {
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                GroupRequest groupRequest = new GroupRequest();
                groupRequest.setApproved(false);
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
    @PostMapping("/groupRequests/all/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUPADMIN')")
    public ResponseEntity<List<GroupRequest>> getAllGroupRequests(@PathVariable  Long id) {
    	Group grupa=groupService.findGroupById(id);
        List<GroupRequest> groupRequests = groupRequestService.findAllByGroup(id);
        
        List<GroupRequest> pendingGroupRequests = new ArrayList<>();
        for (GroupRequest request : groupRequests) {
            if (request.isApproved()) {
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

        Group p=groupService.findGroupByName(Group.getName());
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
	    Group deletedGroup = new Group();
	    deletedGroup.setDeleted(true);
	    return new ResponseEntity<>(deletedGroup, HttpStatus.OK);
	}

	
	@DeleteMapping("/suspend")
	@Transactional
	@PreAuthorize("hasAnyRole( 'ADMIN')")
	//@CrossOrigin()
	public ResponseEntity<Group> delete2(@RequestBody GroupDTO g) throws Exception {
		Long l=Long.valueOf(g.getId());
		logger.info("LOLA"+l.toString()+g.getSuspendedReason());
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
