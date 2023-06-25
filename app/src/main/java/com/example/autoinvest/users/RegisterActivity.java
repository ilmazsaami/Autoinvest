package com.example.autoinvest.users;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.autoinvest.Constants;
import com.example.autoinvest.R;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {


    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;
    private TextView mErrorText;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;



    @Override
    public void onCreate(Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(R.layout.activity_register);

        mUsernameField = (EditText)findViewById(R.id.username_field);
        mPasswordField = (EditText)findViewById(R.id.password_field);
        mPasswordField.setTypeface(Typeface.DEFAULT);
        mConfirmPasswordField = (EditText)findViewById(R.id.password_confirm_field);
        mConfirmPasswordField.setTypeface(Typeface.DEFAULT);
        mErrorText = (TextView)findViewById(R.id.error_text);
        mRegisterButton = (Button)findViewById(R.id.register_button);
        mProgressBar = (ProgressBar)findViewById(R.id.loading_icon);
        mConfirmPasswordField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onRegister(null);
                }
                return false;
            }
        });

    }


    /**
     * Display an error message
     */
    public void displayErrorMessage(){
        mErrorText.setText(R.string.register_error);
        mErrorText.setVisibility(View.VISIBLE);
    }

    public void onRegister(View v){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (!networkInfo.isConnected()) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if(mUsernameField.getText().toString().equals("")){
            mErrorText.setText(R.string.invalid_username);
            mErrorText.setVisibility(View.VISIBLE);
            return;
        }
        if(mPasswordField.getText().toString().equals("")){
            mErrorText.setText(R.string.invalid_password);
            mErrorText.setVisibility(View.VISIBLE);
            return;
        }
        if(!mConfirmPasswordField.getText().toString().equals(mPasswordField.getText().toString())){
            mErrorText.setText(R.string.invalid_password_confirm);
            mErrorText.setVisibility(View.VISIBLE);
            return;
        }
        startLoading();
        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        new LoginAndGetSession().execute(username,password);
    }

    /**
     * Start a login network call
     */
    private class LoginAndGetSession extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String ... str){
            String username = str[0];
            String password = str[1];
            String sessionID = "";
            String urlEndPoint = Constants.WEB_URL + "/api/v1/accounts/register" ;
            JSONObject credentials = new JSONObject();
            try {
                credentials.put("username",username);
                credentials.put("password",password);
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            //Run the api call
            HttpURLConnection client = null;
            try {
                URL url = new URL(urlEndPoint);
                client =(HttpURLConnection) url.openConnection();
                client.setConnectTimeout(10000);
                client.setRequestMethod("POST");
                client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                client.setDoOutput(true);
                OutputStream os = client.getOutputStream();
                os.write(credentials.toString().getBytes("UTF-8"));
                os.flush();
                os.close();
                if(client.getResponseCode()!= 201){
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
                if(client!=null){
                    client.disconnect();
                }
                return false;
            }finally {
                if(client!=null){
                    client.disconnect();
                }
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean bool){
            if(bool){
                Toast.makeText(RegisterActivity.this,"Account Registered",Toast.LENGTH_LONG).show();
                finish();
            }else{
                displayErrorMessage();
                stopLoading();
            }
        }
    }

    public void startLoading(){
        mErrorText.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRegisterButton.setVisibility(View.GONE);
        mUsernameField.setEnabled(false);
        mPasswordField.setEnabled(false);
        mConfirmPasswordField.setEnabled(false);
    }
    public void stopLoading(){
        mErrorText.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mRegisterButton.setVisibility(View.VISIBLE);
        mUsernameField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mConfirmPasswordField.setEnabled(true);
    }

}
