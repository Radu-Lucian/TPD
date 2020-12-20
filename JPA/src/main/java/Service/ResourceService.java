package Service;

import DAO.ResourceRepository;
import DAO.RightRepository;

import javax.persistence.Persistence;

public class ResourceService {

    private ResourceRepository resourceDao;

    public ResourceService(){
        try{
            resourceDao = new ResourceRepository(Persistence.createEntityManagerFactory("JPA"));
        }catch (Exception ex){
            System.out.println("error creating the user repository");
        }
    }
}
