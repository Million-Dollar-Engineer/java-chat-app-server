package chatapp.service;

import chatapp.repository.IAdminRepository;
import chatapp.repository.IGroupChatRepository;

public class GroupChatService {
    IGroupChatRepository repo;

    public GroupChatService(IGroupChatRepository groupChatRepository) {
        this.repo = groupChatRepository;
    }

    public String getGroupChatList(String sortBy, String name) throws Exception {
        try {
            return repo.getGroupChatListByAdmin(sortBy, name);
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
    }
}
