package chatapp.service;

import chatapp.entity.UserEntity;
import chatapp.repository.IAdminRepository;


public class AdminService {
    IAdminRepository repo;

    public AdminService(IAdminRepository adminRepository) {
        this.repo = adminRepository;
    }

    public void setAccountStatus(String id, String status) throws Exception {
        try {
            repo.setAccountStatus(id, status);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }


    public String readUserData(String fullname, String username, String status) throws Exception {
        try {

            return repo.getUserData(fullname, username, status);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public void updateUserData(UserEntity user) throws Exception {
        try {
            repo.updateUser(user);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public void deleteUserData(String id) throws Exception {
        try {
            repo.deleteUser(id);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public void getLoginHistories(String order) throws Exception{
        try {

        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

}
