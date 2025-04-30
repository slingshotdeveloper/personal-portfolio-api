package org.example.service;

import org.example.model.ContactForm;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final PolicyFactory SANITIZER = Sanitizers.BLOCKS;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.address}")
    private String myEmail;

    public void sendEmail(ContactForm contactForm) throws MessagingException, UnsupportedEncodingException {
        String requestId = UUID.randomUUID().toString();
        try {
            String firstName = SANITIZER.sanitize(capitalizeFirstLetter(contactForm.getFirstName()));
            String lastName = SANITIZER.sanitize(capitalizeFirstLetter(contactForm.getLastName()));
            String email = contactForm.getEmail();
            String subject = SANITIZER.sanitize(contactForm.getSubject());
            String message = SANITIZER.sanitize(contactForm.getMessage());

            logger.debug("Sanitized inputs [Request ID: {}] - subject length: {}, message length: {}",
                    requestId, subject.length(), message.length());

            String safeFullName = (firstName + " " + lastName).replaceAll("[\\r\\n\\t]", "");

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(new InternetAddress(myEmail, safeFullName));
            helper.setTo(myEmail);
            helper.setSubject("Dev Inquiry - " + subject);
            // Set email as plain text to avoid HTML rendering
            helper.setText(
                    "From: " + firstName + " " + lastName + "\n\n" +
                    "Email: " + email + "\n\n" +
                    "Message: \n" + message,
                    false // false indicates plain text
            );

            mailSender.send(mimeMessage);
            sendConfirmationEmail(email, firstName);
        } catch (MessagingException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error sending email", e);
        }
    }

    private void sendConfirmationEmail(String recipientEmail, String firstName) throws MessagingException, UnsupportedEncodingException {
        try {
            String safeFirstName = SANITIZER.sanitize(firstName);

            MimeMessage confirmationMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(confirmationMessage, true, "UTF-8");

            helper.setFrom(new InternetAddress(myEmail, "SlingShot Dev"));
            helper.setTo(recipientEmail);
            helper.setSubject("Message Confirmation");
            helper.setText(
                    "Hey " + safeFirstName + ",\n\n" +
                    "Thanks for reaching out! I have received your message and will get back to you shortly.\n\n" +
                    "Blessings,\n\nDavid Davis",
                    false // false indicates plain text
            );

            mailSender.send(confirmationMessage);
        } catch (MessagingException e) {
            throw e;
        }
    }

    private static String capitalizeFirstLetter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}