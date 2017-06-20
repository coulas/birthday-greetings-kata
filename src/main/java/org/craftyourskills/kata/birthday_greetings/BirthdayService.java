package org.craftyourskills.kata.birthday_greetings;

import javax.mail.Session;
import java.io.File;
import java.time.LocalDate;

/**
 * Created by Nicolas Fédou on 20/06/2017.
 */
public class BirthdayService {
    private final Session session;
    private final File file;

    public BirthdayService(String smtpHost, int smtpPort, String employeeSource) {
        this(getSmtpSession(smtpHost, smtpPort), new File(employeeSource));
    }

    public BirthdayService(Session session, File file) {
        this.session = session;
        this.file = file;
    }

    private static Session getSmtpSession(String smtpHost, int smtpPort) {
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", "" + smtpPort);
        return Session.getInstance(props, null);
    }

    public void sendGreetings(LocalDate parse) {

    }
}
