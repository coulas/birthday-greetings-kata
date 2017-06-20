package it.xpug.kata.birthday_greetings;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.junit.Assert.*;

import it.xpug.kata.birthday_greetings.BirthdayService;
import it.xpug.kata.birthday_greetings.XDate;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.*;

import com.dumbster.smtp.*;


public class AcceptanceTest {

	private static final int NONSTANDARD_PORT = 9999;
	private BirthdayService birthdayService;
	private SimpleSmtpServer mailServer;

	@Before
	public void setUp() throws Exception {
		mailServer = SimpleSmtpServer.start(NONSTANDARD_PORT);
		birthdayService = new BirthdayService();
	}

	@After
	public void tearDown() throws Exception {
		mailServer.stop();
		Thread.sleep(200);
	}

	@Test
	public void willSendGreetings_whenItsSomebodysBirthday() throws Exception {

		birthdayService.sendGreetings("employee_data.txt", new XDate("2008/10/08"), "localhost", NONSTANDARD_PORT);

		SoftAssertions must = new SoftAssertions();
		must.assertThat(mailServer.getReceivedEmailSize()).describedAs("message not sent?").isGreaterThanOrEqualTo(1);

		SmtpMessage message = (SmtpMessage) mailServer.getReceivedEmail().next();
        must.assertThat(message.getBody()).contains("Happy Birthday, dear John!");
        must.assertThat(message.getHeaderValue("Subject")).isEqualTo("Happy Birthday!");
        must.assertThat( message.getHeaderValues("To")).containsExactly("john.doe@foobar.com");
        must.assertAll();
	}

	@Test
	public void willNotSendEmailsWhenNobodysBirthday() throws Exception {
		birthdayService.sendGreetings("employee_data.txt", new XDate("2008/01/01"), "localhost", NONSTANDARD_PORT);

		assertThat(mailServer.getReceivedEmailSize()).describedAs("what? messages?").isLessThanOrEqualTo(0);
	}
}
