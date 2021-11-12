package com.example.ksleruygulamas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class KisilerDb {                                                //Arayüzdeki nesnelerin veri tabanına erişebilmesi için KisilerDb sınıfı oluşturuldu.

    public ArrayList<Kisiler>tümKisiler(VeriTabani vt){
        ArrayList<Kisiler> kisilerArrayList=new ArrayList<>();
        SQLiteDatabase db=vt.getWritableDatabase();

        Cursor c =db.rawQuery("SELECT * FROM kisiler",null);   //Cursor üzerinden veritabanına sorgumuzu atıyoruz.
        while(c.moveToNext()){                                                  //Satır satır gelen verileri doldurucak.
            Kisiler k=new Kisiler(c.getInt(c.getColumnIndex("kisi_id")),
                    c.getString(c.getColumnIndex("kisi_ad")),
                    c.getString(c.getColumnIndex("kisi_tel")));
            kisilerArrayList.add(k);                                            //KisilerArrayList'e k nesnesini ekledik.
        }
        db.close();
        return kisilerArrayList;                                                //Tüm kişileri getirecek.

    }

    public ArrayList<Kisiler>kisiAra(VeriTabani vt,String AramaKelime){        //Search kısımdaki kelime aratarak kişileri bulmamızı sağlar.
        ArrayList<Kisiler> kisilerArrayList=new ArrayList<>();
        SQLiteDatabase db=vt.getWritableDatabase();

        Cursor c =db.rawQuery("SELECT * FROM kisiler WHERE kisi_ad like '%"+AramaKelime+"%'",null);   //Cursor üzerinden veritabanına sorgumuzu atıyoruz.
        while(c.moveToNext()){                                                  //Satır satır gelen verileri doldurucak.
            Kisiler k=new Kisiler(c.getInt(c.getColumnIndex("kisi_id")),
                    c.getString(c.getColumnIndex("kisi_ad")),
                    c.getString(c.getColumnIndex("kisi_tel")));
            kisilerArrayList.add(k);                                            //KisilerArrayList'e k nesnesini ekledik.
        }
        db.close();
        return kisilerArrayList;                                                //Tüm kişileri getirecek.

    }

    public void KisiSil(VeriTabani vt,int kisi_id){                             //sil metodu
        SQLiteDatabase db=vt.getWritableDatabase();
        db.delete("kisiler","kisi_id=?",new String[]{String.valueOf(kisi_id)}); //Kisiler tablosundan kisi_id=? olan kisileri sil.
        db.close();

    }
    public void KisiEkle(VeriTabani vt, String kisi_ad, String kisi_tel){                //Kisi ekle metodu
        SQLiteDatabase db=vt.getWritableDatabase();
        ContentValues values=new ContentValues();                                  //verilerin toplanmasını sağlar.

        values.put("kisi_ad",kisi_ad);                                             //yazilan kisi adını ekle
        values.put("kisi_tel",kisi_tel);                                           //yazılan kisi tel nosunu ekle
        db.insertOrThrow("kisiler",null,values);              //Kisiler isimli tabloya values değerleri gönderildi.insertOrThrow veri tabanına kayıt eklemek için kullanılır.
        db.close();


    }
    public void KisiGüncelle(VeriTabani vt, int kisi_id, String kisi_ad, String kisi_tel){   //Kisi Güncelle metodu
        SQLiteDatabase db=vt.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("kisi_ad",kisi_ad);                                             //kisi adını güncelle
        values.put("kisi_tel",kisi_tel);                                           //kisi teli güncelle
        db.update("kisiler",values,"kisi_id=?",new String[]{String.valueOf(kisi_id)}); // Kisiler tablosundan kisi_id=? olanları güncelle.
        db.close();


    }

}
