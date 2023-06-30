package rs.ac.uns.ftn.svtvezbe07.model.entity;


import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Administrator extends User {
    
    public Administrator() {
    }

}
