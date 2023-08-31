package rs.ac.uns.ftn.svtvezbe07.service;

import java.util.List;

import rs.ac.uns.ftn.svtvezbe07.model.dto.ReportDTO;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Report;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public interface ReportService {
	   List<Report> getAll();
	    
	    Report save (Report Report);

		Report findReportById(Long id);

		Report findReport(Long id);

		Report createReport(ReportDTO newReport);
		

	    void delete(Long id);

//	   Report findReportByUserId(User user) ;
	    


}
