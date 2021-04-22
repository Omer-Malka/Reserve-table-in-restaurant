package twins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.boundaries.NewUserDetails;
import twins.boundaries.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
	private int port;
	private String url; //http://localhost:{port_number}/twins/users
	private RestTemplate restTemplet;
	
	
	@LocalServerPort// set the random port for the test server
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct//after i get all the data and build bean classes
	public void init() {
		this.url = "http://localhost:" + this.port + "/twins/users";
		this.restTemplet=new RestTemplate();
	}
//	@BeforeEach
//	public void setup() {
//
//		String newUrl="http://localhost:" + this.port +"/twins/admin/users/test/test";
//		this.restTemplet.delete(newUrl);
//	}
	
	@AfterEach
	public void tearDown() {

		String newUrl="http://localhost:" + this.port +"/twins/admin/users/test/test";
		this.restTemplet.delete(newUrl);
	}
	
	@Test
	public void testPostEmptyUserReturnErrorStatus() throws Exception{
		//GIVEN the server is up and DB is empty 
		
		//THEN i INVOKE POST 
		Map <String,Object> newUser=new HashMap<>();
		assertThrows(Exception.class, ()->{
			this.restTemplet.
			postForObject(this.url, newUser, UserBoundary.class);
		
		});

	}
	
	@Test//gherkin
	public void testPostNewUserDetailsReturn2xxStatusWithSameUserReturn() throws Exception{
		//GIVEN the server is up
		//do nothing 	
		
		//WHEN
		NewUserDetails newUser=new NewUserDetails();
		newUser.setAvatar("testAvatar");
		newUser.setEmail("test@test.co.il");
		newUser.setRole("testRoll");
		newUser.setUserName("test");
		UserBoundary response=this.restTemplet.
				postForObject(this.url, newUser, UserBoundary.class);
		
		
		
		assertThat(response.getUserId().getEmail()).isEqualTo(newUser.getEmail());
		
		//THEN
		
		
	}

	@Test
	public void testPostUserStoresInDB() throws Exception{
		//GIVEN the server is run and DB is empty 
		
		
		//WHEN POST 
		NewUserDetails newUser=new NewUserDetails();
		newUser.setAvatar("testAvatar");
		newUser.setEmail("test@test.co.il");
		newUser.setRole("testRoll");
		newUser.setUserName("test");
		UserBoundary response=this.restTemplet.
				postForObject(this.url, newUser, UserBoundary.class);
		
		
		//THEN DB contains the user 
		UserBoundary actual=this.restTemplet.
		getForObject(this.url+"/login"+"/{userSpace}/{userEmail}", UserBoundary.class,response.getUserId().getSpace(),response.getUserId().getEmail() );
		assertThat(actual).isNotNull();
		assertThat(actual.getUserId().getEmail()).isEqualTo(newUser.getEmail());
		assertThat(actual.getAvatar()).isEqualTo(newUser.getAvatar());
		
		
	}
	@Test
	public void testUpdateUserAndValidateDBIsUpdated() {
		//given the db contains user 
		NewUserDetails newUser=new NewUserDetails();
		newUser.setAvatar("testAvatar");
		newUser.setEmail("test@test.co.il");
		newUser.setRole("testRoll");
		newUser.setUserName("test");
		UserBoundary response=this.restTemplet.postForObject(this.url,newUser, UserBoundary.class);
		//when
		UserBoundary update=new UserBoundary();
		
		update.setUserName("new_test");
		update.setRole("new_role");
		update.setAvatar("new_avatar");
		
		this.restTemplet.put
		(this.url+"/{userspace}/{useremail}",
				update,response.getUserId().getSpace(),response.getUserId().getEmail());
	
		
		assertThat(this.restTemplet.getForObject
				(this.url+"/login"+"/{userspace}"+"/{useremail}",
						UserBoundary.class, response.getUserId().getSpace(),response.getUserId().getEmail()).getAvatar()).isNotEqualTo(response.getAvatar());

		
		assertThat(this.restTemplet.getForObject(this.url+"/login"+"/{userspace}/{useremail}",
						UserBoundary.class, response.getUserId().getSpace(),response.getUserId().getEmail()).getUserName())
		.isNotEqualTo(response.getUserName());
		
		
		
		assertThat(this.restTemplet.getForObject(this.url+"/login"+"/{userspace}/{useremail}",
				UserBoundary.class, response.getUserId().getSpace(),response.getUserId().getEmail()).getRole())
.isNotEqualTo(response.getRole());

	}
}
