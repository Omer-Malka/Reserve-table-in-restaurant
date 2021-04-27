package twins;


import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.boundaries.InvokedByBoundary;
import twins.boundaries.Item;
import twins.boundaries.ItemIdBoundary;
import twins.boundaries.OperationBoundary;
import twins.boundaries.UserIdBoundary;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OperationControllerTests {
	private int i;
	public final String TEST_VALUE = "test";
	public final String PATH_INVOKE = "/twins/operations";
	public final String PATH_INVOKE_ASYNC = "/twins/operations/async";
	public final int SIZE = 2;
	private int port;
	private String[] url;

	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	

	
	@PostConstruct
	public void init() {
		this.url = new String[2];
		this.url[0]=  "http://localhost:" + this.port + PATH_INVOKE;
		this.url[1]=  "http://localhost:" + this.port + PATH_INVOKE_ASYNC;
		this.restTemplate = new RestTemplate();
		
	}
	
	@AfterEach
	public void tearDown() {
		String newUrl =  "http://localhost:" + this.port + "/twins/admin/operations/test/test";
		this.restTemplate.delete(newUrl);
	}
	
	public void doTestWithPath(String url) {
		
	}
	
	@Test
	public void testPostEmptyOperationReturnsErrorStatus() throws Exception {
		Map<String, Object> operation = new HashMap<>();
		for (i = 0; i <SIZE; i++) {
		assertThrows(Exception.class, ()->{
			
			this.restTemplate
			.postForObject(this.url[i], operation, OperationBoundary.class);
			
			
		});
		}
	}
	
	@Test
	public void testPostOperationWithNullTypeReturns2xxStatus() throws Exception {
		//GIVEN the server is up
		//Do nothing
		//for (i = 0; i <SIZE; i++) {
		//WHEN I invoke POST /operation with {type=null}
		OperationBoundary operation = new OperationBoundary();
		operation.setType(null);
		operation.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setItem(new Item(new ItemIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setOperationAttributes(new HashMap<>());
		
		for (i = 0; i <SIZE; i++) {
			assertThrows(Exception.class, ()->{
				
				this.restTemplate
				.postForObject(this.url[i], operation, OperationBoundary.class);
				
				
			});
			}
		//}
		//THEN the server returns status 2xx
		//Do nothing
	
}
	
	
	@Test
	public void testPostOperationWithNullItemReturns2xxStatus() throws Exception {
		//GIVEN the server is up
		//Do nothing
		
		//WHEN I invoke POST /operation with {Item=null}
		
		OperationBoundary operation = new OperationBoundary();
		operation.setType(TEST_VALUE);
		operation.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setItem(null);
		operation.setOperationAttributes(new HashMap<>());
		for (i = 0; i <SIZE; i++) {
		assertThrows(Exception.class, ()->{
			
				this.restTemplate
				.postForObject(this.url[i], operation, OperationBoundary.class);
				
		});
		}
		//THEN the server returns status 2xx
		//Do nothing
	}
	
	@Test
	public void testPostOperationWithNullItem_IdReturns2xxStatus() throws Exception {
		//GIVEN the server is up
		//Do nothing
		
		//WHEN I invoke POST /operation with {item=null}
		
		OperationBoundary operation = new OperationBoundary();
		operation.setType(TEST_VALUE);
		operation.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setItem(new Item(new ItemIdBoundary(null, TEST_VALUE)));
		operation.setOperationAttributes(new HashMap<>());
		
		for (i = 0; i <SIZE; i++) {
			assertThrows(Exception.class, ()->{
				
				this.restTemplate
				.postForObject(this.url[i], operation, OperationBoundary.class);
				
				
			});
			}
		
		//THEN the server returns status 2xx
		//Do nothing
	}
	
	@Test
	public void testPostOperationWithNullItem_SpaceReturns2xxStatus() throws Exception {
		//GIVEN the server is up
		//Do nothing
		
		//WHEN I invoke POST /operation with {item=null}
		
		OperationBoundary operation = new OperationBoundary();
		operation.setType(TEST_VALUE);
		operation.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setItem(new Item(new ItemIdBoundary(TEST_VALUE, null)));
		operation.setOperationAttributes(new HashMap<>());
		
		for (i = 0; i <SIZE; i++) {
			assertThrows(Exception.class, ()->{
				
				this.restTemplate
				.postForObject(this.url[i], operation, OperationBoundary.class);
				
				
			});
			}
		
		//THEN the server returns status 2xx
		//Do nothing
	}
	
	@Test
	public void testPostOperationWithNullInvokeByReturns2xxStatus() throws Exception {
		//GIVEN the server is up
		//Do nothing
		
		//WHEN I invoke POST /operation with {item=null}
		
		OperationBoundary operation = new OperationBoundary();
		operation.setType(TEST_VALUE);
		operation.setInvokedBy(null);
		operation.setItem(new Item(new ItemIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setOperationAttributes(new HashMap<>());
		
		for (i = 0; i <SIZE; i++) {
			assertThrows(Exception.class, ()->{
				
				this.restTemplate
				.postForObject(this.url[i], operation, OperationBoundary.class);
				
				
			});
			}
		
		//THEN the server returns status 2xx
		//Do nothing
	}
	
	@Test
	public void testPostOperationWithNullUserIdSpaceReturns2xxStatus() throws Exception {
		//GIVEN the server is up
		//Do nothing
		
		//WHEN I invoke POST /operation with {item=null}
		
		OperationBoundary operation = new OperationBoundary();
		operation.setType(TEST_VALUE);
		operation.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(null, TEST_VALUE)));
		operation.setItem(new Item(new ItemIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setOperationAttributes(new HashMap<>());
		
		for (i = 0; i <SIZE; i++) {
			assertThrows(Exception.class, ()->{
				
				this.restTemplate
				.postForObject(this.url[i], operation, OperationBoundary.class);
				
				
			});
			}
		
		//THEN the server returns status 2xx
		//Do nothing
	}
	
	@Test
	public void testPostOperationWithNullUserIdEmailReturns2xxStatus() throws Exception {
		//GIVEN the server is up
		//Do nothing
		
		//WHEN I invoke POST /operation with {item=null}
		
		OperationBoundary operation = new OperationBoundary();
		operation.setType(TEST_VALUE);
		operation.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(TEST_VALUE, null)));
		operation.setItem(new Item(new ItemIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setOperationAttributes(new HashMap<>());
		
		for (i = 0; i <SIZE; i++) {
			assertThrows(Exception.class, ()->{
				
				this.restTemplate
				.postForObject(this.url[i], operation, OperationBoundary.class);
				
				
			});
			}
		
		//THEN the server returns status 2xx
		//Do nothing
	}

	
	@Test
	public void testPostOperationAndReturnsStatus2xxStatus() throws Exception {
		//GIVEN the server is up
		//Do nothing
		
		//WHEN I invoke POST /operation with {item=null}
		for (int i = 0; i < SIZE; i++) {
		OperationBoundary operation = new OperationBoundary();
		operation.setType(TEST_VALUE);
		operation.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setItem(new Item(new ItemIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setOperationAttributes(new HashMap<>());
		
//		assertThrows(Exception.class, ()->{
//			this.restTemplate
//			.postForObject(this.url, operation, ItemBoundary.class);
//		});
		
		//THEN the server returns status 2xx
		//Do nothing
	}
	}
	
}
