package edu.byu.cs.tweeter.client.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterUnitTest {
    private static final String LOG_TAG = "MainPresenterTest";

    private MainPresenter.MainView mockView;
    private StatusService mockStatusService;
    private Cache mockCache;
    private AuthToken token;

    private MainPresenter mainPresenterSpy;

    @Before
    public void setup() {
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);

        token = new AuthToken();
        token.setToken("test token");
        Cache.setCurrUserAuthToken(token);

        //Mockito.when(Cache.getInstance().getCurrUserAuthToken()).thenReturn(token);
    }

    @Test
    public void testPostStatus_postSuccess() {
        Answer<Void> answer = getAnswer("success");

        postAnswer(answer);

        Mockito.verify(mockView).cancelPostingMessage();
        Mockito.verify(mockView).displayMessage("Successfully Posted!");


    }

    @Test
    public void testPostStatus_postFailedWithMessage() {
        Answer<Void> answer = getAnswer("failureMessage");

        postAnswer(answer);

        Mockito.verify(mockView, Mockito.times(0)).cancelPostingMessage();
        Mockito.verify(mockView).displayMessage("Failed to post status" + ": " + "failed test message");

    }

    @Test
    public void testPostStatus_postFailedWithException() {
        Answer<Void> answer = getAnswer("failureException");

        postAnswer(answer);

        Mockito.verify(mockView, Mockito.times(0)).cancelPostingMessage();
        Mockito.verify(mockView).displayMessage("Failed to post status" + " because of exception: " + "exception test message");

    }

    @Test
    public void testPostStatus_getMessagePrefix() {
        Answer<Void> answer = getAnswer("messagePrefix");

        postAnswer(answer);
    }

    private void postAnswer(Answer<Void> answer) {
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus("test status");
        Mockito.verify(mockView).displayPostingMessage();
    }

    private Answer<Void> getAnswer(String test) {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.PostStatusObserver observer = invocation.getArgument(2,MainPresenter.PostStatusObserver.class);
                AuthToken authToken = invocation.getArgument(0, AuthToken.class);
                Status status = invocation.getArgument(1,Status.class);

                if (test.equals("success")) {
                    observer.handleSuccess();
                }
                else if (test.equals("failureMessage")) {
                    observer.handleFailure("failed test message");
                }
                else if (test.equals("failureException")) {
                    observer.handleException(new Exception("exception test message"));
                }
                else if (test.equals("messagePrefix")) {
                    String returnMessage = observer.getMessagePrefix();
                    assertEquals("Failed to post status", returnMessage);
                }


                assertNotNull(observer);
                assertEquals(token, authToken);
                assertEquals("test status",status.getPost());
                return null;
            }
        };

        return answer;
    }
}
