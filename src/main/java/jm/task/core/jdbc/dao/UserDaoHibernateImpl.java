package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = createSessionFactory(Util.getHibernateConfiguration());
    }

    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();
        String sqlQuery = "CREATE TABLE IF NOT EXISTS users " +
                "(id BIGINT AUTO_INCREMENT, " +
                "name VARCHAR(256) NOT NULL, " +
                "lastName VARCHAR(256) NOT NULL, " +
                "age TINYINT UNSIGNED NOT NULL, " +
                "PRIMARY KEY (id))";
        session.createSQLQuery(sqlQuery).executeUpdate();
        session.close();
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        String sqlQuery = "DROP TABLE IF EXISTS users";
        session.createSQLQuery(sqlQuery).executeUpdate();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        User user = new User(name, lastName, age);
        session.save(user);
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        Session session = this.sessionFactory.openSession();
        User user = (User) session.get(User.class, id);
        if (user != null) {
            session.delete(user);
        }
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = this.sessionFactory.openSession();
        List<User> users = session.createQuery("FROM User").list();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        session.createQuery("DELETE FROM User").executeUpdate();
        session.close();
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
