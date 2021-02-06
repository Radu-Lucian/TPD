package Service;

import DAO.UserRoleRepository;
import Model.UserRole;

import javax.persistence.Persistence;

public class UserRoleService {

    private UserRoleRepository userRoleDao;

    public UserRoleService(){
        try{
            userRoleDao = new UserRoleRepository(Persistence.createEntityManagerFactory("JPA"));
        }catch (Exception ex){
            System.out.println("error creating the user repository");
        }
    }

    public void update(UserRole userRole) {
        userRoleDao.update(userRole);
    }

    public void addNewUserRole(UserRole userRole) {
        userRoleDao.create(userRole);
    }
}
