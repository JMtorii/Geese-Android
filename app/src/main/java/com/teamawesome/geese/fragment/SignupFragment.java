package com.teamawesome.geese.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.teamawesome.geese.R;
import com.teamawesome.geese.rest.RestExamples;
import com.teamawesome.geese.util.SessionManager;
import com.teamawesome.geese.util.Utilities;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class SignupFragment extends Fragment {
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

                if (validateFormValues(username, email, password, true)) {
                    attemptSignup(username, email, password);
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
                                try {
                                    String firstName = object.getString("first_name");
                                    String lastName = object.getString("last_name");
                                    String email = object.getString("email");
                                    loginUserComplete(firstName + " " + lastName, email);
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

                if (validateFormValues(username, email, password, true)) {
                    attemptLogin(username, email, password);
                }
            }
        });
    }

    private boolean validateFormValues(String username, String email, String password, boolean fromSignUp) {
        // Only requires either username or password for login vs both for signup
        boolean validUsername = true;
        if (!Utilities.isValidUsername(username)) {
            if (fromSignUp) {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.invalid_username), Toast.LENGTH_LONG).show();
                return false;
            } else {
                validUsername = false;
            }
        }
        if (!Utilities.isValidEmail(email)) {
            if (fromSignUp) {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
                return false;
            } else if (!validUsername) {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.invalid_username), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (!Utilities.isValidPassword(password)) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void attemptSignup(String username, String email, String password) {
//         TODO shenanigans here to attempt a signup and setup callback
//         Failure callback:
//         attemptLogin(username, email, password);
//         Successful:
//        loginUserComplete(username, email);
    }

    private void attemptLogin(String username, String email, String password) {
//      TODO send request to attempt login
//      loginUserComplete(username, email);

        // TODO I love testing
        RestExamples.testAllGoose();
    }

    private void loginUserComplete(String user, String email) {
        Toast.makeText(getActivity().getApplicationContext(), "Welcome ".concat(user).concat("! Redirecting..."), Toast.LENGTH_SHORT).show();
        SessionManager.createLoginSession(user, email);
        getFragmentManager().popBackStack();
    }
}
