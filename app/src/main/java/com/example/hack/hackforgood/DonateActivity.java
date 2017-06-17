package com.example.hack.hackforgood;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DonateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CalendarView.OnDateChangeListener {

    private static final String TAG = "DonateActivity";
    private Button submit;
    private EditText nameText;
    private EditText descText;
    private CalendarView calendar;
    private Spinner spinner;

    private int quantity = 0;

    OkHttpClient client = new OkHttpClient();

    private static final String[]paths = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10+"};
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        submit = (Button) findViewById(R.id.submitB);
        nameText = (EditText) findViewById(R.id.nameText);
        descText = (EditText) findViewById(R.id.descText);
        calendar = (CalendarView) findViewById(R.id.expDate);
        spinner = (Spinner) findViewById(R.id.dropDownQ);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        calendar.setOnDateChangeListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumbit();
            }
        });


    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "key=AAAAH2ByzG4:APA91bFmRBoCbGtQClosDA88CiftaE_IR9MIqM4rlME2jST3xxZlsD-2J87mUoHAaXrUh0j7xc-MfyO8jEJWG6LFJ81O4sqRVim_qJx43BATEQX9zoW6eAc0hveSEHHGHDzkjhVlT_sS")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private void sumbit() {
        if (nameText.getText().toString().matches("") || descText.getText().toString().matches("") || date.matches("")) {
            Toast.makeText(this, "Please fill out all the forms", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "sumbit: "+quantity);
        Log.d(TAG, "sumbit: date: "+date);
        String json = "{'to':'fvUghm3Y3ew:APA91bEB5Iaho21utRJsRGDMmvIA6jd3ZhdwDU1wnuAxG48tLmRd36T5jOLbajbUkS8ros1pv7Cbo9mI2hS4fHW_2uLF1NsyBiiuG5QeeU349QAcDi6q26SVhGf5AueaBXUhW_Rm_h3Y'," +
                "'data':{" +
                "'message': "+nameText.getText().toString()+" is up for donation}}";
        String url = "https://fcm.googleapis.com/fcm/send";
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:cee4ce70-1339-4902-a1e1-eb38f0635e99", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        // Create LambdaInvokerFactory, to be used to instantiate the Lambda proxy.
                LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.US_WEST_2, credentialsProvider);


        // Create the Lambda proxy object with a default Json data binder.
// You can provide your own data binder by implementing
// LambdaDataBinder.
        final MyInterface myInterface = factory.build(MyInterface.class);


        RequestClass request = new RequestClass("John", "Doe");
// The Lambda function invocation results in a network call.
// Make sure it is not called from the main thread.
        new AsyncTask<RequestClass, Void, ResponseClass>() {
            @Override
            protected ResponseClass doInBackground(RequestClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return myInterface.FirebasePushNotifications(params[0]);
                } catch (LambdaFunctionException lfe) {
                    Log.e("Tag", "Failed to invoke echo", lfe);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ResponseClass result) {
                if (result == null) {
                    return;
                }

                // Do a toast
                Toast.makeText(DonateActivity.this, result.getGreetings(), Toast.LENGTH_LONG).show();
            }
        }.execute(request);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        quantity = position+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        date = String.valueOf(month)+'/'+String.valueOf(dayOfMonth)+'/'+String.valueOf(year);
    }
}
