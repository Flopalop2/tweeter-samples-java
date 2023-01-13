package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    private static final int PAGE_SIZE = 10;

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
        followService.getFollowers(authToken, targetUser, pageSize, lastItem, new GetFollowersObserver());
    }

    private FollowService followService;

    public FollowersPresenter(PagedView<User> view, User user) {
        super(view,user,PAGE_SIZE);
        followService = new FollowService();
    }


    public class GetFollowersObserver extends PagedObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to get followers";
        }
    }


}
