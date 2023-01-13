package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.observer.NotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter{

    public interface MainView extends View {
        void displayMessage(String message);
        void displayLogoutMessage();
        void cancelLogoutMessage();
        void displayPostingMessage();
        void cancelPostingMessage();
        void setFollowButtonStatus(boolean status);
        void setFollowersCount(int count);
        void setFollowingCount(int count);
        void updateSelectedUserFollowingAndFollowers();
        void logoutUser();
    }

    private FollowService followService;
    private StatusService statusService;


    public MainPresenter(MainView view) {
        super(view);
        this.followService = new FollowService();
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public void postStatus(String post) {
        ((MainView)view).displayPostingMessage();
        PostStatusObserver statusObserver =new PostStatusObserver();
        try {
            Status newStatus = new Status(post,  Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
            getStatusService().postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, statusObserver);

        } catch (Exception e) {
            statusObserver.handleException(e);
        }
    }

    public class PostStatusObserver extends Observer implements NotificationObserver {

        @Override
        public void handleSuccess() {
            ((MainView)view).cancelPostingMessage();
            ((MainView)view).displayMessage("Successfully Posted!");
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to post status";
        }
    }

    public void onFollowButtonClick(boolean wasFollowing, User selectedUser) {
        if (wasFollowing) {
            followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                    new FollowRelationshipObserver(selectedUser, true));
            ((MainView)view).displayMessage("Removing " + selectedUser.getName() + "...");
        } else {
            followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                    new FollowRelationshipObserver(selectedUser, false));
            ((MainView)view).displayMessage("Adding " + selectedUser.getName() + "...");
        }
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerObserver());
    }

    public void updateSelectedUserFollowingAndFollowers(User user) {
        followService.getFollowersCount(Cache.getInstance().getCurrUserAuthToken(), user,
                new GetCountObserver(false));
        followService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(), user,
                new GetCountObserver(true));
    }

    public void logoutUser() {
        ((MainView)view).displayLogoutMessage();
        getUserService().logoutUser(new LogoutObserver());
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }





    public class GetCountObserver extends Observer implements CountObserver {

        private boolean isFollowingCount;

        public GetCountObserver(boolean isFollowingCount) {
            this.isFollowingCount = isFollowingCount;
        }

        @Override
        public void handleSuccess(int count) {
            if (isFollowingCount) {
                ((MainView) view).setFollowingCount(count);
            } else {
                ((MainView) view).setFollowersCount(count);
            }
        }

        @Override
        protected String getMessagePrefix() {
            if (isFollowingCount) {
                return "Failed to get following count";
            } else {
                return "Failed to get followers count";
            }
        }
    }


    public class IsFollowerObserver extends Observer implements edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            ((MainView)view).setFollowButtonStatus(isFollower);
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to determine following relationship";
        }
    }

    public class FollowRelationshipObserver extends Observer implements NotificationObserver {
        private User selectedUser;
        private boolean unfollowUser;

        public FollowRelationshipObserver(User selectedUser, boolean unfollowUser) {
            this.selectedUser = selectedUser;
            this.unfollowUser = unfollowUser;
        }

        @Override
        public void handleSuccess() {
            updateSelectedUserFollowingAndFollowers(selectedUser);
            ((MainView)view).setFollowButtonStatus(unfollowUser);
        }

        @Override
        public void handleFailure(String message) {
            super.handleFailure(message);
            //((MainView)view).setFollowButtonStatus(true);
        }

        @Override
        public void handleException(Exception ex) {
            super.handleException(ex);
            //((MainView)view).setFollowButtonStatus(true);
        }

        @Override
        protected String getMessagePrefix() {
            if (unfollowUser) {
                return "Failed to unfollow";
            } else {
                return "Failed to follow";
            }
        }
    }

    public class LogoutObserver extends Observer implements NotificationObserver {

        @Override
        public void handleSuccess() {
            ((MainView)view).cancelLogoutMessage();
            ((MainView)view).logoutUser();
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to logout";
        }
    }
}
