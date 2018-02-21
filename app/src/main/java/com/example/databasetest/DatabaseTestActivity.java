package com.example.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DatabaseTestActivity extends AppCompatActivity implements OnClickListener {

    private Button mbtnCreate, mbtnAdd, mbtnUpdate, mbtnDelete, mbtnQuery;
    private MyDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        mbtnCreate = findViewById(R.id.btn_create);
        mbtnAdd = findViewById(R.id.btn_add);
        mbtnUpdate = findViewById(R.id.btn_update);
        mbtnDelete = findViewById(R.id.btn_delete);
        mbtnQuery = findViewById(R.id.btn_query);

        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

        mbtnCreate.setOnClickListener(this);
        mbtnAdd.setOnClickListener(this);
        mbtnUpdate.setOnClickListener(this);
        mbtnDelete.setOnClickListener(this);
        mbtnQuery.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*
            通过getWritableDatabase或者getReadableDatabase可以获得一个MyDatabaseHelper（可操作性数据库的实例）实例，同时如果数据库未存在则新建一个
            数据库，当磁盘满时，调用读则只会返回一个读的对象，调用写则出现异常
             */
            case R.id.btn_create:
                dbHelper.getWritableDatabase();
                break;


                /*通过dbHelper.getWritableDatabase()获得SQLiteDatabase实例
                 通过获得ContentValues来封装要添加的数据，id值在建表时候已经设置为自增长，所以这不用设置
                 组装数据完成后通过调用SQLiteDatabase中的insert方法给数据库的表添加数据，如果还要继续添加数据记得将values
                 的值清除，然后再次组装数据
                  */
            case R.id.btn_add:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //开始组装第一条数据
                values.put("name", "the first line code");
                values.put("author", "GuoLin");
                values.put("pages", 560);
                values.put("price", 79.00);
                db.insert("Book", null, values);
                values.clear();//清空Values的值
                //开始组装第二条数据
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("price", 49.00);
                db.insert("Book", null, values);
                break;
            /*
                通过构建一个ContentValues 对象组装数据
                调用SQLiteDatabase的update方法更新数据，第三。第四参数是SQL语言中的where语句  指定给那一行更新数据
             */
            case R.id.btn_update:
                SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                ContentValues values1 = new ContentValues();
                values1.put("price", 60.00);
                db1.update("Book", values1, "name = ?", new String[]{"the first line code"});
                break;
                /*
                通过调用 SQLiteDatabase 中的delete方法 删除价格低于50的书本
                 */
            case R.id.btn_delete:
                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                db2.delete("Book","price < ?",new String[]{"50"});
                break;
                /*
                构建SQLiteDatabase对象，通过query方法来获取Cursor实例，，通过Cursor来操作
                moveToFirst()是光标移动到第一行 moveToNext()是下一行
                 在这里因为上面的删除，表中只剩下一个数据   我就全部打印出来
                关于SQL的查询内容很多   第二个参数用于指定去查询那几列，第三第四个参数是约束查询的行，第五个参数指定去group by
                之后的数据，第六个参数是对groupby后的数据过滤，第七个参数用于指定查询结果的排列方式
                在这里后面参数全为null为打印全表数据
                 */
            case R.id.btn_query:
                SQLiteDatabase db3 = dbHelper.getWritableDatabase();
                Cursor cursor = db3.query("Book",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("BookStore", "book name is "+name);
                        Log.d("BookStore", "book author is "+author);
                        Log.d("BookStore", "book pages is "+pages);
                        Log.d("BookStore", "book price is "+price);
                    }while(cursor.moveToNext());
                        cursor.close();
                }
                break;
        }
        ;
    }
}
