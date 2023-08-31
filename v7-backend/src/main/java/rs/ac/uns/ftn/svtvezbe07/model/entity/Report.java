package rs.ac.uns.ftn.svtvezbe07.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="reports")
public class Report {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(   name = "reason")
	    private ReportReason reportReason;
	    @Column(   name = "timestamp_")
	    private LocalDateTime timestamp;
	    
	    @OneToOne 
	    @JoinColumn(name="reported")
	    private User reported;
	    
	    @OneToOne 
	    private Post reported2;
	    
	    @OneToOne 
	    private Comment reported3;
	    
	    
	    @OneToOne
	    @JoinColumn(name="user_id")
	    private User byUser;

	    @Column (name = "accepted")
	    private Boolean accepted;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public ReportReason getReportReason() {
			return reportReason;
		}

		public void setReportReason(ReportReason reportReason) {
			this.reportReason = reportReason;
		}

		

		public LocalDateTime getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(LocalDateTime timestamp) {
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
