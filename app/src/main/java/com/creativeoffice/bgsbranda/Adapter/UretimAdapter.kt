package com.creativeoffice.bgsbranda.Adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.creativeoffice.bgsbranda.Activity.UretimActivity

import com.creativeoffice.bgsbranda.Datalar.SiparisData
import com.creativeoffice.bgsbranda.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.*
import kotlinx.android.synthetic.main.dialog_tente.view.*
import kotlinx.android.synthetic.main.dialog_tente.view.etAcilim
import kotlinx.android.synthetic.main.dialog_tente.view.etCephe
import kotlinx.android.synthetic.main.dialog_tente.view.etKumasKodu
import kotlinx.android.synthetic.main.dialog_tente.view.etProfilRengi
import kotlinx.android.synthetic.main.dialog_tente.view.etSacakTuru
import kotlinx.android.synthetic.main.dialog_tente.view.etSacakYazisi
import kotlinx.android.synthetic.main.dialog_tente.view.etSiparisNotu

import kotlinx.android.synthetic.main.item_siparis.view.tvMusteriAdi
import kotlinx.android.synthetic.main.item_siparis.view.tvMusteriTel
import kotlinx.android.synthetic.main.item_siparis.view.tvSiparisTuru
import kotlinx.android.synthetic.main.item_uretim.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UretimAdapter(val myContext: Context, val uretimler: ArrayList<SiparisData>, val kullaniciKey: String) : RecyclerView.Adapter<UretimAdapter.UretimHolder>() {
    val ref = FirebaseDatabase.getInstance().reference
    val uretimRef = FirebaseDatabase.getInstance().reference.child("Uretim")

    var musteriAdi: String? = null
    var musteriTelNo: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UretimAdapter.UretimHolder {
        val myView = LayoutInflater.from(myContext).inflate(R.layout.item_uretim, parent, false)

        return UretimHolder(myView)
    }


    override fun getItemCount(): Int {
        return uretimler.size
    }

    override fun onBindViewHolder(holder: UretimAdapter.UretimHolder, position: Int) {
        val itemData = uretimler[position]
        holder.setData(uretimler[position])

        holder.itemView.setOnLongClickListener {
            val popup = PopupMenu(myContext, holder.itemView)
            popup.inflate(R.menu.popup_menu_uretim)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.popUretimiTamamla -> {
                        var alert = AlertDialog.Builder(myContext)
                            .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                        ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {}
                                            override fun onDataChange(p0: DataSnapshot) {
                                                try {
                                                    var tenteData = p0.getValue(SiparisData.TenteData::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()
                                                    }
                                                } catch (e: Exception) {
                                                    Log.e("Tek", "Uretim adapter tamamlama hatası")

                                                }

                                            }
                                        })
                                    }
                                }
                            })
                            .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0!!.dismiss()
                                }
                            }).create()

                        alert.show()
                    }
                    R.id.popDüzenle -> {
                        if (itemData.siparis_turu == "Tente") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = View.inflate(myContext, R.layout.dialog_tente, null)
                            uretimRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    var gelenData = p0.getValue(SiparisData.TenteData::class.java)!!
                                    viewDialog.etCephe.setText(gelenData.tente_cephe)
                                    viewDialog.etAcilim.setText(gelenData.tente_acilim)
                                    viewDialog.etKumasKodu.setText(gelenData.tente_kumaskodu)
                                    viewDialog.etSacakTuru.setText(gelenData.tente_sacak)
                                    viewDialog.etSacakYazisi.setText(gelenData.tente_sacak_yazisi)
                                    viewDialog.etSanziman.setText(gelenData.sanzimanYonu)
                                    viewDialog.etProfilRengi.setText(gelenData.profilRengi)
                                    viewDialog.etSiparisNotu.setText(itemData.siparis_notu)
                                }
                            })
                            builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.dismiss()
                                }
                            })
                            builder.setPositiveButton("Güncelle", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    Toast.makeText(myContext, "Sipariş Güncellendi", Toast.LENGTH_LONG).show()
                                    var cephe = viewDialog.etCephe.text.toString()
                                    var acilim = viewDialog.etAcilim.text.toString()
                                    var kumasKodu = viewDialog.etKumasKodu.text.toString()
                                    var sacakTuru = viewDialog.etSacakTuru.text.toString()
                                    var sacakYazisi = viewDialog.etSacakYazisi.text.toString()
                                    var sanziman = viewDialog.etSanziman.text.toString()
                                    var profilRengi = viewDialog.etProfilRengi.text.toString()
                                    var siparisNot = viewDialog.etSiparisNotu.text.toString()
                                    var guncelData = SiparisData.TenteData(cephe, acilim, kumasKodu, sacakTuru, sacakYazisi, sanziman, profilRengi, itemData.siparis_key)
                                    uretimRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    uretimRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                    myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

                                }
                            })

                            builder.setView(viewDialog)
                            var dialogSiparisTuru: Dialog = builder.create()
                            dialogSiparisTuru.show()
                        }
                        if (itemData.siparis_turu == "Körüklü Tente") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = View.inflate(myContext, R.layout.dialog_koruklu_tente, null)

                            uretimRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    var gelenData = p0.getValue(SiparisData.KorukluTenteData::class.java)!!
                                    viewDialog.etCephe.setText(gelenData.tente_cephe)
                                    viewDialog.etAcilim.setText(gelenData.tente_acilim)
                                    viewDialog.etKumasKodu.setText(gelenData.tente_kumaskodu)
                                    viewDialog.etSacakTuru.setText(gelenData.tente_sacak)
                                    viewDialog.etSacakYazisi.setText(gelenData.tente_sacak_yazisi)
                                    viewDialog.etİpYonu.setText(gelenData.ipYonu)
                                    viewDialog.etProfilRengi.setText(gelenData.profilRengi)
                                    viewDialog.etSiparisNotu.setText(itemData.siparis_notu)
                                }
                            })


                            builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.dismiss()
                                }


                            })
                            builder.setPositiveButton("Güncelle", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    Toast.makeText(myContext, "Sipariş Güncellendi", Toast.LENGTH_LONG).show()
                                    var cephe = viewDialog.etCephe.text.toString()
                                    var acilim = viewDialog.etAcilim.text.toString()
                                    var kumasKodu = viewDialog.etKumasKodu.text.toString()
                                    var sacakTuru = viewDialog.etSacakTuru.text.toString()
                                    var sacakYazisi = viewDialog.etSacakYazisi.text.toString()
                                    var ipyonu = viewDialog.etİpYonu.text.toString()
                                    var profilRengi = viewDialog.etProfilRengi.text.toString()
                                    var siparisNot = viewDialog.etSiparisNotu.text.toString()
                                    var guncelData = SiparisData.KorukluTenteData(cephe, acilim, kumasKodu, sacakTuru, sacakYazisi, ipyonu, profilRengi, itemData.siparis_key)
                                    uretimRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    uretimRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                    myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

                                }

                            })
                            builder.setView(viewDialog)
                            var dialogSiparisTuru: Dialog = builder.create()
                            dialogSiparisTuru.show()
                        }
                    }
                    R.id.popSil -> {

                        var alert = AlertDialog.Builder(myContext)
                            .setTitle("Siparişi Sil")
                            .setMessage("Emin Misin ?")
                            .setPositiveButton("Sil", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    ref.child("SilinenUretimler").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                        uretimRef.child(itemData.siparis_key.toString()).removeValue()
                                    }
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

        holder.itemView.setOnClickListener {

            if (itemData.siparis_turu == "Tente") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = View.inflate(myContext, R.layout.dialog_tente, null)

                builder.setView(viewDialog)
                uretimRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.TenteData::class.java)!!
                        viewDialog.etCephe.setText(gelenData.tente_cephe)
                        viewDialog.etAcilim.setText(gelenData.tente_acilim)
                        viewDialog.etKumasKodu.setText(gelenData.tente_kumaskodu)
                        viewDialog.etSacakTuru.setText(gelenData.tente_sacak)
                        viewDialog.etSacakYazisi.setText(gelenData.tente_sacak_yazisi)
                        viewDialog.etSanziman.setText(gelenData.sanzimanYonu)
                        viewDialog.etProfilRengi.setText(gelenData.profilRengi)
                        viewDialog.etSiparisNotu.setText(itemData.siparis_notu)
                    }
                })


                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Körüklü Tente") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialogKoruklu = View.inflate(myContext, R.layout.activity_koruklu_tente, null)

                uretimRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.KorukluTenteData::class.java)!!
                        viewDialogKoruklu.etCephe.setText(gelenData.tente_cephe)
                        viewDialogKoruklu.etAcilim.setText(gelenData.tente_acilim)
                        viewDialogKoruklu.etKumasKodu.setText(gelenData.tente_kumaskodu)
                        viewDialogKoruklu.etSacakTuru.setText(gelenData.tente_sacak)
                        viewDialogKoruklu.etSacakYazisi.setText(gelenData.tente_sacak_yazisi)
                        viewDialogKoruklu.etİpYonu.setText(gelenData.ipYonu)
                        viewDialogKoruklu.etProfilRengi.setText(gelenData.profilRengi)
                        viewDialogKoruklu.etSiparisNotu.setText(itemData.siparis_notu)
                    }
                })
                builder.setView(viewDialogKoruklu)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
        }

        holder.telNo.setOnClickListener {
            val arama =
                Intent(Intent.ACTION_DIAL)//Bu kod satırımız bizi rehbere telefon numarası ile yönlendiri.
            arama.data = Uri.parse("tel:" + musteriTelNo)
            myContext.startActivity(arama)
        }
    }

    inner class UretimHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var musteriAd = itemView.tvMusteriAdi
        var telNo = itemView.tvMusteriTel
        var siparisTuru = itemView.tvSiparisTuru
        var ureten = itemView.tvUretenAdi
        var uretenZaman = itemView.tvUretenZamani


        fun setData(siparisData: SiparisData) {
            try {
                siparisTuru.text = siparisData.siparis_turu
                if (!siparisData.ureten_zaman.toString().isNullOrEmpty()) {
                    uretenZaman.text = formatData(siparisData.ureten_zaman.toString().toLong())
                }
                musteriBilgileri(siparisData)
            } catch (e: Exception) {

            }


        }

        fun formatData(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("HH:mm dd.MM.yyyy")
            return format.format(date)
        }

        fun musteriBilgileri(siparisData: SiparisData) {
            ref.child("Musteriler").child(siparisData.musteri_key.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.hasChildren()) {
                        musteriAdi = p0.child("musteri_ad_soyad").value.toString()
                        musteriTelNo = p0.child("musteri_tel").value.toString()

                        musteriAd.text = p0.child("musteri_ad_soyad").value.toString()
                        telNo.text = p0.child("musteri_tel").value.toString()

                    }

                }

            })

            ref.child("users").child(siparisData.ureten.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.hasChildren()) {
                        ureten.text = p0.child("user_name").value.toString()
                    }
                }

            })
        }
    }

}