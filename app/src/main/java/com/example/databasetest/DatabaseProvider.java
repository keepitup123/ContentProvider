package com.example.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    //权限标志  通常以包名命名
    public static final String AUTHORITY = "com.example.databasetest.provider";

    private MyDatabaseHelper dbHelper;
    private static UriMatcher uriMatcher;
    //静态代码块来注册uriMatcher,以匹配等下进行match判断是哪一个uri，这里通过创建一个UriMatch实例
    //通过调用addURI方法传入注册内容
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }


    //无参的构造方法
    public DatabaseProvider() {
    }

    /*
    这里以一个接口方式，去处理删除数据的请求
    通过Helper的getWritableDatabase方法创建一个SQLliteDatabase对象，通过该对象去调用数据库CRUD操作
    通过uriMatch.match方法来进行匹配判断，通过返回的代码块来确定是要操作那些数据
    定义一个int型的变量接收delete方法返回的int数据，该数据是反应删除了那些行
    最后返回反应删除得行的int型数据
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deletedRows = db.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Book", selection, selectionArgs);
                break;
            case CATEGORY_DIR:
                deletedRows = db.delete("Book", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Category", "id = ?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return deletedRows;
        //throw new UnsupportedOperationException("Not yet implemented");
    }
    /*
    通过匹配结果返回的代码块来选择返回的MIME
     */
    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category";
        }
       // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
    /*
     这里以一个接口方式，去处理添加数据的请求
    通过Helper的getWritableDatabase方法创建一个SQLliteDatabase对象，通过该对象去调用数据库CRUD操作
    通过uriMatch.match方法来进行匹配判断，通过返回的代码块来确定是要操作那些数据
    并返回一个Uri实例
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
                break;
            default:
                break;
        }
        return uriReturn;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /*
     这里以一个接口方式，去初始化你的contentprovider数据的请求
     这里实例化Helper，并传入当前的context 和数据库名
     */
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return false;
    }
    /*
     这里以一个接口方式，去处理查询数据的请求
    通过Helper的getReadableDatabase方法创建一个SQLliteDatabase对象，通过该对象去调用数据库CRUD操作
    通过uriMatch.match方法来进行匹配判断，通过返回的代码块来确定是要操作那些数据
    该方法会返回一个Cursor对象，该对象带有查询的数据结果
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book", projection, "id = ?", new String[]{bookId},
                        null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category", projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category", projection, "id = ?", new String[]{categoryId},
                        null, null, sortOrder);
                break;
            default:
        }
        return cursor;
        //  throw new UnsupportedOperationException("Not yet implemented");
    }
    /*
     这里以一个接口方式，去处理更新数据的请求
    通过Helper的getWritableDatabase方法创建一个SQLliteDatabase对象，通过该对象去调用数据库CRUD操作
    通过uriMatch.match方法来进行匹配判断，通过返回的代码块来确定是要操作那些数据
    该方法会返回一个int变量，反应了更新了那些行
    通过对传入的uri对象调用getSegments（）get(1)方法获取新的表的数据的id值
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updetaRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updetaRows = db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updetaRows = db.update("Book", values, "id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updetaRows = db.update("Category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updetaRows = db.update("Category", values, "id = ?", new String[]{categoryId});
                break;
            default:
                break;

        }
        return updetaRows;
        // throw new UnsupportedOperationException("Not yet implemented");
    }
}
