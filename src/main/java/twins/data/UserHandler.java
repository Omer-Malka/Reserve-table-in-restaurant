package twins.data;

import org.springframework.data.repository.CrudRepository;

import twins.boundaries.UserIdBoundary;

public interface UserHandler extends CrudRepository <UserEntity, String> {

}
