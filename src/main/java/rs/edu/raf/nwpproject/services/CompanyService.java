package rs.edu.raf.nwpproject.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.nwpproject.models.Company;
import rs.edu.raf.nwpproject.repositories.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService implements IService<Company, Long> {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company create(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company update(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public List<Company> findAll() {
        return (List<Company>) companyRepository.findAll();
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public Company findByName(String name) {
        return companyRepository.findByName(name);
    }

    @Override
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }
}
