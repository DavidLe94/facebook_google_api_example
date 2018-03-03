package com.example.asus.androidadvance_assignment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowStudentsActivity extends AppCompatActivity {
    String DATABASE_NAME = "StudentsManagement.sqlite";
    SQLiteDatabase Database;
    ArrayList<students> arrList;
    StudentAdapter adapter;
    ListView lvStudents;

    //Khai bao bien cho dialog update student
    Spinner spnUpdateClass;
    EditText edtUpdateStuId;
    EditText edtUpdateStuName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);
        init();
        readDataShowListView();
    }

    private void init() {
        lvStudents = (ListView)findViewById(R.id.lvShowStudents);
        arrList = new ArrayList<students>();
        adapter = new StudentAdapter(arrList, ShowStudentsActivity.this);
        lvStudents.setAdapter(adapter);
        registerForContextMenu(lvStudents);
        adapter.notifyDataSetChanged();
    }

    private void readDataShowListView(){
        Database = database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = Database.rawQuery("SELECT * FROM Students", null);
        arrList.clear();
        for (int i=0; i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String stuID = cursor.getString(1);
            String stuName = cursor.getString(2);
            String className = cursor.getString(3);

            arrList.add(new students(id, stuID, stuName, className));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_show_students, menu);
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        students stu = arrList.get(info.position);

        //lấy ra id của sinh viên
        final String stuID = stu.getStudentsID();
        switch(id){
            case R.id.btnDeleteStudent:
                delete(stuID);
                break;
            case R.id.btnUpdateStudent:
                update(stuID);
                break;
        }
        return super.onContextItemSelected(item);
    }

    //Phuong thuc dung de xoa sinh vien khoi danh sach sinh vien...
    private void delete(final String stuID){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure delete?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_delete);

        alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database = database.initDatabase(ShowStudentsActivity.this, DATABASE_NAME);
                Database.delete("Students", "Student_ID = ?", new String[]{stuID});
                arrList.clear();
                Cursor cursor = Database.rawQuery("SELECT * FROM Students", null);
                while (cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    String stuId = cursor.getString(1);
                    String stuName = cursor.getString(2);
                    String className = cursor.getString(3);

                    arrList.add(new students(id, stuId, stuName, className));
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(ShowStudentsActivity.this, "Delete Sucessfully!", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //phương thức dùng để update thông tin sinh viên
    private void update(final String stuID){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.update_students_layout);
        dialog.setTitle("Update Information Studens");

        spnUpdateClass = (Spinner)dialog.findViewById(R.id.spnUpdateClass);
        edtUpdateStuId = (EditText)dialog.findViewById(R.id.edtUpdateStuId);
        edtUpdateStuName = (EditText)dialog.findViewById(R.id.edtUpdateStuName);
        Button btnOk = (Button)dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);

        loadDataToSpinnerUpdateClass();
        showInfoStuToDialog(stuID);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("Student_ID", edtUpdateStuId.getText().toString());
                contentValues.put("Name", edtUpdateStuName.getText().toString());
                contentValues.put("ClassName", spnUpdateClass.getSelectedItem().toString());

                Database = database.initDatabase(ShowStudentsActivity.this, DATABASE_NAME);
                Database.update("Students", contentValues, "Student_ID = ?", new String[]{stuID});
                arrList.clear();
                Cursor cursor = Database.rawQuery("SELECT * FROM Students", null);
                while (cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    String stuId = cursor.getString(1);
                    String stuName = cursor.getString(2);
                    String className = cursor.getString(3);

                    arrList.add(new students(id, stuId, stuName, className));
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(ShowStudentsActivity.this, "Update Sucessfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        adapter.notifyDataSetChanged();
    }

    //hiển thị thông tin sinh viên đọc được từ db lên dialog
    private void showInfoStuToDialog(String stuId){
        //ArrayList<String> arr = new ArrayList<String>();
        Database = database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = Database.rawQuery("SELECT * FROM Students WHERE Student_ID = ?", new String[]{stuId});
        cursor.moveToFirst();

        edtUpdateStuId.setText(cursor.getString(1));
        edtUpdateStuName.setText(cursor.getString(2));

    }

    //
    private void loadDataToSpinnerUpdateClass(){
        ArrayList<String> arr = new ArrayList<String>();
        Database = database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = Database.rawQuery("SELECT * FROM Class",null);
        for(int i=0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            arr.add(cursor.getString(1));
        }

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUpdateClass.setAdapter(adp);
    }
}
