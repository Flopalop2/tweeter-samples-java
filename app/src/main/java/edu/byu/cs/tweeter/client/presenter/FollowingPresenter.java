package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    private static final int PAGE_SIZE = 10;

    private FollowService followService;

    public FollowingPresenter(PagedView<User> view, User targetUser) {
        super(view, targetUser, PAGE_SIZE);
        this.followService = new FollowService();
    }

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
        followService.getFollowing(authToken, targetUser, pageSize, lastItem, new GetFollowingObserver());
    }

    public class GetFollowingObserver extends PagedObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to get following";
        }
    }

}
