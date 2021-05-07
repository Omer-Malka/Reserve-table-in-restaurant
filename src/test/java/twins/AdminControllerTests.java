package twins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import twins.boundaries.InvokedByBoundary;
import twins.boundaries.Item;
import twins.boundaries.ItemBoundary;
import twins.boundaries.ItemIdBoundary;
import twins.boundaries.NewUserDetails;
import twins.boundaries.OperationBoundary;
import twins.boundaries.UserBoundary;
import twins.boundaries.UserIdBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminControllerTests {
	public final String TEST_VALUE = "test";
	public final String PATH_ADMIN = "/twins/admin";
	public final String PATH_USER_POST= "/twins/users";
	public final String PATH_ITEM_POST = "/twins/items/test/test";
	public final String PATH_OPERATION_INVOKE_POST = "/twins/operations";
	public final String PATH_OPERATION_INVOKE_ASYNC_POST = "/twins/operations/async";
	public final String PATH_USER_DELETE = "/twins/admin/users/test/test";
	public final String PATH_ITEM_DELETE = "/twins/admin/items/test/test";
	public final String PATH_OPERATION_DELETE = "/twins/admin/operations/test/test";
	public final int SIZE = 8;
	private int port;
	private String[] url;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = new String[SIZE];
		this.url[0]=  "http://localhost:" + this.port + PATH_ADMIN;
		this.url[1]=  "http://localhost:" + this.port + PATH_USER_POST;
		this.url[2]=  "http://localhost:" + this.port + PATH_ITEM_POST;
		this.url[3]=  "http://localhost:" + this.port + PATH_OPERATION_INVOKE_POST;
		this.url[4]=  "http://localhost:" + this.port + PATH_OPERATION_INVOKE_ASYNC_POST;
		this.url[5]=  "http://localhost:" + this.port + PATH_USER_DELETE;
		this.url[6]=  "http://localhost:" + this.port + PATH_ITEM_DELETE;
		this.url[7]=  "http://localhost:" + this.port + PATH_OPERATION_DELETE;
		this.restTemplate = new RestTemplate();
	}

	@AfterEach
	public void tearDown() {
		String newUrl;
		for (int i = 5; i < SIZE; i++) {
			newUrl = url[i];
			this.restTemplate.delete(newUrl);
		}
		
	}

	@Test
	public void testGetUsersStoredInDB() throws Exception{
		//GIVEN the server is run and DB is empty 


		//WHEN POST 
		NewUserDetails newUser=new NewUserDetails();
		newUser.setAvatar("TEST_VALUE");
		newUser.setEmail("TEST_VALUE");
		newUser.setRole("TEST_VALUE");
		newUser.setUserName("TEST_VALUE");
		
		UserBoundary response=this.restTemplate.
				postForObject(this.url[1], newUser, UserBoundary.class);		

		//THEN DB contains the user 
		ResponseEntity<UserBoundary[]> actual = this.restTemplate.
				getForEntity(this.url[0]+ "/users" + "/{userSpace}/{userEmail}", UserBoundary[].class, response.getUserId().getSpace(),response.getUserId().getEmail());
		assertThat(actual).isNotNull();
		assertThat(actual.getBody()[0].getUserId().getEmail()).isEqualTo(newUser.getEmail());
		assertThat(actual.getBody()[0].getAvatar()).isEqualTo(newUser.getAvatar());

	}
	
//	@Test
//	public void testGetOperationsStoredInDB() throws Exception{
//		//GIVEN the server is run and DB is empty 
//
//
//		//WHEN POST 
//		OperationBoundary operation = new OperationBoundary();
//		operation.setType(TEST_VALUE);
//		operation.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(TEST_VALUE, TEST_VALUE)));
//		operation.setItem(new Item(new ItemIdBoundary(TEST_VALUE, TEST_VALUE)));
//		operation.setOperationAttributes(new HashMap<>());
//
//		OperationBoundary response = this.restTemplate
//				.postForObject(this.url[4], operation, OperationBoundary.class);
//		response = this.restTemplate
//				.postForObject(this.url[3], operation, OperationBoundary.class);
//		
//		//THEN DB contains the user 
//		ResponseEntity<OperationBoundary[]> actual = this.restTemplate.
//				getForEntity(this.url[0]+ "/users" + "/{userSpace}/{userEmail}", OperationBoundary[].class, response.getOperationId().getSpace(), response.getOperationId().getId());
//		assertThat(actual).isNotNull();
//		assertThat(actual.getBody()[0].getOperationId().getSpace()).isEqualTo(operation.getOperationId().getSpace());
//		assertThat(actual.getBody()[0].getOperationId().getId()).isEqualTo(operation.getOperationId().getId());
//	}

	@Test
	public void testDeleteUsersStoredInDB() throws Exception{
		//GIVEN the server is run and DB is empty 


		//WHEN POST 
		NewUserDetails newUser=new NewUserDetails();
		newUser.setAvatar("TEST_VALUE");
		newUser.setEmail("TEST_VALUE");
		newUser.setRole("TEST_VALUE");
		newUser.setUserName("TEST_VALUE");
		UserBoundary response=this.restTemplate.
				postForObject(this.url[1], newUser, UserBoundary.class);		

		String newUrl = this.url[5];
		this.restTemplate.delete(newUrl);

		//THEN DB contains the user 
		ResponseEntity<UserBoundary[]> actual = this.restTemplate.
				getForEntity(this.url[0]+ "/users" + "/{userSpace}/{userEmail}", UserBoundary[].class, response.getUserId().getSpace(),response.getUserId().getEmail() );
		assertThat(actual.getBody().length).isEqualTo(0);

	}
	
	@Test
	public void testDeleteItemsStoredInDB() throws Exception{
		//GIVEN the server is run and DB is empty 


		//WHEN POST 
		ItemBoundary item = new ItemBoundary();
		item.setName(TEST_VALUE);
		item.setType(TEST_VALUE);
		ItemBoundary response = this.restTemplate
				.postForObject(this.url[2], item, ItemBoundary.class);
		
		String newUrl = this.url[6];
		this.restTemplate.delete(newUrl);

		//THEN DB contains the user 
		ResponseEntity<ItemBoundary[]> actual = this.restTemplate.
				getForEntity(this.url[0]+ "/items" + "/{userSpace}/{userEmail}", ItemBoundary[].class, response.getItemId().getSpace(), response.getItemId().getId());
		assertThat(actual.getBody().length).isEqualTo(0);	

	}

}
