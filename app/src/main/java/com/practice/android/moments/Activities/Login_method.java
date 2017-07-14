package com.practice.android.moments.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.android.moments.R;
import com.practice.android.moments.Service.MyFirebaseInstanceIDService;

import java.util.Arrays;


public class Login_method extends AppCompatActivity {

    private static final String TAG = "Login Activity";
    private static final int RC_SIGN_IN = 0;
    public static String token;
    DatabaseReference databaseReference;
    private Button ViaEmail;
    private Button Viaphone;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private com.google.android.gms.common.SignInButton signInButton;
    private GoogleSignInAccount account;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//facebook SDK initialized
        FacebookSdk.sdkInitialize(getApplication());
        setContentView(R.layout.activity_login_method);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        ViaEmail = (Button) findViewById(R.id.Emailact);
        Viaphone = (Button) findViewById(R.id.Phoneact);
        //Google sign in variable
        signInButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.goobut);

        //Google Sign in Option
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).
                requestEmail().
                build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Login_method.this, "Check ur connection", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                startActivity(new Intent(Login_method.this, BottomNavigation.class));
                Toast.makeText(Login_method.this, "Logged in", Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
//google button called

//move to email activity
        ViaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_method.this, MainActivity.class));
                finish();

            }
        });
//move to phone activity
        Viaphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_method.this, Phoneprovider.class));
                finish();
            }
        });

//Facebook Button Starts
        //Facebook Sign in Variable
        loginButton = (LoginButton) findViewById(R.id.Face_login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                showProgressDialog();
                Log.e(TAG, "facebook:onSuccess:" + loginResult);
                Log.e(TAG, "facebook:Result:" + loginResult.getAccessToken().getUserId());

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login_method.this, "FaceBook Sign in cancelled", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.getMessage());
//                Log.e(TAG, "Account already registered with us other option");
                Toast.makeText(Login_method.this, "Account already registered with us other option", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
                Toast.makeText(Login_method.this, "FaceBook Sign in Failed", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });
        //Facebook Button Ends
    }

    // Google Sign-in Button Code
    //Starts

    private void signIn() {
        showProgressDialog();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                Toast.makeText(this, "Google Sign in Failed", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }

//callback to the facebook login button
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("Facebook=========", callbackManager.toString() + "==========" + requestCode);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            Toast.makeText(Login_method.this, "Logged in via Google", Toast.LENGTH_SHORT).show();
                            firebaseUser = firebaseAuth.getCurrentUser();
                            String user_id;
                            if (firebaseUser != null) {
                                user_id = firebaseUser.getUid();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(acct.getDisplayName())
                                        .build();

                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e("Editing", "User profile updated.");
                                                }
                                            }
                                        });

                                firebaseUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e(TAG, "Email sent.");
                                                }
                                            }
                                        });
                                startService(new Intent(getApplicationContext(), MyFirebaseInstanceIDService.class));
                                token = MyFirebaseInstanceIDService.refreshedToken;
                                DatabaseReference currentuser_db = databaseReference.child(user_id).child("User Info");
                                currentuser_db.child("name").setValue(acct.getDisplayName());
                                currentuser_db.child("email").setValue(acct.getEmail());
                                currentuser_db.child("phone").setValue("Default");
                                currentuser_db.child("photo").setValue("Default");
                                currentuser_db.child("gender").setValue("Default");
                                currentuser_db.child("userToken").setValue(token);
                                currentuser_db.child("relationship").setValue("Default");
                                currentuser_db.child("about").setValue("Default");
                                currentuser_db.child("date_of_birth").setValue("Default");
                                currentuser_db.child("coverPhoto").setValue("default");
                                updateUI(firebaseUser);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login_method.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.e(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.e(TAG, "Credential:" + "\n" + credential);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String user_id;

                            if (firebaseUser != null) {
                                user_id = firebaseUser.getUid();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(firebaseUser.getDisplayName())
                                        .build();

                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e("Editing", "User profile updated.");
                                                }
                                            }
                                        });

                                firebaseUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e(TAG, "Email sent.");
                                                }
                                            }
                                        });

                                startService(new Intent(getApplicationContext(), MyFirebaseInstanceIDService.class));
                                String token1 = MyFirebaseInstanceIDService.refreshedToken;

                                DatabaseReference currentuser_db = databaseReference.child(user_id).child("User Info");
                                currentuser_db.child("name").setValue(firebaseUser.getDisplayName());
                                currentuser_db.child("email").setValue(firebaseUser.getEmail());
                                currentuser_db.child("phone").setValue("Default");
                                currentuser_db.child("photo").setValue("Default");
                                currentuser_db.child("gender").setValue("Default");
                                currentuser_db.child("userToken").setValue(token1);
                                currentuser_db.child("relationship").setValue("Default");
                                currentuser_db.child("about").setValue("Default");
                                currentuser_db.child("date_of_birth").setValue("Default");
                                currentuser_db.child("coverPhoto").setValue("default");
                                updateUI(firebaseUser);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login_method.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {

            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login_method.this, BottomNavigation.class));
            hideProgressDialog();
        } else {
            hideProgressDialog();
            Log.e(TAG, "No Authenticated user found");

            Toast.makeText(Login_method.this, "No Authenticated user found", Toast.LENGTH_SHORT).show();

//            Toast.makeText(this, "Please verify Email", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("PLease wait");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);

        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    // Google Sign-in Button Code
    //Ends

    /*When Back Button is pressed it will open a dialog box written You want to exit!!!!
      if pressed yes then it will exit
      else it remain the same
      */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Login_method.this);
        builder1.setMessage("You want to exit!!!!");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(Login_method.this, "Thank you for Staying back", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {

//            if(firebaseUser.isEmailVerified()){
            updateUI(firebaseUser);
            showProgressDialog();
//            }else
//            {
//                updateUI(null);
//                showProgressDialog();
//
//            }

        }
    }
}