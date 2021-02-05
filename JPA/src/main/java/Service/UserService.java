package Service;

import Model.User;
import DAO.UserRepository;

import javax.persistence.Persistence;
import java.util.List;

public class UserService {
    private UserRepository userDao;

    public UserService(){
        try{
            userDao = new UserRepository(Persistence.createEntityManagerFactory("JPA"));
        }catch (Exception ex){
            System.out.println("error creating the user repository");
        }
    }

    public void addUser(User newUser){
        userDao.create(newUser);
    }

    public void removeUser(User userToRemove, int userId) {
        userDao.remove(userToRemove, userId);
    }

    public User findUser(int userId) {
        return userDao.find(userId);
    }

    public void updateUser(User updateUser){
        userDao.update(updateUser);
    }

    public List<User> getAllUser(){
        return userDao.findAll();
    }

    public User findUserByToken(String token) {
        return userDao.findByToken(token);
    }

    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }
}
