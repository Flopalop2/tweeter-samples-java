package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class Presenter {
    public interface View {
        void displayMessage(String message);
    }

    protected View view;
    private UserService userService;

    public Presenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public View getView() {
        return view;
    }

    public UserService getUserService() {
        return userService;
    }

    public abstract class Observer implements ServiceObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage(getMessagePrefix() + ": " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage(getMessagePrefix() + " because of exception: " + ex.getMessage());
        }

        protected abstract String getMessagePrefix();
    }
}
