package rs.ac.uns.ftn.svtvezbe07.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe07.model.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    void deleteById(Long id);
}
