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


    public String readUserData(String fullname, String username, String status, String sortBy, String order)
            throws Exception {
        try {

            return repo.getUserData(fullname, username, status, sortBy, order);
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

    public String getLoginHistories(String order) throws Exception{
        try {
            if(order == null) order = "";
            return repo.getLoginHistories(order);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public String getSpamReport(String sortBy, String startTime, String endTime, String username) throws Exception{
        try {
            return repo.getSpamReportList(sortBy, startTime, endTime, username);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public String getNumberOfUserEachMonth(int year) throws Exception{
        try {
            return repo.getNumberOfUserEachMonth(year);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public String getNumberOfFriendAndFOF(String sortBy, String order, String name, String greaterThan,
                                          String lowerThan, String equal) throws Exception{
        try {
            return repo.getFriendAndFriendOfFriends(sortBy, order, name, greaterThan, lowerThan, equal);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public String getNumberOfUserActiveEachMonth(int year) throws Exception{
        try {
            return repo.getNumberOfUserActiveEachMonth(year);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public String getActiveUsersOperation(String sortBy, String order, String startTime, String endTime,
                                          String username, String greaterThan,
                                          String lowerThan, String equal) throws Exception{
        try {
            return repo.getActiveUsersAndRelevantInfo(sortBy, order, startTime, endTime
                    , username , equal, greaterThan, lowerThan);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

}
