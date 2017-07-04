package org.craftyourskills.kata.birthday_greetings;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.StrictAssertions.assertThat;


public class BirthdayEndtoEndShould {

	private static final int NONSTANDARD_PORT = 9999;
	private BirthdayService birthdayService;
	private SimpleSmtpServer mailServer;

	@Before
	public void setUp() throws Exception {
		mailServer = SimpleSmtpServer.start(NONSTANDARD_PORT);
		birthdayService = new BirthdayService("localhost", NONSTANDARD_PORT, "employee_data.txt");
	}

	@After
	public void tearDown() throws Exception {
		mailServer.stop();
		Thread.sleep(200);
	}

	@Test
	public void willSendGreetings_whenItsSomebodysBirthday() throws Exception {

		birthdayService.sendGreetings(LocalDate.parse("2017/12/08"));

		SoftAssertions should = new SoftAssertions();
		should.assertThat(mailServer.getReceivedEmailSize()).describedAs("message not sent?").isGreaterThanOrEqualTo(1);

		SmtpMessage message = (SmtpMessage) mailServer.getReceivedEmail().next();
		should.assertThat(message.getBody()).contains("Happy Birthday, dear John!");
		should.assertThat(message.getHeaderValue("Subject")).isEqualTo("Happy Birthday!");
		should.assertThat( message.getHeaderValues("To")).containsExactly("john.doe@foobar.com");
		should.assertAll();
	}

	@Test
	public void willNotSendEmailsWhenNobodysBirthday() throws Exception {
		birthdayService.sendGreetings(LocalDate.parse("2017/12/09"));

		assertThat(mailServer.getReceivedEmailSize()).describedAs("what? messages?").isLessThanOrEqualTo(0);
	}
}
