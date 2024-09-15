package rs.ac.uns.ftn.svtvezbe07.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.svtvezbe07.model.dto.PostDTO;
import rs.ac.uns.ftn.svtvezbe07.model.dto.ReportDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Report;
import rs.ac.uns.ftn.svtvezbe07.service.ReportService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "http://4200", maxAge = 3600)
public class ReportController {

    private final ReportService reportService;
   
	private static final Logger logger = LogManager.getLogger(Log4jExample.class); 

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAll();
        return ResponseEntity.ok(reports);
    }
    @CrossOrigin(origins = "http://4200", maxAge = 3600)
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Report report = reportService.findReport(id);
        if (report != null) {
        	logger.info("Vracen report po ID-u");
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://4200", maxAge = 3600)
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody ReportDTO report) {
        Report createdReport = reportService.createReport(report);
        logger.info("Napravljen report");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    @PutMapping("/{id}")
	@CrossOrigin(origins = "http://4200")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN','GROUPADMIN')")
	public ResponseEntity<Report> edit(@RequestBody ReportDTO editPost) throws Exception {
	  

	        Report edit = reportService.findReport(editPost.getId());

	        edit.setAccepted(editPost.getAccepted());

	        reportService.save(edit);

	        Report p=reportService.findReport(edit.getId());

	        logger.info("Prihvacen ili odbijen report");
        return new ResponseEntity<>(p, HttpStatus.OK);
	
	}
	
    @CrossOrigin(origins = "http://4200", maxAge = 3600)
    @DeleteMapping("/{id}")
    public ResponseEntity<Report> deleteReport(@PathVariable Long id) {
       /* boolean deleted =*/ reportService.delete(id);
       Report post = reportService.findReport(id);
	   // post.setDeleted(true);
	    reportService.delete(id);
	    return new ResponseEntity<>(post, HttpStatus.OK);
//        if (deleted) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }
}
