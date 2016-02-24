package com.teamawesome.geese.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.R;
import com.teamawesome.geese.fragment.TosDialogFragment;
import com.teamawesome.geese.rest.model.Goose;
import com.teamawesome.geese.util.Constants;
import com.teamawesome.geese.util.HashingAlgorithm;
import com.teamawesome.geese.util.RestClient;
import com.teamawesome.geese.util.SessionManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lcolam on 2/3/16.
 */
public class LoginActivity extends Activity {
    Activity mContext = this;
    private static String loggingTag = "Login Activity";

    private CallbackManager callbackManager;

    private Button signupButton;
    private LoginButton facebookLoginButton;
    private EditText usernameText, emailText, passwordText;
    private TextView tosText, loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        findWidgets(this);
        setupSignupButton();
        setupFacebookLogin();
        setupTextClickables();

        // TODO: Remove this...
        usernameText.setText("lcolam");
        emailText.setText("lcolam@uwaterloo.ca");
        passwordText.setText("this#is*a&PassworD2");
    }

    private void findWidgets(Activity activity) {
        signupButton = (Button) activity.findViewById(R.id.email_sign_up_button);
        facebookLoginButton = (LoginButton) activity.findViewById(R.id.facebook_login_button);
        usernameText = (EditText) activity.findViewById(R.id.username_entry);
        emailText = (EditText) activity.findViewById(R.id.email_entry);
        passwordText = (EditText) activity.findViewById(R.id.password_entry);
        tosText = (TextView) activity.findViewById(R.id.creation_ToS);
        loginText = (TextView) activity.findViewById(R.id.login);
    }

    private void setupSignupButton() {
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = "", email = "", password = "";
                username = usernameText.getText().toString().trim();
                usernameText.setText(username);
                email = emailText.getText().toString().trim();
                emailText.setText(email);
                password = passwordText.getText().toString();

                HashingAlgorithm ha = new HashingAlgorithm();
                try {
                    String hashedPassword = ha.sha256(password);
                    attemptSignup(username, email, hashedPassword);
                } catch (Exception e) {
                    Log.e(loggingTag, "Failed to hash password");
                }
            }
        });
    }

    private void setupFacebookLogin() {
        List<String> permissionNeeds = Arrays.asList("public_profile", "user_friends", "email", "user_location");
        facebookLoginButton.setReadPermissions(permissionNeeds);

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse graphResponsesponse) {
                                Log.e(loggingTag, "Facebook sign in success");
                                try {
                                    String firstName = object.getString("first_name");
                                    String lastName = object.getString("last_name");
                                    String email = object.getString("email");

//                                    loginUserComplete(firstName + " " + lastName, email, );
                                } catch (org.json.JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                Log.e("Test", "Facebook login success");
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(mContext.getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                Log.e("Test", "Facebook login cancel");

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(mContext.getApplicationContext(), "Error while attempting to login.", Toast.LENGTH_SHORT).show();
                Log.e("Test", "Facebook login error");

            }
        });
    }

    private void setupTextClickables() {
        tosText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                /*DialogFragment tosDialogFragment = TosDialogFragment.newInstance();
                tosDialogFragment.show(getFragmentManager(), "tosDialog");*/
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = "", email = "", password = "";
                username = usernameText.getText().toString().trim();
                usernameText.setText(username);
                email = emailText.getText().toString().trim();
                emailText.setText(email);
                password = passwordText.getText().toString();

                HashingAlgorithm ha = new HashingAlgorithm();
                try {
                    String hashedPassword = ha.sha256(password);
                    attemptLogin(username, email, hashedPassword);
                } catch (Exception e) {
                    Log.e(loggingTag, "Failed to hash password");
                }
            }
        });
    }

    private void attemptSignup(final String username, final String email, final String hashedPassword) {
        Goose goose = new Goose(username, email, hashedPassword);
        Call<Void> call = RestClient.geeseService.createGoose(goose);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    attemptLogin(username, email, hashedPassword);
                } else {
                    Log.e(loggingTag, "Signup failed");
                    Toast.makeText(mContext.getApplicationContext(), "Signup failed...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(loggingTag, "Failed to create goose");
                Log.e(loggingTag, t.getMessage().toString());
                t.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), "Server down, try again later...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptLogin(final String username, final String email, final String hashedPassword) {
        Goose goose = new Goose(email, hashedPassword);
        Call<ResponseBody> call = RestClient.loginService.attemptLogin(goose);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        String token = response.body().string();
                        loginUserComplete(username, email, token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(loggingTag, "Login failed");
                    Toast.makeText(mContext.getApplicationContext(), "Login failed...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(loggingTag, "Login attempt failed");
                Log.e(loggingTag, t.getMessage().toString());
                t.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), "Server down, try again later...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUserComplete(String user, String email, String token) {
        SessionManager.createLoginSession(user, email, token);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
