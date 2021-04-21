package twins;

import static org.assertj.core.api.Assertions.assertThat;
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

import twins.boundaries.ItemBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ItemControllerTests {
	public final String TEST_VALUE = "test";
	private int port;
	private String url; //http://localhost:{port_number}/twins/items/{userSpace}/{userEmail}
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/twins/items/test/test";
		this.restTemplate = new RestTemplate();
	}
	
	@AfterEach
	public void tearDown() {
		String newUrl =  "http://localhost:" + this.port + "/twins/admin/items/test/test";
		this.restTemplate.delete(newUrl);
	}
	
	@Test
	public void testPostEmptyItemReturnsErrorStatus() throws Exception {
		Map<String, Object> item = new HashMap<>();
		assertThrows(Exception.class, ()->{
			this.restTemplate
			.postForObject(this.url, item, ItemBoundary.class);
		});
	}
	
	@Test
	public void testPostItemReturns2xxStatusWithSameItenReturned() throws Exception {
		//GIVEN the server is up
		//Do nothing
		
		//WHEN I invoke POST /items with {"name":"test", "type":"test"}
		ItemBoundary item = new ItemBoundary();
		item.setName(TEST_VALUE);
		item.setType(TEST_VALUE);
		ItemBoundary response = this.restTemplate
				.postForObject(this.url, item, ItemBoundary.class);
		
		//THEN the server returns status 2xx
		//Do nothing
		//AND the response body contains "name":"test"
		assertThat(response.getName()).isEqualTo(TEST_VALUE);
		assertThat(response.getType()).isEqualTo(TEST_VALUE);
	}
	
	@Test
	public void testPostItemStoredItemInDB() throws Exception {
		ItemBoundary item = new ItemBoundary();
		item.setName(TEST_VALUE);
		item.setType(TEST_VALUE);
		ItemBoundary response = this.restTemplate
				.postForObject(this.url, item, ItemBoundary.class);
		
		ItemBoundary actual = this.restTemplate.getForObject(this.url + "/test/{id}", ItemBoundary.class, response.getItemId());
		assertThat(actual).isNotNull();
		assertThat(actual.getName()).isEqualTo(item.getName());
		assertThat(actual.getType()).isEqualTo(item.getType());
	}
	
	@Test
	public void testUpdateItemAndValidateTheDBIsUpdated() {
		ItemBoundary item = new ItemBoundary();
		item.setName(TEST_VALUE);
		item.setType(TEST_VALUE);
		item = this.restTemplate
				.postForObject(this.url, item, ItemBoundary.class);
		
		ItemBoundary update = new ItemBoundary();
		update.setName(TEST_VALUE + "1");
		update.setType(TEST_VALUE+ "1");
		
		this.restTemplate.put(this.url + "/test/{id}", update, item.getItemId());
		
		assertThat(this.restTemplate.getForObject(this.url+"/test/{id}", ItemBoundary.class, item.getItemId()).getName())
			.isEqualTo(update.getName())
			.isNotEqualTo(item.getName());
		
		assertThat(this.restTemplate.getForObject(this.url+"/test/{id}", ItemBoundary.class, item.getItemId()).getType())
		.isEqualTo(update.getType())
		.isNotEqualTo(item.getType());
		
	}
}
