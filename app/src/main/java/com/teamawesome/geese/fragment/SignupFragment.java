package com.teamawesome.geese.fragment;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.rest.model.Goose;
import com.teamawesome.geese.util.HashingAlgorithm;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignupFragment extends Fragment {
    private static String loggingTag = "Signup Fragment";

    private CallbackManager callbackManager;

    private Button signupButton;
    private LoginButton facebookLoginButton;
    private EditText usernameText, emailText, passwordText;
    private TextView tosText, loginText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        findWidgetsInView(view);
        setupSignupButton();
        setupFacebookLogin();
        setupTextClickables();

        // TODO: Remove this...
        usernameText.setText("lcolam");
        emailText.setText("lcolam@uwaterloo.ca");
        passwordText.setText("this#is*a&PassworD2");
        //

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void findWidgetsInView(View view) {
        signupButton = (Button) view.findViewById(R.id.email_sign_up_button);
        facebookLoginButton = (LoginButton) view.findViewById(R.id.facebook_login_button);
        usernameText = (EditText) view.findViewById(R.id.username_entry);
        emailText = (EditText) view.findViewById(R.id.email_entry);
        passwordText = (EditText) view.findViewById(R.id.password_entry);
        tosText = (TextView) view.findViewById(R.id.creation_ToS);
        loginText = (TextView) view.findViewById(R.id.login);
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
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getActivity().getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getActivity().getApplicationContext(), "Error while attempting to login.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTextClickables() {
        tosText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment tosDialogFragment = TosDialogFragment.newInstance();
                tosDialogFragment.show(getFragmentManager(), "tosDialog");
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
        Call<Void> call = ((MainActivity) getActivity()).geeseService.createGoose(goose);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    attemptLogin(username, email, hashedPassword);
                } else {
                    Log.e(loggingTag, "Signup failed");
                    Toast.makeText(getActivity().getApplicationContext(), "Signup failed...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(loggingTag, "Failed to create goose");
                Log.e(loggingTag, t.getMessage().toString());
                t.printStackTrace();
            }
        });
    }

    private void attemptLogin(final String username, final String email, final String hashedPassword) {
        Goose goose = new Goose(email, hashedPassword);
        Call<ResponseBody> call = ((MainActivity) getActivity()).loginService.attemptLogin(goose);
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
                    Toast.makeText(getActivity().getApplicationContext(), "Login failed...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(loggingTag, "Login attempt failed");
                Log.e(loggingTag, t.getMessage().toString());
                t.printStackTrace();
            }
        });
    }

    private void loginUserComplete(String user, String email, String token) {
        Toast.makeText(getActivity().getApplicationContext(), "Welcome ".concat(user).concat("! Redirecting..."), Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).getSessionManager().createLoginSession(user, email, token);
        getFragmentManager().popBackStack();
    }
}
