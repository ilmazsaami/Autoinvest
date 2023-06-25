package com.example.autoinvest.account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.autoinvest.Constants;
import com.example.autoinvest.R;
import com.example.autoinvest.drawer.DrawerActivity;
import com.example.autoinvest.users.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;




public class Account_Activity extends DrawerActivity {

    private ListView mListView;
    private AccountAdapter mAdapter;
    private List<InformationObject> mList = new ArrayList<>();
    private EditText mInputDeposit;
    private EditText mInputWithdraw;
    private Button mDeposit;
    private Button mWithdraw;
    private SwipeRefreshLayout swipe1;
    private SwipeRefreshLayout swipe2;
    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_account);

        //Drawer and toolbar
        initDrawer();
        mListView = (ListView)findViewById(R.id.values_list);
        InformationObject i = new InformationObject("Account Balance","3000");
        InformationObject j = new InformationObject("Portfolio value","500");
        InformationObject k = new InformationObject("Book value","1000");
        InformationObject l = new InformationObject("Change in Dollar","4%");
        InformationObject m = new InformationObject("Percent Change","6%");
        mList.add(i);
        mList.add(j);
        mList.add(k);
        mList.add(l);
        mList.add(m);
        mAdapter = new AccountAdapter(this,mList);
        mListView.setAdapter(mAdapter);
        TextView tv = (TextView)findViewById(R.id.empty_list);
        mListView.setEmptyView(tv);
        mDeposit= (Button)findViewById(R.id.deposit);
        mWithdraw = (Button) findViewById(R.id.withdraw);
        mInputDeposit = (EditText)findViewById(R.id.input1);
        mInputWithdraw = (EditText)findViewById(R.id.input2);
        mDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (!networkInfo.isConnected()) {
                    Toast.makeText(Account_Activity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mInputDeposit.getText().toString().equals("")){
                    return;
                }
                mDeposit.setEnabled(false);
                mInputDeposit.setEnabled(false);
                new DepositMoney().execute(Integer.valueOf(mInputDeposit.getText().toString()));

            }
        });
        mWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (!networkInfo.isConnected()) {
                    Toast.makeText(Account_Activity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mInputWithdraw.getText().toString().equals("")){
                    return;
                }
                mWithdraw.setEnabled(false);
                mInputWithdraw.setEnabled(false);
                new WithdrawMoney().execute(Integer.valueOf(mInputWithdraw.getText().toString()));
            }
        });
        //Query the server for account data
