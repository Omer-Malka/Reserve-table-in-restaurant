package twins.data;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface ItemHandler extends MongoRepository<ItemEntity, String> {

	public Page<ItemEntity> findAllByUserSpaceAndUserEmail(
			@Param("userSpace") String userSpace, 
			@Param("userEmail") String userEmail, 
			Pageable pegeable);

	public List<ItemEntity> findAllByItemAttributesLike(
			@Param("pattern") String pattern);

	public List<ItemEntity> findAllByTypeAndActive(
			@Param("type") String type,
			@Param("active") boolean active);

	public List<ItemEntity> findAllByType(
			@Param("type") String type);

	public List<ItemEntity> findAllByTypeAndNameLike(
			@Param("type") String type,
			@Param("pattern") String pattern);

	public List<ItemEntity> findAllByActiveAndTypeAndNameLike(
			@Param("active")boolean active, 
			@Param("type")String type, 
			@Param("pattern") String pattern);

	public List<ItemEntity> findAllByUserEmailAndActiveAndTypeAndNameLike(
			@Param("email") String email, 
			@Param("active") boolean active, 
			@Param("type") String type, 
			@Param("pattern") String pattern);

}
