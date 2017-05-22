package services;

import com.google.inject.ImplementedBy;
import models.User;
import services.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {
    User create(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    User update(User user);
}
