package com.example.ksleruygulamas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class VeriTabani extends SQLiteOpenHelper {
    public VeriTabani(@Nullable Context context) {
        super(context, "kisiler.rehber", null, 1);       //context:veritabanına giden yol,VeriTabanı ismi:kisiler.sqLite,version:1 çünkü ilk defa veritabanı oluşturuluyor.

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE kisiler(kisi_id INTEGER PRIMARY KEY AUTOINCREMENT,kisi_ad TEXT,kisi_tel TEXT);"); //Sql sorgusunu veritabanında çalıştırarak ilgili tabloları yaratmamızda yardımcı oldu.

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS kisiler");                    //Bir sıkıntı olduğunda kisiler 'i sil
        onCreate(sqLiteDatabase);

    }
}
