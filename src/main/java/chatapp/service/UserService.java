package chatapp.service;

import chatapp.dto.User;
import chatapp.entity.UserEntity;
import chatapp.repository.IUserRepository;
import chatapp.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;


public class UserService {
    private final IUserRepository repo;

    public UserService(IUserRepository repo) {
        this.repo = repo;
    }

    public void createUser(UserEntity user) throws Exception {
        // check logic
        // call repo
        try {
            repo.createUser(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getClientIP(HttpServletRequest request) {
        String clientIP = request.getHeader("X-Forwarded-For");
        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("Proxy-Client-IP");
        }
        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("WL-Proxy-Client-IP");
        }

        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getRemoteAddr();
        }

        return clientIP;
    }

    public String findUserForLogin(UserEntity user, String IPaddr) throws Exception {
        // check logic
        // call repo
        try {
            return repo.findUserByUsernamePassword(user, IPaddr);
        } catch (Exception e) {
            throw e;
        }
    }

    public String generateRandomPassword() {
        int length = 12;
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(randomIndex));
        }

        return password.toString();
    }

    public void resetAndSendPasswordToEmail(UserEntity user) throws Exception {
        String senderEmail = "chatgptnnam@gmail.com";
        String senderPassword = "gqbu tbmf gkvw axxg";

        String newPassword = generateRandomPassword();


        // Recipient's email address
        String recipientEmail = repo.takeEmailByUsername(user.getUsername());
        if (recipientEmail == "") throw new Exception("Your email or username does not exist");

        // Set up the JavaMail session using Gmail SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender and recipient addresses
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Set the email subject and content
            message.setSubject("Hello from JavaMail");
            message.setText("Your new password is: " + newPassword);

            repo.resetPassword(user, Utils.hashString(newPassword));

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();

        }
    }

    public void saveFriendRequest(String user_id, String friend_id) throws Exception {
        try {
            if (!user_id.isEmpty() && !friend_id.isEmpty()) {
                repo.saveFriendRequest(user_id, friend_id);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public void acceptFriendRequest(String user_id, String friend_id) throws Exception {
        try {
            if (!user_id.isEmpty() && !friend_id.isEmpty()) {
                repo.acceptFriendRequest(user_id, friend_id);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public void unFriend(String user_id, String friend_id) throws Exception {
        try {
            if (!user_id.isEmpty() && !friend_id.isEmpty()) {
                repo.deleteFriend(user_id, friend_id);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public List<User> getFriendList(String user_id) throws Exception {
        try {
            return repo.getFriendList(user_id);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public String getOnlineFriendList(String user_id) throws Exception {
        try {
            return repo.getOnlineFriend(user_id);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public boolean isUserInGroup(String user_id, String group_id) throws Exception {
        try {
            return repo.isUserInGroup(user_id, group_id);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdByUsername(String username) throws Exception {
        try {
            return repo.getUserIdByUsername(username);
        } catch (Exception e) {
            return "";
        }
    }

    public String getUserNameById(String user_id) throws Exception {
        try {
            return repo.getUsernameByUserId(user_id);
        } catch (Exception e) {
            return "";
        }
    }

    public User getUserByUsername(String username) throws Exception {
        try {
            return repo.getUserByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> getFriendRequestList(String id) {
        try {
            return repo.getFriendRequestList(id);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void blockUser(String userId, String blockUserId) {
        try {
            repo.blockUser(userId, blockUserId);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Boolean isBlocked(String userId, String userIdByUsername) {
        try {
            return repo.isBlocked(userId, userIdByUsername);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
