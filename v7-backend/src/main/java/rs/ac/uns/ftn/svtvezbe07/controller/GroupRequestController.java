package rs.ac.uns.ftn.svtvezbe07.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.svtvezbe07.model.dto.GroupRequestDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe07.service.GroupRequestService;
import rs.ac.uns.ftn.svtvezbe07.service.GroupService;
import rs.ac.uns.ftn.svtvezbe07.service.UserService;

@RestController
@RequestMapping("api/group-requests")
@CrossOrigin(origins = "http://4200", maxAge = 3600)
public class GroupRequestController {

    @Autowired
    private GroupRequestService groupRequestService;

    @Autowired
    private GroupService groupService;

 	private static final Logger logger = LogManager.getLogger(Log4jExample.class);
    @Autowired
    private UserService userService;
    
    @PostMapping("/create/{id}")
    public ResponseEntity<GroupRequest> createGroupRequest(@PathVariable Long id) {
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


    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
    public ResponseEntity<List<GroupRequest>> getAllGroupRequests() {
        List<GroupRequest> groupRequests = groupRequestService.getAll();

        List<GroupRequest> pendingGroupRequests = new ArrayList<>();
        for (GroupRequest request : groupRequests) {
            if (request.isApproved()) {
               continue;
            }else { pendingGroupRequests.add(request);}
        }

        return new ResponseEntity<>(pendingGroupRequests, HttpStatus.OK);
    }

//
//    @GetMapping("/all/user")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<List<GroupRequest>> getAllGroupRequestsForUser() {
//        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        rs.ac.uns.ftn.svtvezbe07.model.entity.User user = userService.findByUsername(currentUser.getUsername());
//        List<GroupRequest> groupRequests = groupRequestService.getAllGroupRequestsForUser(user);
//        return new ResponseEntity<>(groupRequests, HttpStatus.OK);
//    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
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

    @Transactional
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupRequest> deleteGroupRequest(@PathVariable Long id) {
        try {
            groupRequestService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
