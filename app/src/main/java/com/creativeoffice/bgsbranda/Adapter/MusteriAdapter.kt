package com.creativeoffice.bgsbranda.Adapter


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.creativeoffice.bgsbranda.Activity.MusterilerActivity
import com.creativeoffice.bgsbranda.Datalar.MusteriData
import com.creativeoffice.bgsbranda.R
import com.creativeoffice.bgsbranda.SiparisTurleriActivity.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_gidilen_musteri.view.*
import kotlinx.android.synthetic.main.dialog_siparis_ekle.view.*
import kotlinx.android.synthetic.main.item_musteri.view.*

import kotlin.collections.ArrayList


class MusteriAdapter(val myContext: Context, val musteriler: ArrayList<MusteriData>, val kullaniciAdi: String) : RecyclerView.Adapter<MusteriAdapter.MusteriHolder>() {

    val ref = FirebaseDatabase.getInstance().reference
    lateinit var dialogMsDznle: Dialog

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
                intent.putExtra("musteriKey", itemData.musteri_key)
                myContext.startActivity(intent)
            }

            viewDialog.tvKorukluTente.setOnClickListener {
                val intent = Intent(myContext, KorukluTenteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey", itemData.musteri_key)
                myContext.startActivity(intent)
            }

            viewDialog.tvKisBahcesi.setOnClickListener {
                val intent = Intent(myContext, PergoleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey", itemData.musteri_key)
                myContext.startActivity(intent)
            }
            viewDialog.tvSemsiye.setOnClickListener {
                val intent = Intent(myContext, SemsiyeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey", itemData.musteri_key)
                myContext.startActivity(intent)
            }
            viewDialog.tvKarpuz.setOnClickListener {
                val intent = Intent(myContext, KarpuzTenteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey", itemData.musteri_key)
                myContext.startActivity(intent)
            }
            viewDialog.tvSeffaf.setOnClickListener {
                val intent = Intent(myContext, SeffafTenteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey", itemData.musteri_key)
                myContext.startActivity(intent)
            }
            viewDialog.tvWintent.setOnClickListener {
                val intent = Intent(myContext, WintentActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey", itemData.musteri_key)
                myContext.startActivity(intent)

            }
            viewDialog.tvDiger.setOnClickListener {
                val intent = Intent(myContext, DigerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey", itemData.musteri_key)
                myContext.startActivity(intent)

            }
            viewDialog.tvTamir.setOnClickListener {
                val intent = Intent(myContext, TamirActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.putExtra("musteriKey", itemData.musteri_key)
                myContext.startActivity(intent)

            }

            builder.setView(viewDialog)
            var dialogSiparisTuru: Dialog = builder.create()
            dialogSiparisTuru.show()

        }

        holder.itemView.setOnLongClickListener {

            val popup = PopupMenu(myContext, holder.itemView)
            popup.inflate(R.menu.popup_menu_musteri)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.popDüzenleMusteri -> {

                        var musteriKey = musteriler[position].musteri_key.toString()
                        var builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(myContext)

                        var dialogView: View = inflate(myContext, R.layout.dialog_gidilen_musteri, null)
                        builder.setView(dialogView)

                        dialogMsDznle = builder.create()
                        dialogView.tvAdSoyad.text = musteriler[position].musteri_ad_soyad.toString()
                        dialogView.etAdresGidilen.setText(musteriler[position].musteri_adres.toString())
                        dialogView.etTelefonGidilen.setText(musteriler[position].musteri_tel.toString())

                        dialogView.imgCheck.setOnClickListener {

                            if (dialogView.etAdresGidilen.text.toString().isNotEmpty() && dialogView.etTelefonGidilen.text.toString().isNotEmpty()) {
                                var adres = dialogView.etAdresGidilen.text.toString()
                                var telefon = dialogView.etTelefonGidilen.text.toString()

                                ref.child("Musteriler").child(musteriKey).child("musteri_adres").setValue(adres)
                                ref.child("Musteriler").child(musteriKey).child("musteri_tel").setValue(telefon)
                                Toast.makeText(myContext, "Müşteri Bilgileri Güncellendi", Toast.LENGTH_LONG).show()
                                dialogMsDznle.dismiss()

                            } else {
                                Toast.makeText(myContext, "Bilgilerde boşluklar var", Toast.LENGTH_LONG).show()
                            }
                        }
                        dialogView.imgBack.setOnClickListener {

                            dialogMsDznle.dismiss()
                        }

                        dialogMsDznle.setCancelable(false)

                        dialogMsDznle.show()
                    }
                    R.id.popSilMusteri -> {

                        var alert = AlertDialog.Builder(myContext)
                            .setTitle("Müşteriyi Sil")
                            .setMessage("Emin Misin ?")
                            .setPositiveButton("Sil", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    ref.child("Musteriler").child(musteriler[position].musteri_key.toString()).removeValue()
                                    myContext.startActivity(Intent(myContext, MusterilerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                                    ref.child("Silinenler/Musteriler").child(itemData.musteri_key.toString()).setValue(itemData)


                                }
                            })
                            .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0!!.dismiss()
                                }
                            }).create()

                        alert.show()


                    }


                }
                return@OnMenuItemClickListener true
            })

            popup.show()
            return@setOnLongClickListener true
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
