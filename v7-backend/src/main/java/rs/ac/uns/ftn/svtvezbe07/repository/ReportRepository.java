package rs.ac.uns.ftn.svtvezbe07.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Report;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long>{
	//List<Report> findAllByByUser(User byUser);
	   Report findReportById(Long id);
	   void deleteById(Long id);

}
