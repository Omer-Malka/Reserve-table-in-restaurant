package twins.data;

import org.springframework.data.repository.PagingAndSortingRepository;


public interface ItemHandler extends PagingAndSortingRepository<ItemEntity, String> {

}
