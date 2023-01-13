package edu.byu.cs.tweeter.client.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.AuthenticationPresenter;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the login screen.
 */
public class LoginFragment extends Fragment implements AuthenticationPresenter.AuthenticationView {
    private static final String LOG_TAG = "LoginFragment";

    private Toast loginInToast;
    private EditText alias;
    private EditText password;
    private TextView errorView;

    private LoginPresenter presenter;

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @return the fragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        alias = view.findViewById(R.id.loginUsername);
        password = view.findViewById(R.id.loginPassword);
        errorView = view.findViewById(R.id.loginError);
        Button loginButton = view.findViewById(R.id.loginButton);

        presenter = new LoginPresenter(this);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Login and move to MainActivity.
                try {
                    presenter.logIn(alias.getText().toString(),password.getText().toString());

                    //loginInToast = Toast.makeText(getContext(), "Logging In...", Toast.LENGTH_LONG);
                    //loginInToast.show();
                    //displayAccessingMessage();
                } catch (Exception e) {
                    errorView.setText(e.getMessage());
                }
            }
        });

        return view;
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setErrorView(String error) {
        errorView.setText(error);
    }

    @Override
    public void displayAccessingMessage() {
        //loginInToast = Toast.makeText(getContext(), "Logging In...", Toast.LENGTH_LONG);
        //loginInToast.show();
        displayMessage("Logging In...");
    }

    @Override
    public void cancelAccessingMessage() {
        if (loginInToast != null)
            loginInToast.cancel();
    }

    @Override
    public void displayWelcomeMessage() {
        //Toast.makeText(getContext(), "Hello " + Cache.getInstance().getCurrUser().getName(), Toast.LENGTH_LONG).show();
        displayMessage("Hello " + Cache.getInstance().getCurrUser().getName());
    }

    @Override
    public void loadUserPage(User loggedInUser) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, loggedInUser);
        startActivity(intent);
    }
}
