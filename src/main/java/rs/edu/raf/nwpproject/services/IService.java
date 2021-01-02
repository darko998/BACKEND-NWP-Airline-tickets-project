package rs.edu.raf.nwpproject.services;

import java.util.List;
import java.util.Optional;

public interface IService<T, ID> {

    T create(T var1);

    T update(T var1);

    List<T> findAll();

    Optional<T> findById(ID id);

    void deleteById(ID id);
}
