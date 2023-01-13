package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    private static final int PAGE_SIZE = 10;

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, Status lastItem) {
        statusService.getFeed(authToken, targetUser, pageSize, lastItem, new GetFeedObserver());
    }

    private StatusService statusService;

    public FeedPresenter(PagedView<Status> view, User targetUser) {
        super(view, targetUser, PAGE_SIZE);
        this.statusService = new StatusService();
    }

    public class GetFeedObserver extends PagedObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to get feed";
        }
    }

}
