package com.example.asus.androidadvance_assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity{

    //Khai bao bien cho main activity
    TabHost tabHost;
    //Khai bao bien cho Students Activity
    EditText edtUser, edtPass;
    CheckBox cbRemb;
    Button btnLogin;
    RelativeLayout rlt;
    ListView lvNews;
    ImageView maps;


    private LoginButton loginButton;
    private CallbackManager callbackManager;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTabLayout();
        setDataTabNews();
        setDataTabStudent();
        facebook();
        maps();
    }

    private void maps(){
        maps = (ImageView)findViewById(R.id.imgMaps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setDataTabNews() {
        lvNews = (ListView)findViewById(R.id.lvNews);
        new AsyncProcess(MainActivity.this, lvNews).execute("http://tuoitre.vn/rss/tt-giao-duc.rss");
    }

    @Override
    protected void onPause() {
        super.onPause();
        savingSharedrefereces();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoringPreferences();
    }

    //Phuong thuc dung de ghi nho dang nhap va chuyen doi activity khi khi dang nhap thanh cong
    //Phuong thuc ap dung cho Students Activity
    private void setDataTabStudent() {
        edtUser = (EditText)findViewById(R.id.edtUsername);
        edtPass = (EditText)findViewById(R.id.edtPassword);
        cbRemb = (CheckBox)findViewById(R.id.cbRemb);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        //rlt = (RelativeLayout)findViewById(R.id.layout_info);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtUser.getText().toString().isEmpty() || edtPass.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter enought infomations!!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    //Phuong thuc dung de kiem tra CheckBox co duoc check hay khong
    //Neu duoc check thi luu user va pass lai, va nguoc lai.
    private void savingSharedrefereces(){
        //tao doi tuong getSharedRferences
        SharedPreferences sharedreferences = getSharedPreferences("data-login", MODE_PRIVATE);
        //tao doi tuong editor de luu thay doi
        SharedPreferences.Editor editor = sharedreferences.edit();
        String username = edtUser.getText().toString();
        String password = edtPass.getText().toString();
        Boolean check = cbRemb.isChecked();
        if(!check){
            //xoa moi thong tin luu truoc do
            editor.clear();
        }else{
            //luu vao editor
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putBoolean("saveStatus", check);
        }
        //chap nhan luu xuong file
        editor.commit();
    }

    //Phuong thuc dung de lay user va pass da luu dua ra EditText khi nguoi dung dang nhap lai
    private void restoringPreferences(){
        SharedPreferences pref = getSharedPreferences("data-login", MODE_PRIVATE);
        boolean check = pref.getBoolean("saveStatus", false);
        if(check){
            String username = pref.getString("username", "");
            String password = pref.getString("password", "");
            edtUser.setText(username);
            edtPass.setText(password);
        }
        cbRemb.setChecked(check);
    }


    //Phuong thuc dung de set layout cho TabHost

    private void setTabLayout() {
        tabHost = (TabHost)findViewById(R.id.tabLayout);
        tabHost.setup();

        TabHost.TabSpec spec;

        spec = tabHost.newTabSpec("student");
        spec.setContent(R.id.tabStudent);
        spec.setIndicator("Student", getResources().getDrawable(R.drawable.students));
        tabHost.addTab(spec);

        spec =tabHost.newTabSpec("news");
        spec.setIndicator("News",getResources().getDrawable(R.drawable.news));
        spec.setContent(R.id.tabNews);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

    //
    private void facebook(){
        //Login Facebook...
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //rlt.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
