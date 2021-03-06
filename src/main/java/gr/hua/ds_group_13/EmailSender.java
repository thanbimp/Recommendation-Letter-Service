package gr.hua.ds_group_13;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Service
@EnableConfigurationProperties(EmailSenderProperties.class)
public class EmailSender {

    @Autowired
    private EmailSenderProperties emailSenderProperties;

    private String email;
    private String password;

    public static File CreatePDF(String LetterBody, String ProfFName, String ProfLName) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Temp.pdf"));
        document.open();
        BaseFont bf = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        document.add(new Paragraph(LetterBody, new Font(bf, 12)));
        Paragraph nameParagraph = new Paragraph("\n\n\n\n\n\n" + ProfFName + " " + ProfLName, new Font(bf, 18));
        nameParagraph.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
        document.add(nameParagraph);
        document.close();
        writer.close();
        return new File("Temp.pdf");
    }

    public void SendEmail(Letter letter) throws MessagingException {

        email = emailSenderProperties.getEmail();
        password = emailSenderProperties.getPassword();
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", this.emailSenderProperties.getSmtpServer());
        properties.put("mail.smtp.port", this.emailSenderProperties.getSmtpPort());
        properties.put("mail.smtp.ssl.enable", this.emailSenderProperties.getSmtpSSL());
        properties.put("mail.smtp.auth", this.emailSenderProperties.getSmtpAuth());
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });


        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(letter.getReceiverEmail()));
        message.setSubject("New Reccomendation letter!");

        String msg = "Hello,<br>You have received a new recommendation letter.<br>Please see the attached PDF file.<br>Thank you!";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        try {
            attachmentBodyPart.attachFile(CreatePDF(letter.getBody(), letter.getProfFName(), letter.getProfLName()));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        multipart.addBodyPart(attachmentBodyPart);
        message.setContent(multipart);

        Transport.send(message);
    }
}
