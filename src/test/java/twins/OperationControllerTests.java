package twins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
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
import twins.boundaries.ItemBoundary;
import twins.boundaries.ItemIdBoundary;
import twins.boundaries.OperationBoundary;
import twins.boundaries.OperationIdBoundary;
import twins.boundaries.UserIdBoundary;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OperationControllerTests {
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
		this.url = "http://localhost:" + this.port + "/twins/operations";
		this.restTemplate = new RestTemplate();
	}
	
	@AfterEach
	public void tearDown() {
		String newUrl =  "http://localhost:" + this.port + "/twins/admin/operations/test/test";
		this.restTemplate.delete(newUrl);
	}
	
	@Test
	public void testPostEmptyOperationReturnsErrorStatus() throws Exception {
		Map<String, Object> operation = new HashMap<>();
		assertThrows(Exception.class, ()->{
			this.restTemplate
			.postForObject(this.url, operation, ItemBoundary.class);
		});
	}
	
	@Test
	public void testPostOperationWithTypeNullReturns2xxStatusWithSameOperationReturnd() throws Exception {
		//GIVEN the server is up
		//Do nothing
		
		//WHEN I invoke POST /items with {"name":"test", "type":"test"}
		OperationBoundary operation = new OperationBoundary();
		operation.setType(null);
		operation.setInvokedBy(new InvokedByBoundary(new UserIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setItem(new Item(new ItemIdBoundary(TEST_VALUE, TEST_VALUE)));
		operation.setOperationAttributes(new HashMap<>());
		
		assertThrows(Exception.class, ()->{
			this.restTemplate
			.postForObject(this.url, operation, ItemBoundary.class);
		});
		
		//THEN the server returns status 2xx
		//Do nothing
		//AND the response body contains "name":"test"
		
		
	
	}
	
//	@Test
//	public void testPostOperationReturns2xxStatusWithSameOperationReturned() throws Exception {
//		//GIVEN the server is up
//		//Do nothing
//		
//		//WHEN I invoke POST /operations with {"name":"test", "type":"test"}
//		OperationBoundary operation = new OperationBoundary();
//		operation.setType("testType");
////		item.setName(TEST_VALUE);
////		item.setType(TEST_VALUE);
//		OperationBoundary response = this.restTemplate
//				.postForObject(this.url, operation, OperationBoundary.class);
//		
//		//THEN the server returns status 2xx
//		//Do nothing
//		//AND the response body contains "name":"test"
//		assertThat(response.getType()).isEqualTo("testType");
//	}
//	
////	@Test
////	public void testPostOperationStoredOperationInDB() throws Exception {
////	
////			ItemBoundary item = new ItemBoundary();
////			item.setName(TEST_VALUE);
////			item.setType(TEST_VALUE);
////			ItemBoundary response = this.restTemplate
////					.postForObject(this.url, item, ItemBoundary.class);
////			ItemBoundary actual = this.restTemplate.getForObject(this.url + "/{itemSpace}/{itemId}", 
////					ItemBoundary.class, response.getItemId().getSpace(), response.getItemId().getId());
////			assertThat(actual).isNotNull();
////			assertThat(actual.getName()).isEqualTo(item.getName());
////			assertThat(actual.getType()).isEqualTo(item.getType());
////		}
}
