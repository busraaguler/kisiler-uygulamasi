package com.example.ksleruygulamas;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class KisilerAdapter extends RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu>{   //Sayfayı Adapter'e bağladık.
    private Context mContext;                                                            //İşletim sisteminin bize sunduğu özelliklere erişebilmeyi sağlar.
    private List<Kisiler> KisilerListe;                                                  //Kisiler sıınıfından KisilerLİste adınds bir liste oluşturuldu.
    private VeriTabani vt;

    public KisilerAdapter(Context mContext,List<Kisiler>KisilerListe,VeriTabani vt){      //constructor oluşturuldu.
        this.mContext=mContext;
        this.KisilerListe=KisilerListe;
        this.vt=vt;
    }


    public KisilerAdapter(Context mContext,List<Kisiler> KisilerListe){
        this.mContext=mContext;
        this.KisilerListe=KisilerListe;
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kisi_card_tasarim,parent,false);
        return new CardTasarimTutucu(view);                      //Constructor'a view ile tasarımı eklemek istiyorum.
    }

    @Override
    public void onBindViewHolder(@NonNull final CardTasarimTutucu holder, int position) {       //Kaç tane veri varsa o kadar çalışıcak.Position ile sırayla indeks numarası girecek.
    final  Kisiler kisi=KisilerListe.get(position);                                                //Hangi satırdaki veriyi aldığımı görebilirim.
        holder.TextViewKisiBilgi.setText(kisi.getKisi_ad()+" -" +kisi.getKisi_tel());           //holder nesnesi görsel nesnelere erişmek içindir.
        holder.ImageViewNokta.setOnClickListener(new View.OnClickListener() {                   //butonun tıklanabilir olması sağlandı.
            @Override
            public void onClick(View view) {

                PopupMenu popupmenu=new PopupMenu(mContext,holder.ImageViewNokta);  //ImageViewNokta'ya tıklandığında popupmenu çalışsın.
                popupmenu.getMenuInflater().inflate(R.menu.pop_up_menu,popupmenu.getMenu());                   //popupmenu'nün  açılması sağlanır.

                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {                                                          //Tıklanıldığında işlemler yapılmasını sağlayacaktır.
                            case R.id.action_sil:
                                Snackbar.make(holder.ImageViewNokta, "Kişi Silinsin Mi?", Snackbar.LENGTH_SHORT)
                                        .setAction("Evet", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                              new KisilerDb().KisiSil(vt,kisi.getKisi_id());
                                              KisilerListe=new KisilerDb().tümKisiler(vt);
                                              notifyDataSetChanged();
                                            }
                                        })
                                        .show(); //Sil tıklandı diye bir snackbar oluşturuldu.
                                return true;
                            case R.id.action_guncelle:
                                alertGoster(kisi);                   //Main Activity'deki alertGoster() çağrılır.Pop_up_menu 'deki sil ve güncelle seçenekleri çalışır.
                                return true;
                            default:
                                return false;

                        }
                    }
                });

                popupmenu.show();           //Kullanıcıya görüntülemek için show() metodu çağırılır.



            }
        });
    }

    @Override
    public int getItemCount() {                                  //Kaç tane veri olduğunu
        return KisilerListe.size();
    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder{         //kisi_card_tasarim'daki görsel nesneleri bağlayabilmek için extends edildi.
        private TextView TextViewKisiBilgi;
        private ImageView ImageViewNokta;

        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);
            TextViewKisiBilgi = itemView.findViewById(R.id.textViewKisiBilgi);
            ImageViewNokta=itemView.findViewById(R.id.imageViewNokta);

        }
    }
    public void alertGoster(final Kisiler kisi){
        LayoutInflater layout= LayoutInflater.from(mContext);                  //Dışarıdan tasarıma erişilmesini sağlar.
        View tasarim =layout.inflate(R.layout.alert_tasarim,null);

        final EditText editTextAd  =tasarim.findViewById(R.id.editTextAd);
        final EditText editTextTel =tasarim.findViewById(R.id.editTextTel);

        editTextAd.setText(kisi.getKisi_ad());                                 //Güncellenen kisinin bilgileri editText'in içine aktarır.
        editTextTel.setText(kisi.getKisi_tel());


        AlertDialog.Builder ad=new AlertDialog.Builder(mContext);
        ad.setTitle("Kişi Güncelle");
        ad.setView(tasarim);
        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String kisi_ad = editTextAd.getText().toString().trim();
                String kisi_tel = editTextTel.getText().toString().trim();

                new KisilerDb().KisiGüncelle(vt,kisi.getKisi_id(),kisi_ad,kisi_tel);

                KisilerListe=new KisilerDb().tümKisiler(vt);
                notifyDataSetChanged();

                Toast.makeText(mContext, kisi_ad+" -"+"kisi_tel",Toast.LENGTH_SHORT).show();   //Kişi ekle dendiğinde kişiyi ekleyecek ve kişi orbitalini göstericek.
            }
        }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        ad.create().show();


    }
}
