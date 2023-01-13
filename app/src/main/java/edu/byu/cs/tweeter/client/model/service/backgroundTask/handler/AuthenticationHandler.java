package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticationObserverT;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticationHandler extends BackgroundTaskHandler<AuthenticationObserverT> {
    public AuthenticationHandler(AuthenticationObserverT observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(AuthenticationObserverT observer, Bundle data) {
        User authenticatedUser = (User) data.getSerializable(AuthenticateTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);

        observer.handleSuccess(authenticatedUser, authToken);
    }
}