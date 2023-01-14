package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {

    public static final String FOLLOWEES_KEY = "followees";
    private static final String LOG_TAG = "GetFollowingTask";

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            String lastFolloweeAlias = getLastItem() == null ? null : getLastItem().getAlias(); //todo: ?

            FollowingRequest request = new FollowingRequest(getAuthToken(), targetUserAlias, getLimit(), lastFolloweeAlias);
            FollowingResponse response = getServerFacade().getFollowees(request, FollowService.URL_PATH);

            if (response.isSuccess()) {
                this.setItems(response.getFollowees());
                this.setHasMorePages(response.getHasMorePages());
                Pair<List<User>, Boolean> pair = new Pair<>(response.getFollowees(), response.getHasMorePages());
                sendSuccessMessage();
                return pair;
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followees", ex);
            sendExceptionMessage(ex);
        }
        return null;
        //return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }
}
