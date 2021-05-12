package twins.data;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface ItemHandler extends MongoRepository<ItemEntity, String> {

	public Page<ItemEntity> findAllByUserSpaceAndUserEmail(
			@Param("userSpace")String userSpace, 
			@Param("userEmail") String userEmail, 
			Pageable pegeable);

	public List<ItemEntity> findAllByItemAttributesLike(
			@Param("pattern") String pattern);

}
