package twins.data;



import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserHandler extends MongoRepository <UserEntity, String> {

}
