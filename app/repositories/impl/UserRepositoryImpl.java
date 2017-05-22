package repositories.impl;

import models.Wallet;
import repositories.UserRepository;
import models.User;
import javax.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private JPAApi jpaApi;

    @Inject
    public UserRepositoryImpl(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public User save(User user) {
        jpaApi.em().persist(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return jpaApi.em().createQuery("select p from User p", User.class).getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(jpaApi.em().find(User.class, id));
    }

    @Override
    public User update(User user) {
        return jpaApi.em().merge(user);
    }
}
