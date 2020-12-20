package Service;

import DAO.RightRepository;

import javax.persistence.Persistence;

public class RightService {

    private RightRepository rightDao;

    public RightService(){
        try{
            rightDao = new RightRepository(Persistence.createEntityManagerFactory("JPA"));
        }catch (Exception ex){
            System.out.println("error creating the user repository");
        }
    }
}
