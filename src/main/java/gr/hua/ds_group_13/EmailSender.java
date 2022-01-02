package gr.hua.ds_group_13;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Properties;

public class EmailSender {



    public static void SendEmail(Letter letter,String To) throws MessagingException {
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        //Settings for GMAIL
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("EMAIL HERE", "PASSWORD HERE");
            }
        });


        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("EMAIL FROM HERE"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(To));
        message.setSubject("SUBJECT HERE");

        String msg = "BODY HERE";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        try {
            attachmentBodyPart.attachFile(CreatePDF(letter.getBody(),letter.getProfFName(), letter.getProfLName()));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        multipart.addBodyPart(attachmentBodyPart);
        message.setContent(multipart);

        Transport.send(message);
    }

    public static File CreatePDF(String LetterBody, String ProfFName, String ProfLName) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document,new FileOutputStream("Temp.pdf"));
        document.open();
        Font MainFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
        Font NameFont=  FontFactory.getFont(FontFactory.HELVETICA_BOLD,16,BaseColor.BLACK);
        Chunk MainChunk = new Chunk(LetterBody+"\n\n\n",MainFont);
        Chunk NameChunk=new Chunk(ProfFName+" "+ProfLName,NameFont);
        document.add(MainChunk);
        document.add(NameChunk);
        document.close();
        return new File("Temp.pdf");
    }
}
