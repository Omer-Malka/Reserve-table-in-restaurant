package twins.logic;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import twins.boundaries.UserBoundary;
import twins.boundaries.UserIdBoundary;
import twins.data.UserEntity;
import twins.data.UserHandler;
import twins.data.UserRole;
import twins.helpers.CheckerAuthorization;

@Service
public class UserServiceImplementation implements UserServiceExtended{
	private UserHandler userHandler;
	private String name;
	private CheckerAuthorization checker;

	@Autowired
	public UserServiceImplementation(UserHandler userHandler) {
		super();
		this.userHandler = userHandler;
		this.checker=new CheckerAuthorization(this.userHandler);

	}

	@Value("${spring.application.name: 2021b.lidar.ben.david}")
	public void setName(String name) {
		this.name = name;
	}

	@PostConstruct
	public void init() {
		System.err.println("name: "+this.name);
	}


	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		if (checker.CheckValidUser(this.name+"%"+user.getUserId().getEmail())) {
			throw new RuntimeException("User is already exist");
		}
		if (user.getUserId().getEmail() == null) {
			throw new RuntimeException("Email must not be null");
		}
		if (user.getUserName() == null) {
			throw new RuntimeException("username must not be null");
		}
		
		
		UserEntity entity= this.convertToEntity(user);
		entity.setUserid(this.name+"%"+user.getUserId().getEmail());
		entity=this.userHandler.save(entity);
		return this.convertToBoundary(entity);
	}


	@Override
	@Transactional(readOnly=true)
	public UserBoundary login(String userSpace, String userEmail) {
		Optional<UserEntity> entity=this.userHandler.findById(userSpace+"%"+userEmail);
		if (entity.isPresent()) {
			return this.convertToBoundary(entity.get());
		}else {
			throw new RuntimeException("Id could not be found");
		}

	}

	@Override
	@Transactional
	public void updateUser(String userSpace, String userEmail, UserBoundary update) {
		Optional<UserEntity> entity=this.userHandler.findById(userSpace+"%"+userEmail);
		if (entity.isPresent()) {
			update.setUserId(new UserIdBoundary(userSpace,userEmail));

			UserEntity updatedEntity= this.convertToEntity(update);
			this.userHandler.save(updatedEntity);

		}else {
			throw new RuntimeException("id could not be found");

		}

	}


	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminSpace, String adminMail, int size,int page) {
		//check if user is present and he is admin 
		if (!checker.CheckAdminUser(adminSpace+"%"+adminMail)){
			throw new RuntimeException("The User is not autorizied to do this action");
		}
		Optional<UserEntity> User=this.userHandler.findById(adminSpace+"%"+adminMail);
		UserEntity userEn=User.get();

		Iterable<UserEntity> allUsers = this.userHandler.findAll(PageRequest.of(page, size, Direction.ASC, "id"));
		List<UserBoundary> rv=new ArrayList<>();
		if (allUsers!=null) {
			for(UserEntity entity: allUsers)		
				rv.add(convertToBoundary(entity));
		}else {
			throw new RuntimeException("Cant find Users");
		}
		return rv;
	}


	@Override
	@Transactional
	public void deleteAllUsers(String adminSpace, String adminMail) {
		//check if user is present and he is admin 
		if (!checker.CheckAdminUser(adminSpace+"%"+adminMail)){
			throw new RuntimeException("The User is not autorizied to do this action");
		}
		//else
		this.userHandler.deleteAll();
	}

	private UserEntity convertToEntity(UserBoundary user) {
		UserEntity entity =new UserEntity();
		entity.setAvatar(user.getAvatar());
		entity.setRole(user.getRole());
		entity.setUserName(user.getUserName());
		entity.setUserid(user.getUserId().getSpace()+"%"+user.getUserId().getEmail());
		return entity;
	}

	private UserBoundary convertToBoundary(UserEntity entity) {
		UserBoundary boundary=new UserBoundary();
		boundary.setUserName(entity.getUserName());
		boundary.setAvatar(entity.getAvatar());
		boundary.setRole(entity.getRole());

		String [] arrOfStr=entity.getUserid().split("%");//separate to two strings
		boundary.setUserId(new UserIdBoundary(arrOfStr[0],arrOfStr[1]));
		return boundary;
	}


	@Override
	public List<UserBoundary> getAllUsers(String adminSpace, String adminMail) {
		// STUB IMPLEMENT
		throw new RuntimeException("function canceled");
	}







}
