package com.example.ksleruygulamas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements  SearchView.OnQueryTextListener{      //implements ile MainActivity'e bağlıyoruz.ve iki tane metod gerekiyor bununla birlikte

    private Toolbar toolbar;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private ArrayList<Kisiler>KisilerArrayList;                 //Kisiler sınıfından ArrayList oluşturuldu.
    private KisilerAdapter adapter;                             //KisilerAdapter sınıfından  adapter nesnesi oluşturuldu.
    private VeriTabani vt;

    private int STORAGE_PERMISSION_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar=(Toolbar) findViewById(R.id.toolbar);
        rv=(RecyclerView) findViewById(R.id.rv);
        fab=(FloatingActionButton) findViewById(R.id.fab);

        vt=new VeriTabani(this);
        toolbar.setTitle("Kişiler Uygulaması");
        setSupportActionBar(toolbar);

        KisilerArrayList=new KisilerDb().tümKisiler(vt);                             //tüm kişiler alınmış oldu.


        rv.setHasFixedSize(true);                                                   //rv sabitlenmesi sağlanır.
        rv.setLayoutManager(new LinearLayoutManager(this));                 //tasarım türü belirtildi.
       /* KisilerArrayList=new ArrayList<>();

        Kisiler kisi1=new Kisiler(1,"Büsra Güler","541 619 30 42");                //Kişiler  nesneleri oluşturuldu.
        Kisiler kisi2=new Kisiler(2,"Ibrahım Guler","542 328 34 34");
        Kisiler kisi3=new Kisiler(2,"Ayse Yılmaz","534 938 49 39");

        KisilerArrayList.add(kisi1);                                                                  //Oluşturulan kisiler KisilerArrayList' e eklendi.
        KisilerArrayList.add(kisi2);
        KisilerArrayList.add(kisi3);
*/
        adapter=new KisilerAdapter(this,KisilerArrayList,vt);                               //verileri adaptere yerleştirdik.
        rv.setAdapter(adapter);





       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               alertGoster();
           }
       });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                              //Search menüsünü Main Activity'e bağladık.onCreateOptionsMenu ile

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);                     //Search butonunu aktif hale getirebilmek için

        MenuItem menuItem=menu.findItem(R.id.action_ara);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {                         //veri yazıldığında arama yapar.
        Log.e("onQueryTextSubmit",query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {                                                  //Harf girdikçe veye sildikçe arama yapar.
        Log.e("onQueryTextChange",newText);
        KisilerArrayList=new KisilerDb().kisiAra(vt,newText);                                              //kişiyi ekleyip ardından veri çekildi.
        adapter=new KisilerAdapter(MainActivity.this,KisilerArrayList,vt);                     //verileri adaptere yerleştirdik.
        rv.setAdapter(adapter);
        return false;
    }

    public void alertGoster(){
        LayoutInflater layout= LayoutInflater.from(this);                     //Dışarıdan tasarıma erişilmesini sağlar.
        View tasarim =layout.inflate(R.layout.alert_tasarim,null);

        final EditText editTextAd  =tasarim.findViewById(R.id.editTextAd);
        final EditText editTextTel =tasarim.findViewById(R.id.editTextTel);


        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("Kişi Ekle");                                               //fab' a tıklandığında Kişi Ekle EditView'i açılır.Ve kisiadı ve kisiteli istenir.
        ad.setView(tasarim);
        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String kisi_ad = editTextAd.getText().toString().trim();
                String kisi_tel = editTextTel.getText().toString().trim();

                new KisilerDb().KisiEkle(vt,kisi_ad,kisi_tel);
                KisilerArrayList=new KisilerDb().tümKisiler(vt);                                              //kişiyi ekleyip ardından veri çekildi.
                adapter=new KisilerAdapter(MainActivity.this,KisilerArrayList,vt);                     //verileri adaptere yerleştirdik.
                rv.setAdapter(adapter);

                Toast.makeText(getApplicationContext(), kisi_ad+" -"+"kisi_tel",Toast.LENGTH_SHORT).show();   //Kişi ekle dendiğinde kişiyi ekleyecek ve kişi orbitalini göstericek.
            }
        }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        ad.create().show();


    }
}