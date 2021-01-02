package rs.edu.raf.nwpproject.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.nwpproject.models.Company;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    Company findByName(String name);
}
