package app.dao.repository;

import app.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long>{

    public boolean existsByDocument(long document);

    public Person findByDocument(long document);
    
}
