package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyHelper myHelper;
    private EditText mEtName, mEtClass, mEtCourse, mEtPhone;
    private TextView mTvShow;
    private Button mBtnAdd, mBtnQuery, mBtnUpdate, mBtnDelete;
    private SQLiteDatabase db;
    private ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myHelper = new MyHelper(this);
        init();
    }

    private void init() {
        mEtName = findViewById(R.id.et_name);
        mEtClass = findViewById(R.id.et_class);
        mEtCourse = findViewById(R.id.et_course);
        mEtPhone = findViewById(R.id.et_phone);
        mTvShow = findViewById(R.id.tv_show);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnQuery = findViewById(R.id.btn_query);
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnDelete = findViewById(R.id.btn_delete);
        mBtnAdd.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name, phone, className;
        int id = v.getId();
        if (id == R.id.btn_add) {
            name = mEtName.getText().toString();
            className = mEtClass.getText().toString();
            phone = mEtPhone.getText().toString();
            db = myHelper.getWritableDatabase();
            values = new ContentValues();
            values.put("name", name);
            values.put("class", mEtClass.getText().toString());
            values.put("phone", phone);
            values.put("course", mEtCourse.getText().toString());
            db.insert("information", null, values);
            Toast.makeText(this, "信息成功添加", Toast.LENGTH_LONG).show();
            db.close();
        } else if (id == R.id.btn_query) {
            db = myHelper.getReadableDatabase();
            Cursor cursor = db.query("information", null, null, null, null, null, null);
            if (cursor.getCount() == 0) {
                mTvShow.setText("");
                Toast.makeText(this, "没有数据", Toast.LENGTH_LONG).show();
            } else {
                cursor.moveToFirst();
                mTvShow.setText("Name: " + cursor.getString(1) + " Class: " + cursor.getString(2) + " Course: " + cursor.getString(3) + " Tel: " + cursor.getString(4));
            }

            while (cursor.moveToNext()) {
                mTvShow.append("\n" + "Name :  " + cursor.getString(1) + " ; Class: " + cursor.getString(2) + " ; Course: " + cursor.getString(3) + " ; Tel: " + cursor.getString(4));
            }
            cursor.close();
            db.close();


    } else if (id == R.id.btn_update) {
            db = myHelper.getWritableDatabase();
            values = new ContentValues();
            values.put("phone", mEtPhone.getText().toString());
            values.put("class", mEtClass.getText().toString());
            values.put("course", mEtCourse.getText().toString());
            db.update("information", values, "name=?", new String[]{mEtName.getText().toString()});
            Toast.makeText(this, "信息已被修改", Toast.LENGTH_LONG).show();
            db.close();
        } else if (id == R.id.btn_delete) {
            String names = mEtName.getText().toString();
            db = myHelper.getWritableDatabase();
            int result = db.delete("information", "name=?", new String[]{names});
            if (result > 0) {
                Toast.makeText(this, "信息已被删除", Toast.LENGTH_LONG).show();
                mTvShow.setText("");
            } else {
                Toast.makeText(this, "没有找到对应的信息", Toast.LENGTH_LONG).show();
            }
            db.close();
        }
    }


    class MyHelper extends SQLiteOpenHelper {
        public MyHelper(Context context) {
            super(context, "itcast.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE information(_id INTEGER PRIMARY KEY AUTOINCREMENT, name CARCHAR(20), class VARCHAR(20), phone VARCHAR(20), course VARCHAR(20))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
