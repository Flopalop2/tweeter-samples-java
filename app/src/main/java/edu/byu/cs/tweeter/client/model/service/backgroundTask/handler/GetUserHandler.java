package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserHandler extends BackgroundTaskHandler<PagedPresenter.GetUserObserver> {
    public GetUserHandler(PagedPresenter.GetUserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(PagedPresenter.GetUserObserver observer, Bundle data) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.handleSuccess(user);
    }
}
