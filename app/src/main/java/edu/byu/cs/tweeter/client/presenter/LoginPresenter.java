package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthenticationPresenter{
    public LoginPresenter(AuthenticationView view) {
        super(view);
    }

    public void logIn(String alias, String password) {
        LoginObserver observer = new LoginObserver();
        try {
            validateInput(alias, password);
            ((AuthenticationView)view).setErrorView(null);
            ((AuthenticationView)view).displayAccessingMessage();

            userService.logIn(alias, password, observer);
        } catch (Exception e) {
            throw e;
            //observer.handleException(e);
        }
    }


    public class LoginObserver extends AuthenticationObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to login";
        }
    }
}
