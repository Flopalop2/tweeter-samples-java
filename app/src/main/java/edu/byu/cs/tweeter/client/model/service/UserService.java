package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.NotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticationObserverT;
import edu.byu.cs.tweeter.client.model.service.observer.NotificationObserver;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService extends Service {

    public void getUser(AuthToken currUserAuthToken, String userAlias, PagedPresenter.GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(getUserObserver));
        executeTask(getUserTask);
    }

    //public interface LogoutObserver extends NotificationObserver {}

    public void logoutUser(NotificationObserver logoutObserver) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new NotificationHandler(logoutObserver));
        executeTask(logoutTask);
    }

    /*public interface LoginObserver extends ServiceObserver{
        void handleSuccess(User user);
    }*/

    public void logIn(String alias, String password, AuthenticationObserverT loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias,
                password,
                new AuthenticationHandler(loginObserver));
        executeTask(loginTask);
    }

    /*public interface RegisterObserver extends ServiceObserver {
        void handleSuccess(User user, AuthToken authToken);
    }*/

    public void registerUser(String firstName, String lastName, String alias, String password, String imageBytesBase64, AuthenticationObserverT registerObserver) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthenticationHandler(registerObserver));

        executeTask(registerTask);
    }
}
