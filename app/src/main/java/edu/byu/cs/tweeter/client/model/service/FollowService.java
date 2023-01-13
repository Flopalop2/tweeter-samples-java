package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.CountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.NotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.observer.NotificationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service{

    //public interface GetFollowingObserver extends PagedServiceObserver<User> { }


    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee,
                             PagedServiceObserver<User> getFollowingObserver) {

        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PagedTaskHandler<User>(getFollowingObserver));
        executeTask(getFollowingTask);
    }

    public void getFollowers(AuthToken authToken, User user, int pageSize, User lastFollower, PagedServiceObserver<User> getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, user, pageSize, lastFollower, new PagedTaskHandler<User>(getFollowersObserver));
        executeTask(getFollowersTask);
    }

    //public interface UnfollowObserver extends NotificationObserver{}

    public void unfollow(AuthToken authToken, User user, NotificationObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                user, new NotificationHandler(unfollowObserver));
        executeTask(unfollowTask);
    }

    //public interface GetFollowersCountObserver extends ServiceObserver {
    //    void handleSuccess(int count);
    //}

    //public interface GetFollowingCountObserver extends ServiceObserver{
    //    void handleSuccess(int count);
    //}

    /*public void updateSelectedUserFollowingAndFollowers(AuthToken authToken, User user, MainPresenter.GetFollowersCountObserver getFollowersCountObserver, MainPresenter.GetFollowingCountObserver getFollowingCountObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                user, new GetFollowersCountHandler(getFollowersCountObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                user, new GetFollowingCountHandler(getFollowingCountObserver));
        executor.execute(followingCountTask);
    }*/

    public void getFollowingCount(AuthToken currUserAuthToken, User selectedUser, CountObserver getFollowingCountObserver) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(currUserAuthToken,
                selectedUser, new CountHandler(getFollowingCountObserver));
        executeTask(followingCountTask);
    }

    public void getFollowersCount(AuthToken currUserAuthToken, User selectedUser, CountObserver getFollowersCountObserver) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new CountHandler(getFollowersCountObserver));
        executeTask(followersCountTask);
    }

    //public interface IsFollowerObserver extends ServiceObserver {
     //   void handleSuccess(boolean isFollower);
    //}

    public void isFollower(AuthToken authToken, User currentUser, User selectedUser, IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                currentUser, selectedUser, new IsFollowerHandler(isFollowerObserver));
        executeTask(isFollowerTask);
    }

    //public interface FollowObserver extends NotificationObserver { }

    public void follow(AuthToken authToken, User user, NotificationObserver followObserver) {
        FollowTask followTask = new FollowTask(authToken,
                user, new NotificationHandler(followObserver));
        executeTask(followTask);
    }


}
