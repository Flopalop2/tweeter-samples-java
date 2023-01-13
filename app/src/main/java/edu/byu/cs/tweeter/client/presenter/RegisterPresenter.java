package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends AuthenticationPresenter{

    public RegisterPresenter(AuthenticationView view) {
        super(view);
    }

    public void registerUser(String firstName, String lastName, String alias, String password, Bitmap imageToUpload) {
        try {
            validateInput(alias,password,firstName,lastName,imageToUpload);
            ((AuthenticationView)view).setErrorView(null);
            ((AuthenticationView)view).displayAccessingMessage();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageToUpload.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();
            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            userService.registerUser(firstName, lastName, alias, password, imageBytesBase64, new RegisterObserver());
        } catch (Exception e) {
            throw e;
        }
    }


    public class RegisterObserver extends AuthenticationObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to register";
        }
    }
}
