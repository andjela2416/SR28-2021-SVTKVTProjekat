package rs.ac.uns.ftn.svtvezbe07.model.dto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.OneToOne;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.model.entity.ReportReason;
import rs.ac.uns.ftn.svtvezbe07.model.entity.User;

public class ReportDTO {
	
	
    public ReportDTO() {
		super();
	}


	private Long id;


    private ReportReason reportReason;

    private LocalDate timestamp;
    

    private User reported;
    

    private Post reported2;
    
    
    private Long reported2Id;
    

    private Comment reported3;
    
    private Long reported3Id;

    private User byUser;


    private Boolean accepted;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getReported2Id() {
		return reported2Id;
	}


	public void setReported2Id(Long reported2Id) {
		this.reported2Id = reported2Id;
	}


	public ReportReason getReportReason() {
		return reportReason;
	}


	public void setReportReason(ReportReason reportReason) {
		this.reportReason = reportReason;
	}


	public LocalDate getTimestamp() {
		return timestamp;
	}


	public Long getReported3Id() {
		return reported3Id;
	}


	public void setReported3Id(Long reported3Id) {
		this.reported3Id = reported3Id;
	}


	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}


	public User getReported() {
		return reported;
	}


	public void setReported(User reported) {
		this.reported = reported;
	}




	public Post getReported2() {
		return reported2;
	}


	public void setReported2(Post reported2) {
		this.reported2 = reported2;
	}


	public Comment getReported3() {
		return reported3;
	}


	public void setReported3(Comment reported3) {
		this.reported3 = reported3;
	}


	public User getByUser() {
		return byUser;
	}


	public void setByUser(User byUser) {
		this.byUser = byUser;
	}


	public Boolean getAccepted() {
		return accepted;
	}


	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}
    

}
