package ohtu.services;

import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import ohtu.data_access.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private UserDao userDao;

    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (containsUser(user, username, password)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsUser(User user, String username, String password) {
        return user.getUsername().equals(username) && user.getPassword().equals(password);
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null) {
            return false;
        }

        if (invalid(username, password)) {
            return false;
        }

        userDao.add(new User(username, password));

        return true;
    }

    private boolean invalid(String username, String password) {
        // validity check of username and password
        
        return usernameInvalid(username) || passwordInvalid(password);
    }
    
    private boolean usernameInvalid(String username){
        return usernameLength(username) || usernameContentsInvalid(username) || usernameReserved(username);
    }
    
    private boolean usernameLength(String username){
        return username.length() <= 2;
    }
    
    private boolean usernameContentsInvalid(String username){
        return !username.matches("[a-zA-Z]+");
    }
    
    private boolean usernameReserved(String username){
        return userDao.findByName(username) != null;
    }
    
    private boolean passwordInvalid(String password){
        return passwordLength(password) || passwordContentsInvalid(password);
    }
    
    private boolean passwordContentsInvalid(String password){
        return !password.matches(".*\\d.*") && !password.matches(".*\\W.*");
    }
    
    private boolean passwordLength(String password){
        return password.length() <= 7;
    }
}
