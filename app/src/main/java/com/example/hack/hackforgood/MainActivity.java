package com.example.hack.hackforgood;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private Button donateButton;
    private Button broadcastButton;

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:cee4ce70-1339-4902-a1e1-eb38f0635e99", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        FirebaseMessaging.getInstance().subscribeToTopic("test");

        donateButton = (Button) findViewById(R.id.donateButton);
        broadcastButton = (Button) findViewById(R.id.broadcastButton);

        donateButton.setOnClickListener(new View.OnClickListener() {
            public static final String TAG = "DONATE";

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:  "+ FirebaseInstanceId.getInstance().getToken());
                FirebaseMessaging.getInstance().subscribeToTopic("test");
//                navigateToDonateActivity();

            }
        });

        broadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToBroadcastActivity();
            }
        });
    }

    private void navigateToBroadcastActivity() {
        Intent intent = new Intent(this, DonateActivity.class);
        startActivity(intent);    }

    private void navigateToDonateActivity() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            Intent intent = new Intent(this, DonateActivity.class);
            startActivity(intent);
        } else {
            // not signed in
            // Choose an arbitrary request code value
//            Intent intent = new Intent(this, DonateActivity.class);
//            startActivity(intent);
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    RC_SIGN_IN);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
//                startActivity(SignedInActivity.createIntent(this, response));
                startActivityForResult(
                        // Get an instance of AuthUI based on the default app
                        AuthUI.getInstance().createSignInIntentBuilder().build(),
                        RC_SIGN_IN);
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }


            }

        }
    }
}
