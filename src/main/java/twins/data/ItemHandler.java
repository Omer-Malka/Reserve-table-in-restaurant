package twins.data;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface ItemHandler extends MongoRepository <ItemEntity, String> {

}
