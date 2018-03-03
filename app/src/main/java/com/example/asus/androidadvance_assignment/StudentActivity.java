package com.example.asus.androidadvance_assignment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    private TextView userName;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private ProfilePictureView profilePictureView;
    private LoginButton logOut;
    //Khai báo biến
    String DATABASE_NAME = "StudentsManagement.sqlite";
    SQLiteDatabase Database;
    StudentAdapter adapter;
    ImageButton imbAddClass, imbAddStudents, imbShowStudents;
    Spinner spnClass;
    EditText edtIdStudent;
    EditText edtNameStudent;
    EditText edtClassName;
    RelativeLayout rt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        imbAddClass = (ImageButton)findViewById(R.id.imbAddClass);
        imbAddStudents = (ImageButton)findViewById(R.id.imbAddStudents);
        imbShowStudents = (ImageButton)findViewById(R.id.imbShowStudents);
        //gọi đến các hàm chứa sự kiện của các button...
        addClass();
        addStudents();
        showAllStudents();
        facebook();
        rt.setVisibility(View.VISIBLE);
    }



    //Phuong thuc dung de gan su kien cho imbShowStudents...
    private void showAllStudents() {
        imbShowStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tạo intent
                Intent intent = new Intent(StudentActivity.this, ShowStudentsActivity.class);
                startActivity(intent);
            }
        });
    }

    //Phuong thuc dung de gan su kien cho imbAddStudents...
    private void addStudents() {
        imbAddStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Khởi tạo dialog
                final Dialog dialogStudents = new Dialog(StudentActivity.this);
                //Gán layout cho dialog
                dialogStudents.setContentView(R.layout.add_students_dialog);
                //Đặt tên cho dialog
                dialogStudents.setTitle("Add Students");
                //Ánh xạ các control trong dialog
                spnClass = (Spinner)dialogStudents.findViewById(R.id.spnClass);
                edtIdStudent = (EditText)dialogStudents.findViewById(R.id.edtIdStudent);
                edtNameStudent = (EditText)dialogStudents.findViewById(R.id.edtNameStudent);
                Button btnAddStudents = (Button)dialogStudents.findViewById(R.id.btnAddStudents);
                Button btnAddStudentsCancel = (Button)dialogStudents.findViewById(R.id.btnAddStudentsCancel);
                //gọi hàm lấy dữ liệu chứa tên lớp từ database đổ vào Spinner trong dialog
                loadDataToSpinner();
                //gán sự kiện cho button
                btnAddStudents.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //gọi đến hàm lấy dữ liệu từ dialog ghi vào database
                        if(edtIdStudent.getText().toString().isEmpty() || edtNameStudent.getText().toString().isEmpty()){
                            v.requestFocus();
                            Toast.makeText(StudentActivity.this, "Enter enought infomations!!", Toast.LENGTH_SHORT).show();
                        }else {
                            addStudentToDatabase();
                            //đóng dialog
                            dialogStudents.dismiss();
                        }
                    }
                });

                btnAddStudentsCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogStudents.dismiss();
                    }
                });
                //Hiển thị dialog ra màn hình
                dialogStudents.show();
            }
        });
    }

    //Phuong thuc dung de gan su kien cho imbAddClass
    private void addClass(){
        imbAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(StudentActivity.this);
                dialog.setContentView(R.layout.add_class_dialog);
                dialog.setTitle("Adds Class");

                edtClassName = (EditText)dialog.findViewById(R.id.edtClassName);
                Button btnAddClass = (Button)dialog.findViewById(R.id.btnAddClass);
                Button btnCancel = (Button)dialog.findViewById(R.id.btnAddClassCancel);

                btnAddClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //gọi đến hàm lấy dữ liệu từ dialog ghi xuống database
                        if(edtClassName.getText().toString().isEmpty()){
                            Toast.makeText(StudentActivity.this, "Enter enought information!!", Toast.LENGTH_SHORT).show();
                            v.requestFocus();
                        }else {
                            addClassToDatabase();
                            dialog.dismiss();
                        }

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    //Phuong thuc dung de lay du lieu tu database đổ vào Spinner trong dialog thêm sinh viên...
    private void loadDataToSpinner(){
        //tạo một ArrayList kiểu String để chứa dữ liệu đọc từ database
        ArrayList<String> arr = new ArrayList<String>();
        Database = database.initDatabase(StudentActivity.this, DATABASE_NAME);
        //Tạo một cursor để truy vấn dữ liệu từ bảng "Class"...
        Cursor cursor = Database.rawQuery("SELECT * FROM Class", null);

        for (int i=0; i<cursor.getCount(); i++){
            //di chuyển cursor đến vị trí thứ i...
            cursor.moveToPosition(i);
            //thêm phần tử lấy được vào ArrayList
            arr.add(cursor.getString(1));
        }
        //tạo ra một adapter để kết nối giữa database và ứng dụng
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(adp);
    }

    //Phương thức dùng để lấy dữ liệu từ dialog thêm lớp, ghi xuống database...
    private void addClassToDatabase(){

            Database = database.initDatabase(this, DATABASE_NAME);
            ContentValues contentValues = new ContentValues();
            contentValues.put("Name",edtClassName.getText().toString());

            Database.insert("Class", null, contentValues);
            Toast.makeText(this, "Insert Sucessfully!", Toast.LENGTH_SHORT).show();
    }

    //Phương thức dùng để lấy dữ liệu từ dialog thêm sinh viên và ghi vào database...
    private void addStudentToDatabase(){

            ContentValues contentValues = new ContentValues();
            contentValues.put("Student_ID", edtIdStudent.getText().toString());
            contentValues.put("Name", edtNameStudent.getText().toString());
            contentValues.put("ClassName", spnClass.getSelectedItem().toString());

            Database = database.initDatabase(StudentActivity.this, DATABASE_NAME);
            Database.insert("Students", null, contentValues);
            Toast.makeText(StudentActivity.this, "Insert Sucessfully!", Toast.LENGTH_SHORT).show();
    }

    private void facebook(){
        //Login Facebook...
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        userName = (TextView)findViewById(R.id.txtUser);
        profilePictureView = (ProfilePictureView)findViewById(R.id.image);
        logOut = (LoginButton)findViewById(R.id.log_out);
        rt = (RelativeLayout)findViewById(R.id.layout_info);



        //Lay thong tin nguoi dung
        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };


        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        //nếu đăng nhập thì hiển thị Ảnh đại diện và tên người dùng
        if (isLoggedIn()){
            userName.setVisibility(View.VISIBLE);
            profilePictureView.setVisibility(View.VISIBLE);

        }
        //Nếu chưa đăng nhập thì ẩn
        else {
            userName.setVisibility(View.GONE);
            profilePictureView.setVisibility(View.GONE);
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectFromFacebook();
            }
        });
    }

    //Hàm kiểm tra trạng thái đăng nhập
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    //Hàm hiển thị Ảnh đại diện và tên người dùng
    private void displayMessage(Profile profile) {
        if (profile != null) {
            userName.setText("Hello: " + profile.getName() + "!");
            profilePictureView.setProfileId(profile.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //log out facebook and changed activity
    private void disconnectFromFacebook(){
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();
                Intent intent = new Intent(StudentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }).executeAsync();
    }
}
