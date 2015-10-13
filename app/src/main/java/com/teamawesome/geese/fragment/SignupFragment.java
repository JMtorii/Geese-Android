package com.teamawesome.geese.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.teamawesome.geese.R;
import com.teamawesome.geese.activity.MainActivity;

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
        passwordText.setText("this#is*a&PassworD");
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
                String username = usernameText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                // Send signin request to server with username/password/email and perform callback for success or failure
                // TODO find a Request library to do JSON POST to server
                loginUserComplete(username, email);
            }
        });
    }

    private void setupFacebookLogin() {
        facebookLoginButton.setReadPermissions("user_friends");

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                loginUserComplete("hello", "world@mail.com");
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getActivity().getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getActivity().getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTextClickables() {
        tosText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog tosDialog = new Dialog(getContext());
                tosDialog.setContentView(R.layout.tos_dialog);
                tosDialog.setTitle("Terms of Service");

                Button okButton = (Button) tosDialog.findViewById(R.id.ok_button);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tosDialog.dismiss();
                    }
                });

                tosDialog.show();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog loginDialog = new Dialog(getContext());
                loginDialog.setContentView(R.layout.login_dialog);
                loginDialog.setTitle("Login to Geese!");

                Button loginButton = (Button) loginDialog.findViewById(R.id.login_button);
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Send login request to server
                        Toast.makeText(getActivity().getApplicationContext(), "Logined!", Toast.LENGTH_SHORT).show();
                        loginDialog.dismiss();
                    }
                });

                Button cancelButton = (Button) loginDialog.findViewById(R.id.cancel_button);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginDialog.dismiss();
                    }
                });

                loginDialog.show();
            }
        });
    }

    private void loginUserComplete(String user, String email) {
        Toast.makeText(getActivity().getApplicationContext(), "Welcome! Redirecting...", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).getSessionManager().createLoginSession(user, email);
        getFragmentManager().popBackStack();
    }
}
