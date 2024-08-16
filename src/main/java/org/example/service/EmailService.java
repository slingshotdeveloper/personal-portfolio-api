package org.example.service;

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
    public void sendEmail(ContactForm contactForm) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(myEmail);
            message.setSubject("Dev Inquiry - " + contactForm.getSubject());
            message.setText("From: " + contactForm.getFirstName() + " " + contactForm.getLastName() + "\n\n" + "Email: " + contactForm.getEmail() + "\n\n" + "Message: " + "\n" + contactForm.getMessage());
            mailSender.send(message);

            sendConfirmationEmail(contactForm.getEmail(), contactForm.getFirstName());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void sendConfirmationEmail(String recipientEmail, String firstName) {
        try {
            SimpleMailMessage confirmationMessage = new SimpleMailMessage();
            confirmationMessage.setTo(recipientEmail);
            confirmationMessage.setSubject("Message Confirmation");
            confirmationMessage.setText("Hey " + firstName +",\n\nThanks for reaching out! I have received your message and will get back to you shortly.\n\nBlessings,\n\nDavid Davis");
            mailSender.send(confirmationMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
