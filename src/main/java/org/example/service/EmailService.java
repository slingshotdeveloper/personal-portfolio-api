package org.example.service;

import jakarta.mail.MessagingException;
import org.example.model.ContactForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${email.address}")
    private String myEmail;
    public void sendEmail(ContactForm contactForm) throws MessagingException {
        try {
            String firstName = capitalizeFirstLetter(contactForm.getFirstName());
            String lastName = capitalizeFirstLetter(contactForm.getLastName());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(myEmail);
            message.setSubject("Dev Inquiry - " + contactForm.getSubject());
            message.setText("From: " + firstName + " " + lastName + "\n\n" + "Email: " + contactForm.getEmail() + "\n\n" + "Message: " + "\n" + contactForm.getMessage());
            mailSender.send(message);

            sendConfirmationEmail(contactForm.getEmail(), firstName);
        } catch(MessagingException e) {
            throw new MessagingException("Failed to send email: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendConfirmationEmail(String recipientEmail, String firstName) throws Exception {
        try {
            SimpleMailMessage confirmationMessage = new SimpleMailMessage();
            confirmationMessage.setTo(recipientEmail);
            confirmationMessage.setSubject("Message Confirmation");
            confirmationMessage.setText("Hey " + firstName +",\n\nThanks for reaching out! I have received your message and will get back to you shortly.\n\nBlessings,\n\nDavid Davis");
            mailSender.send(confirmationMessage);
        } catch (Exception e) {
            throw new Exception("Failed to send email: " + e.getMessage(), e);
        }
    }

    private static String capitalizeFirstLetter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
