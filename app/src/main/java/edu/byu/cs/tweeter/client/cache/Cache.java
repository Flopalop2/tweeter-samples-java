package edu.byu.cs.tweeter.client.cache;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The Cache class stores globally accessible data.
 */
public class Cache {
    private static Cache instance = new Cache();

    public static Cache getInstance() {
        return instance;
    }


    /**
     * The currently logged-in user.
     */
    private User currUser;
    /**
     * The auth token for the current user session.
     */
    private static AuthToken currUserAuthToken = null;

    private Cache() {
        initialize();
    }

    private void initialize() {
        currUser = new User(null, null, null);
        //currUserAuthToken = null;
    }

    public void clearCache() {
        initialize();
    }

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public static AuthToken getCurrUserAuthToken() {
        return currUserAuthToken;
    }

    public static void setCurrUserAuthToken(AuthToken currUserAuthToken) {
        Cache.currUserAuthToken = currUserAuthToken;
    }

    public static void setInstance(Cache instance) {
        Cache.instance = instance;
    }
}
