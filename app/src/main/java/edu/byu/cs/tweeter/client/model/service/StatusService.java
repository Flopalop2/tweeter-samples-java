package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.NotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.NotificationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Service {

    //public interface GetStoryObserver extends PagedServiceObserver<Status> {    }

    public void getStory(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedServiceObserver<Status> getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedTaskHandler<Status>(getStoryObserver));
        executeTask(getStoryTask);
    }

    /*public interface GetFeedObserver extends PagedPresenter.PagedObserver<Status> {
    }*/

    public void getFeed(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedServiceObserver<Status> getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedTaskHandler<Status>(getFeedObserver));
        executeTask(getFeedTask);
    }

    //public interface PostStatusObserver extends NotificationObserver {
    //}

    public void postStatus(AuthToken authToken, Status newStatus, NotificationObserver postStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(authToken,
                newStatus, new NotificationHandler(postStatusObserver));
        executeTask(statusTask);
    }

}
