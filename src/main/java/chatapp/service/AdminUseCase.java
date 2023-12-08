package chatapp.service;

import ch.qos.logback.core.encoder.EchoEncoder;
import chatapp.entity.UserEntity;
import chatapp.repository.IAdminRepository;


public class AdminUseCase {
    IAdminRepository repo;
    public AdminUseCase(IAdminRepository adminRepository){
        this.repo = adminRepository;
    }
    public void banAccount(int id) throws Exception{
        try {
            repo.setAccountInActive(id);
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
    }
    public void unbanAccount(int id) throws Exception{
        try {
            repo.setAccountActive(id);
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
    }

    public String readUserData(String fullname, String username, String status) throws Exception{
        try{

            return repo.getUserData(fullname, username, status);
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
    }

    public void updateUserData(UserEntity user)throws Exception {
        try {
            repo.updateUser(user);
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
    }

    public void deleteUserData(int id)throws Exception {
        try {
            repo.deleteUser(id);
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
    }

}
