package rs.edu.raf.nwpproject.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.nwpproject.models.UserType;
import rs.edu.raf.nwpproject.repositories.UserTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserTypeService implements IService<UserType,Long>{

    private final UserTypeRepository userTypeRepository;

    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public UserType create(UserType userType) {
        return userTypeRepository.save(userType);
    }

    @Override
    public UserType update(UserType userType) {
        return userTypeRepository.save(userType);
    }

    @Override
    public List<UserType> findAll() {
        return (List<UserType>) userTypeRepository.findAll();
    }

    @Override
    public Optional<UserType> findById(Long id) {
        return userTypeRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        userTypeRepository.deleteById(id);
    }

}
