package twins.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import twins.data.UserHandler;

@Component
public class CheckerAuthorization {
	private UserHandler userHandler;

	@Autowired
	public CheckerAuthorization(UserHandler userHandler) {
		super();
		this.userHandler = userHandler;
	}
	
	
	
	

}