//        new GetMetaData().execute();
        swipe1 = (SwipeRefreshLayout)findViewById(R.id.empty_swipe);
        swipe2 = (SwipeRefreshLayout)findViewById(R.id.list_swipe);
        SwipeRefreshLayout.OnRefreshListener refresher = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new GetMetaData().execute();
            }
        };
        swipe1.setOnRefreshListener(refresher);
        swipe2.setOnRefreshListener(refresher);


    }


    /**
     * Start a login network call
     */
    private class GetMetaData extends AsyncTask<Void, Void, List<InformationObject>> {
        @Override
        protected List<InformationObject> doInBackground(Void ... v){
            List<InformationObject> list = new ArrayList<>();
            String sessionID = "";
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            sessionID = sharedPref.getString(getString(R.string.session_id),null);
            if (sessionID==null) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                return null;
            }
            String urlEndPoint = Constants.WEB_URL + "/api/v1/accounts/user" ;


            Run the api call
            HttpURLConnection client = null;
            try {
                URL url = new URL(urlEndPoint);
                client =(HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");
//                client.setConnectTimeout(10000);
                client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                client.setRequestProperty("Authorization","Token " + sessionID);
                if(client.getResponseCode()!= 200){
                    return null;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String text = br.readLine();
                JSONArray response= new JSONArray(text);
                list.clear();
                for(int i =0; i<response.length();i++){
                    JSONObject obj = response.getJSONObject(i);
                    list.add(new InformationObject(obj.getString("name"),obj.getString("value")));
                }
            }catch (Exception e){
                e.printStackTrace();
                if(client!=null){
                    client.disconnect();
                }
                return null;
            }finally {
                if(client!=null){
                    client.disconnect();
                }
            }
            InformationObject i = new InformationObject("Account Balance","3000");
            InformationObject j = new InformationObject("Portfolio value","500");
            InformationObject k = new InformationObject("Book value","1000");
            InformationObject l = new InformationObject("Change in Dollar","4%");
            InformationObject m = new InformationObject("Percent Change","6%");
            list.add(i);
            list.add(j);
            list.add(k);
            list.add(l);
            list.add(m);
            return list;
        }
        @Override
        protected void onPostExecute(List<InformationObject> success){
            swipe1.setRefreshing(false);
            swipe2.setRefreshing(false);
            if(success==null){
                Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_SHORT).show();
                return;
            }
            mList.clear();

            InformationObject i = new InformationObject("Account Balance","3000");
            InformationObject j = new InformationObject("Portfolio value","500");
            InformationObject k = new InformationObject("Book value","1000");
            InformationObject l = new InformationObject("Change in Dollar","4%");
            InformationObject m = new InformationObject("Percent Change","6%");
            mList.add(i);
            mList.add(j);
            mList.add(k);
            mList.add(l);
            mList.add(m);
            mAdapter.notifyDataSetChanged();
        }
    }
    /**
     * Start a login network call
     */
    private class WithdrawMoney extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer ... money){
            String sessionID = "";
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            sessionID = sharedPref.getString(getString(R.string.session_id),null);
            if (sessionID==null) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
                return null;
            }
            String urlEndPoint = Constants.WEB_URL + "/api/v1/accounts/withdraw" ;


            //Run the api call
            HttpURLConnection client = null;
            try {
                JSONObject json = new JSONObject();
                json.put("value",money[0]);
                URL url = new URL(urlEndPoint);
                client =(HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setConnectTimeout(10000);
                client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                client.setRequestProperty("Authorization","Token " + sessionID);
                client.setDoOutput(true);
                OutputStream os = client.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                if(client.getResponseCode()!= 202){
                    return null;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String text = br.readLine();
            }catch (Exception e){
                e.printStackTrace();
                if(client!=null){
                    client.disconnect();
                }
                return null;
            }finally {
                if(client!=null){
                    client.disconnect();
                }
            }

            return true;
        }
        @Override
        protected void onPostExecute(Boolean success){
            if(success==null){
                Toast.makeText(getApplicationContext(),"Transaction Failed",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Transaction Succeeded",Toast.LENGTH_SHORT).show();
            }
            mWithdraw.setEnabled(true);
            mInputWithdraw.setEnabled(true);
            mInputWithdraw.setText("");
//            new GetMetaData().execute();
        }
    }
    private class DepositMoney extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer ... money){
            String sessionID = "";
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            sessionID = sharedPref.getString(getString(R.string.session_id),null);
            if (sessionID==null) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
                return null;
            }
            String urlEndPoint = Constants.WEB_URL + "/api/v1/accounts/deposit" ;


            //Run the api call
            HttpURLConnection client = null;
            try {
                JSONObject json = new JSONObject();
                json.put("value",money[0]);
                URL url = new URL(urlEndPoint);
                client =(HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setConnectTimeout(10000);
                client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                client.setRequestProperty("Authorization","Token " + sessionID);
                client.setDoOutput(true);
                OutputStream os = client.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                if(client.getResponseCode()!= 202){
                    return null;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String text = br.readLine();
            }catch (Exception e){
                e.printStackTrace();
                if(client!=null){
                    client.disconnect();
                }
                return null;
            }finally {
                if(client!=null){
                    client.disconnect();
                }
            }

            return "test";
        }
        @Override
        protected void onPostExecute(String success){
            Toast.makeText(getApplicationContext(),"Transaction Suceeded money deposited ",Toast.LENGTH_SHORT).show();
            mDeposit.setEnabled(true);
            mInputDeposit.setEnabled(true);
            mInputDeposit.setText("");
            new GetMetaData().execute();
        }
    }

}
