package repositories;

import com.google.inject.ImplementedBy;
import repositories.impl.UserRepositoryImpl;
import models.User;

import java.util.List;
import java.util.Optional;

@ImplementedBy(UserRepositoryImpl.class)
public interface UserRepository {

    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    User update(User user);
}
