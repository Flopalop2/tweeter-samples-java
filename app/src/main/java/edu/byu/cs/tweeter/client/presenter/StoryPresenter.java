package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {
    private static final int PAGE_SIZE = 10;

    private StatusService statusService;

    public StoryPresenter(PagedView<Status> view,User user) {
        super(view,user,PAGE_SIZE);
        this.statusService = new StatusService();
    }

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, Status lastItem) {
        statusService.getStory(authToken, targetUser, pageSize, lastItem, new GetStoryObserver());
    }

    public class GetStoryObserver extends PagedObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to get story";
        }
    }

}
