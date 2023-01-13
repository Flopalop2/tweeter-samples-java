package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;

public class PagedTaskHandler<T> extends BackgroundTaskHandler<PagedServiceObserver<T>> {
    public PagedTaskHandler(PagedServiceObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(PagedServiceObserver<T> observer, Bundle data) {
        List<T> items = (List<T>) data.getSerializable(GetFollowingTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}