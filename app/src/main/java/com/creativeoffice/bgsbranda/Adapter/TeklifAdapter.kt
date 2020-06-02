package com.creativeoffice.bgsbranda.Adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.getIntent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.creativeoffice.bgsbranda.Activity.SiparislerActivity
import com.creativeoffice.bgsbranda.Activity.TeklifActivity
import com.creativeoffice.bgsbranda.Datalar.SiparisData
import com.creativeoffice.bgsbranda.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.*
import kotlinx.android.synthetic.main.dialog_teklif_ver.view.*
import kotlinx.android.synthetic.main.dialog_tente.view.*
import kotlinx.android.synthetic.main.dialog_tente.view.etAcilim
import kotlinx.android.synthetic.main.dialog_tente.view.etCephe
import kotlinx.android.synthetic.main.dialog_tente.view.etKumasKodu
import kotlinx.android.synthetic.main.dialog_tente.view.etProfilRengi
import kotlinx.android.synthetic.main.dialog_tente.view.etSacakTuru
import kotlinx.android.synthetic.main.dialog_tente.view.etSacakYazisi
import kotlinx.android.synthetic.main.dialog_tente.view.etSiparisNotu
import kotlinx.android.synthetic.main.item_siparis.view.*
import kotlinx.android.synthetic.main.item_siparis.view.tvMusteriAdi
import kotlinx.android.synthetic.main.item_siparis.view.tvMusteriTel
import kotlinx.android.synthetic.main.item_siparis.view.tvSiparisTuru
import kotlinx.android.synthetic.main.item_teklif.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TeklifAdapter(val myContext: Context, val teklifler: ArrayList<SiparisData>, val kullaniciKey:String) : RecyclerView.Adapter<TeklifAdapter.TeklifHolder>() {
    val ref = FirebaseDatabase.getInstance().reference
    val teklifRef = FirebaseDatabase.getInstance().reference.child("Teklifler")

    var musteriAdi: String? = null
    var musteriTelNo: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeklifAdapter.TeklifHolder {
        val myView = LayoutInflater.from(myContext).inflate(R.layout.item_teklif, parent, false)

        return TeklifHolder(myView)
    }

    override fun getItemCount(): Int {
        return teklifler.size
    }

    override fun onBindViewHolder(holder: TeklifAdapter.TeklifHolder, position: Int) {
        val itemData = teklifler[position]
        holder.setData(teklifler[position])

        holder.itemView.setOnLongClickListener {
            val popup = PopupMenu(myContext, holder.itemView)
            popup.inflate(R.menu.popup_menu_teklif)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.popUretim -> {
                        var alert = AlertDialog.Builder(myContext)
                            .setTitle("Üretime Gönder")
                            .setMessage("Emin Misin ?")
                            .setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    ref.child("Uretim").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                        teklifRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {}
                                            override fun onDataChange(p0: DataSnapshot) {
                                                try {
                                                    var tenteData = p0.getValue(SiparisData.TenteData::class.java)!!
                                                    ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        teklifRef.child(itemData.siparis_key.toString()).removeValue()
                                                    }
                                                } catch (e: Exception) {
                                                    Log.e("Tek", "Teklif Adapter URetim hatası")

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
                    R.id.popDüzenleTeklif -> {
                        var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                        var viewDialog = inflate(myContext, R.layout.dialog_teklif_ver, null)
                        viewDialog.etTeklifFiyati.setText(itemData.siparis_teklif.toString())

                        builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.dismiss()
                            }
                        })
                        builder.setPositiveButton("Teklifi Güncelle", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                var fiyat = viewDialog.etTeklifFiyati.text.toString().toInt()
                                teklifRef.child(itemData.siparis_key.toString()).child("siparis_teklif").setValue(fiyat)

                                myContext.startActivity(Intent(myContext, TeklifActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                            }
                        })

                        builder.setView(viewDialog)
                        var dialog: Dialog = builder.create()
                        dialog.show()
                    }
                    R.id.popDüzenle -> {
                        if (itemData.siparis_turu == "Tente") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = inflate(myContext, R.layout.dialog_tente, null)
                            teklifRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
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
                                    teklifRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    teklifRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)

                                    myContext.startActivity(Intent(myContext, TeklifActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

                                }
                            })

                            builder.setView(viewDialog)
                            var dialogSiparisTuru: Dialog = builder.create()
                            dialogSiparisTuru.show()
                        }
                        if (itemData.siparis_turu == "Körüklü Tente") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = inflate(myContext, R.layout.dialog_koruklu_tente, null)

                            teklifRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
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
                                    teklifRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    teklifRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                    myContext.startActivity(Intent(myContext, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

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
                                    ref.child("SilinenTeklifler").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                        teklifRef.child(itemData.siparis_key.toString()).removeValue()
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
                var viewDialog = inflate(myContext, R.layout.dialog_tente, null)

                teklifRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
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


                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Körüklü Tente") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_koruklu_tente, null)

                teklifRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
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
                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
        }

        holder.telNo.setOnClickListener {
            val arama = Intent(Intent.ACTION_DIAL)//Bu kod satırımız bizi rehbere telefon numarası ile yönlendiri.
            arama.data = Uri.parse("tel:" + musteriTelNo)
            myContext.startActivity(arama)
        }
    }

    inner class TeklifHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var musteriAd = itemView.tvMusteriAdi
        var telNo = itemView.tvMusteriTel
        var siparisTuru = itemView.tvSiparisTuru
        var teklifFiyatı = itemView.tvTeklifFiyatı
        var teklifVeren = itemView.tvTeklifVerenAdi
        var teklifVerenZaman = itemView.tvTeklifVerenZaman


        fun setData(siparisData: SiparisData) {
            musteriBilgileri(siparisData)

            siparisTuru.text = siparisData.siparis_turu
            if (siparisData.siparis_teklif.toString().isNotEmpty()) {
                teklifFiyatı.text = siparisData.siparis_teklif.toString() + " tl"
            } else {
                teklifFiyatı.text = "Teklif yok"

            }
            if (!siparisData.teklif_veren_zaman.toString().isNullOrEmpty()) {
                teklifVerenZaman.text = formatData(siparisData.teklif_veren_zaman.toString().toLong())
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

            ref.child("users").child(siparisData.teklif_veren.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.hasChildren()) {
                        teklifVeren.text = p0.child("user_name").value.toString()
                    }
                }

            })
        }
    }

}