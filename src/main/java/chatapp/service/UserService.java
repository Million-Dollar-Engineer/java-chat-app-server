package chatapp.service;

import chatapp.entity.UserEntity;
import chatapp.repository.IUserRepository;
import chatapp.utils.Utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
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

    public int findUserForLogin(UserEntity user) throws Exception {
        // check logic
        // call repo
        try {
            return repo.findUserByUsernamePassword(user);
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

}
