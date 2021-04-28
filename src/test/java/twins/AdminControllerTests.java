package twins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import twins.boundaries.NewUserDetails;
import twins.boundaries.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminControllerTests {
	public final String TEST_VALUE = "test";
	private int port;
	private String url; 
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/twins/admin";
		this.restTemplate = new RestTemplate();
	}

	@AfterEach
	public void tearDown() {
		String newUrl =  "http://localhost:" + this.port + "/twins/admin/users/test/test";
		this.restTemplate.delete(newUrl);
	}

	@Test
	public void testGetUsersStoredInDB() throws Exception{
		//GIVEN the server is run and DB is empty 


		//WHEN POST 
		NewUserDetails newUser=new NewUserDetails();
		newUser.setAvatar("testAvatar");
		newUser.setEmail("test@test.co.il");
		newUser.setRole("testRoll");
		newUser.setUserName("test");
		UserBoundary response=this.restTemplate.
				postForObject("http://localhost:" + this.port + "/twins/users", newUser, UserBoundary.class);		

		//THEN DB contains the user 
		ResponseEntity<UserBoundary[]> actual = this.restTemplate.
				getForEntity(this.url+ "/users" + "/{userSpace}/{userEmail}", UserBoundary[].class, response.getUserId().getSpace(),response.getUserId().getEmail() );
		assertThat(actual).isNotNull();
		assertThat(actual.getBody()[0].getUserId().getEmail()).isEqualTo(newUser.getEmail());
		assertThat(actual.getBody()[0].getAvatar()).isEqualTo(newUser.getAvatar());

	}

	@Test
	public void testDeleteUsersStoredInDB() throws Exception{
		//GIVEN the server is run and DB is empty 


		//WHEN POST 
		NewUserDetails newUser=new NewUserDetails();
		newUser.setAvatar("testAvatar");
		newUser.setEmail("test@test.co.il");
		newUser.setRole("testRoll");
		newUser.setUserName("test");
		UserBoundary response=this.restTemplate.
				postForObject("http://localhost:" + this.port + "/twins/users", newUser, UserBoundary.class);		

		String newUrl=this.url + "/users/" + response.getUserId().getSpace() + "/" + response.getUserId().getEmail();
		this.restTemplate.delete(newUrl);

		//THEN DB contains the user 
		ResponseEntity<UserBoundary[]> actual = this.restTemplate.
				getForEntity(this.url+ "/users" + "/{userSpace}/{userEmail}", UserBoundary[].class, response.getUserId().getSpace(),response.getUserId().getEmail() );
		assertThat(actual.getBody().length).isEqualTo(0);

	}

}
