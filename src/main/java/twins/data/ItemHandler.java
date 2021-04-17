package twins.data;

import org.springframework.data.repository.CrudRepository;

public interface ItemHandler extends CrudRepository <ItemEntity, String> {

}
