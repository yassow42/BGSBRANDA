package com.creativeoffice.bgsbranda.Adapter


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.creativeoffice.bgsbranda.Datalar.MusteriData
import com.creativeoffice.bgsbranda.R
import com.creativeoffice.bgsbranda.SiparisTurleriActivity.PergoleActivity
import com.creativeoffice.bgsbranda.SiparisTurleriActivity.KorukluTenteActivity
import com.creativeoffice.bgsbranda.SiparisTurleriActivity.MafsalliTenteActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_siparis_ekle.view.*
import kotlinx.android.synthetic.main.item_musteri.view.*

import kotlin.collections.ArrayList


class MusteriAdapter(val myContext: Context, val musteriler: ArrayList<MusteriData>,val kullaniciAdi:String) : RecyclerView.Adapter<MusteriAdapter.MusteriHolder>() {

    val ref = FirebaseDatabase.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusteriAdapter.MusteriHolder {
        val myView = LayoutInflater.from(myContext).inflate(R.layout.item_musteri, parent, false)

        return MusteriHolder(myView)
    }

    override fun getItemCount(): Int {
        return musteriler.size
    }

    override fun onBindViewHolder(holder: MusteriAdapter.MusteriHolder, position: Int) {
        val itemData = musteriler[position]
        holder.setData(musteriler[position])


        holder.btnSiparisEkle.setOnClickListener {


            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
            var viewDialog = inflate(myContext, R.layout.dialog_siparis_ekle, null)

            builder.setTitle(itemData.musteri_ad_soyad)

            viewDialog.tvTente.setOnClickListener {
                val intent = Intent(myContext, MafsalliTenteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey",itemData.musteri_key)
                myContext.startActivity(intent)
            }

            viewDialog.tvKorukluTente.setOnClickListener {
                val intent = Intent(myContext, KorukluTenteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey",itemData.musteri_key)
                myContext.startActivity(intent)
            }

            viewDialog.tvKisBahcesi.setOnClickListener {
                val intent = Intent(myContext, PergoleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey",itemData.musteri_key)
                myContext.startActivity(intent)
            }

            builder.setView(viewDialog)
            var dialogSiparisTuru: Dialog = builder.create()
            dialogSiparisTuru.show()

        }


    }

    inner class MusteriHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var musteriAdi = itemView.tvMusteriAdi
        var btnSiparisEkle = itemView.tvSiparisEkle
        var musteriTel = itemView.tvMusteriTel


        fun setData(musteriData: MusteriData) {

            musteriAdi.text = musteriData.musteri_ad_soyad.toString()
            musteriTel.text = musteriData.musteri_tel.toString()

        }


    }


}
