package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticationObserverT;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticationPresenter extends Presenter {

    public interface AuthenticationView extends View {
        void setErrorView(String error);
        void displayAccessingMessage();
        void cancelAccessingMessage();
        void displayWelcomeMessage();
        void loadUserPage(User loggedInUser);
    }

    protected final UserService userService;

    public AuthenticationPresenter(AuthenticationView view) {
        super(view);
        userService = new UserService();
    }

    public void validateInput(String alias, String password) {
        validateAlias(alias);
        validatePassword(password);
    }

    public void validateInput(String alias, String password, String firstName, String lastName, Bitmap image) {
        validateAlias(alias);
        validatePassword(password);
        validateNames(firstName, lastName);
        validateImage(image);
    }

    public void validateAlias(String alias) {
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
    }

    public void validatePassword(String password) {
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public void validateImage(Bitmap image) {
        if (image == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    public void validateNames(String firstName, String lastName) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
    }

    public abstract class AuthenticationObserver extends Observer implements AuthenticationObserverT {

        @Override
        public void handleSuccess(User authenticatedUser, AuthToken authToken) {
            Cache.getInstance().setCurrUser(authenticatedUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            ((AuthenticationView)view).cancelAccessingMessage();
            ((AuthenticationView)view).displayWelcomeMessage();
            try {
                ((AuthenticationView)view).loadUserPage(authenticatedUser);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}