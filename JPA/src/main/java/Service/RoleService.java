package Service;

import DAO.RoleRepository;

import javax.persistence.Persistence;

public class RoleService {

    private RoleRepository roleDao;

    public RoleService(){
        try{
            roleDao = new RoleRepository(Persistence.createEntityManagerFactory("JPA"));
        }catch (Exception ex){
            System.out.println("error creating the user repository");
        }
    }

}
