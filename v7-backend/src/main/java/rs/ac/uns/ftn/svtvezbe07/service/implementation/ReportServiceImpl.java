package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Report;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.repository.ReportRepository;
import rs.ac.uns.ftn.svtvezbe07.service.ReportService;
@Service
public class ReportServiceImpl implements ReportService{
	@Autowired
    private ReportRepository ReportRepository;



    @Override
    public List<Report> getAll() {
        return this.ReportRepository.findAll();
    }
    

	public Report save(Report Report) {
		return ReportRepository.save(Report);
	}


//	@Override
//	public List<Report> findAllByUserId(User user) {
//	    return ReportRepository.findAllByReportedBy(user);
//	}

	
	/*public Optional<Report> getByIdInt(Integer id) {
		return ReportRepository.findReportById(id);
	}*/
	

//
//    @Override
//    public Report findReportByContent(String content) {
//        Optional<Report> Report = ReportRepository.findReportByContent(content);
//        if(!Report.isEmpty()){
//            return Report.get();
//        }
//
//        return null;
//    }

    public Report findReport(Long id) {
        Optional<Report> Report = ReportRepository.findById(id);
        if(!Report.isEmpty()){
            return Report.get();
        }

        return null;
    }
    
    public Report findReportById(Long id) {
        Report Report = ReportRepository.findReportById(id);
        if(Report!=null){
            return Report;
        }

        return null;
    }
    @Override
    public void delete(Long idInt) {
    	Long idLong = idInt;
    	Integer id = Integer.valueOf(idLong.intValue());
        ReportRepository.deleteById(idInt);
    	Report g= ReportRepository.findReportById(idInt);
	      // 	g.setDeleted(true);
	       	ReportRepository.save(g);
 
    }

	@Override
	public Report createReport(rs.ac.uns.ftn.svtvezbe07.model.dto.ReportDTO newReport) {
		 Report Report = new Report();
	        Report.setByUser(newReport.getByUser());
	        Report.setReported(newReport.getReported());
	        Report.setReported2(newReport.getReported2());
	        Report.setReported3(newReport.getReported3());
	        Report.setReportReason(newReport.getReportReason());
	        Report.setTimestamp(LocalDateTime.now());
	        Report = ReportRepository.save(Report);
	        return Report;
	}


//	@Override
//	public Report findReportByUserId(User user) {
//		Report l =ReportRepository.findReportByUserBy(user);
//		return l;
//	}

}
