package com.example.efm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestRegister extends AppCompatActivity {
    int val=0;
    String name,cnumber,lnumber,cpsw,psw,address;
    Button reg;
    String ServerURL = "https://learnfriendly.000webhostapp.com/rohan/insert.php";
    String ServerURL_T = "https://learnfriendly.000webhostapp.com/rohan/RestUsers.php";
    EditText et1,et2,et3,et4,et5,et6;private ProgressDialog progressDialog;
    String[] restnum = new String[100];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_register);
        et1 =  findViewById(R.id.rname);
        et2 = findViewById(R.id.rnumber);
        et3 =  findViewById(R.id.lnumber);
        et4= findViewById(R.id.psw);
        et5=findViewById(R.id.cpsw);
        et6=findViewById(R.id.address);
        reg = findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et1.getText().toString())){
                    et1.requestFocus();
                    Toast.makeText(RestRegister.this, "Restaurant name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et2.getText().toString())){
                    et2.requestFocus();
                    Toast.makeText(RestRegister.this, "Contact number cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et3.getText().toString())){
                    et3.requestFocus();
                    Toast.makeText(RestRegister.this, "License number cannot be empty", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(et4.getText().toString())){
                    et4.requestFocus();
                    Toast.makeText(RestRegister.this, "password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et5.getText().toString())){
                    et5.requestFocus();
                    Toast.makeText(RestRegister.this, "password  cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et6.getText().toString())){
                    et6.requestFocus();
                    Toast.makeText(RestRegister.this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(!et4.getText().toString().equals(et5.getText().toString()))
                {
                    et4.requestFocus();et5.requestFocus();
                    Toast.makeText(RestRegister.this, "The passwords did not match", Toast.LENGTH_LONG).show();
                }
                else if(et2.getText().toString().length()!=10){
                    et2.requestFocus();
                    et2.setError("Invalid Phone Number");
                }
                else if(!isValidPassword(et4.getText().toString()))
                {
                    et4.setError("You password should be of size >= 8 ,should contain special chracters, upper and lower case letter");
                }
                else{
                    if(!haveNetwork())
                    Toast.makeText(RestRegister.this, "Please check your Internet connection...", Toast.LENGTH_SHORT).show();
                    else{
                     getJSON(ServerURL_T,"sri","ram");
                    //    Toast.makeText(RestRegister.this, "Connected...", Toast.LENGTH_SHORT).show();
                        /*if(val==0)
                        InsertData(et1.getText().toString(),et2.getText().toString(),et3.getText().toString(),
                                et4.getText().toString(),et6.getText().toString());
                        else
                        {
                            et2.setError("This number is Already registered..Try to login");
                        }*/
                    }
                }
            }
        });
}
    private boolean haveNetwork(){
        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())have_MobileData=true;
        }
        return have_WIFI||have_MobileData;
    }


    public void InsertData(final String name, final String number, final String lnumber, final String pass, final String address){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name ;
                String Number = number ;
                String lnumberH = lnumber;
                String passH = pass;
                String addressH = address;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("number", Number));
                nameValuePairs.add(new BasicNameValuePair("lnumber",lnumberH ));
                nameValuePairs.add(new BasicNameValuePair("pass",passH ));
                nameValuePairs.add(new BasicNameValuePair("address",addressH ));
                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (Exception e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progressDialog = ProgressDialog.show(RestRegister.this, "Just a minute","Please wait....", true);
            }
            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                progressDialog.dismiss();
                Toast.makeText(RestRegister.this, "Registration Successful...Please Login", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RestRegister.this, RestLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, number, lnumber, pass, address);
    }
    private void getJSON(final String urlWebService,final String email,final String password) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(RestRegister.this, "Checking....","Please Wait....", true);
            }


            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);
                progressDialog.dismiss();
                try {
                    int k = loadView(s);
                    if (k == 1) {
                        et2.setError("This number is already registered..please try to login");
                    }
                    else{
                        InsertData(et1.getText().toString(), et2.getText().toString(), et3.getText().toString(),
                                et4.getText().toString(), et6.getText().toString());
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

//start
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data = URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(email,"UTF-8")+"&"
                            +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
//end
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();

    }
    private int loadView(String json) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);
        restnum = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            restnum[i] = obj.getString("number");

        }
        for(int i=0;i<restnum.length;i++){

            if(restnum[i].equals(et2.getText().toString())) {
            return 1;
            }

        }
        return 0;
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}