package chatapp.service;

import chatapp.repository.IAdminRepository;
import chatapp.repository.IGroupChatRepository;

public class GroupChatService {
    IGroupChatRepository repo;

    public GroupChatService(IGroupChatRepository groupChatRepository) {
        this.repo = groupChatRepository;
    }

    public String getGroupChatList(String sortBy, String order, String name) throws Exception {
        try {
            return repo.getGroupChatListByAdmin(sortBy, order, name);
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
    }

    public String getGroupChatMember(String group_id, String admin) throws Exception {
        try {
            return repo.getGroupChatMember(group_id, admin);
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
    }
}
