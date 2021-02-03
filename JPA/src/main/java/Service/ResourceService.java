package Service;

import DAO.ResourceRepository;
import DAO.RightRepository;
import Model.Resource;

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

    public void addFile(Resource newResource){
        resourceDao.create(newResource);
    }

    public Resource findById(int id){
        return resourceDao.find(id);
    }
}
