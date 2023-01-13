package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    public interface PagedView<T> extends View {
        void setLoading(boolean isLoading);
        void addItems(List<T> items);
        void navigateToUser(User user);
    }

    private final UserService userService;

    private final User targetUser;
    private final AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();

    private final int pageSize;
    private T lastItem;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private boolean isGettingUser;

    public PagedPresenter(PagedView<T> view, User targetUser, int pageSize) {
        super(view);
        this.targetUser = targetUser;
        this.pageSize = pageSize;

        userService = new UserService();
    }

    public void setLastItem(T lastItem) {
        this.lastItem = lastItem;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void onUserClick(String userAlias) {
        getUserService().getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias,
                new GetUserObserver());
        ((PagedView<T>) view).displayMessage("Getting user's profile...");
    }


    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;

            ((PagedView<T>)view).setLoading(true);

            getItems(authToken, targetUser, pageSize, lastItem);
        }
    }

    public abstract class PagedObserver extends Observer implements PagedServiceObserver<T> {

        @Override
        public void handleSuccess(List<T> items, boolean hasMore) {
            setLoading(false);
            ((PagedView<T>) view).setLoading(false);

            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setLastItem(lastItem);
            setHasMorePages(hasMore);
            ((PagedView<T>)view).addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            setLoading(false);
            ((PagedView<T>)view).setLoading(false);
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            setLoading(false);
            ((PagedView<T>)view).setLoading(false);
            view.displayMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }

    }

    public class GetUserObserver extends Observer implements ServiceObserver {

        public void handleSuccess(User user) {
            isGettingUser = false;
            ((PagedView<T>)view).navigateToUser(user);
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to get user's profile";
        }
    }

    protected abstract void getItems(AuthToken authToken, User targetUser, int pageSize, T lastItem);
}
