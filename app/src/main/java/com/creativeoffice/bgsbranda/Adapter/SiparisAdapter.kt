package com.creativeoffice.bgsbranda.Adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
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
import kotlinx.android.synthetic.main.activity_karpuz_tente.view.*
import kotlinx.android.synthetic.main.activity_koruklu_tente.*
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.*
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.appBarLayoutKoruklu
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.chSeritRengiVarMi
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.etSeritRengiAdeti
import kotlinx.android.synthetic.main.activity_pergole.view.*
import kotlinx.android.synthetic.main.activity_seffaf_tente.view.*
import kotlinx.android.synthetic.main.activity_semsiye.view.*
import kotlinx.android.synthetic.main.activity_tente_mafsalli.view.*
import kotlinx.android.synthetic.main.activity_tente_mafsalli.view.appBarLayoutMafsalli
import kotlinx.android.synthetic.main.activity_tente_mafsalli.view.etAcilim
import kotlinx.android.synthetic.main.activity_tente_mafsalli.view.etCephe
import kotlinx.android.synthetic.main.activity_tente_mafsalli.view.etKumasKodu
import kotlinx.android.synthetic.main.activity_tente_mafsalli.view.etProfilRengi
import kotlinx.android.synthetic.main.activity_tente_mafsalli.view.etSacakYazisi
import kotlinx.android.synthetic.main.activity_tente_mafsalli.view.etSiparisNotu
import kotlinx.android.synthetic.main.dialog_teklif_ver.view.*
import kotlinx.android.synthetic.main.item_siparis.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SiparisAdapter(val myContext: Context, val siparisler: ArrayList<SiparisData>, val kullaniciKey: String) : RecyclerView.Adapter<SiparisAdapter.SiparisHolder>() {
    val ref = FirebaseDatabase.getInstance().reference
    val siparisRef = FirebaseDatabase.getInstance().reference.child("Siparisler")

    var musteriAdi: String? = null
    var musteriTelNo: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiparisAdapter.SiparisHolder {
        val myView = LayoutInflater.from(myContext).inflate(R.layout.item_siparis, parent, false)

        return SiparisHolder(myView)
    }

    override fun getItemCount(): Int {
        return siparisler.size
    }

    override fun onBindViewHolder(holder: SiparisAdapter.SiparisHolder, position: Int) {
        val itemData = siparisler[position]
        holder.setData(siparisler[position])
        holder.itemView.setOnClickListener {

            if (itemData.siparis_turu == "Mafsallı Tente") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_tente_mafsalli, null)
                viewDialog.spSanzimanYonuMafsalli.visibility = View.GONE
                viewDialog.spSacakTuruMafsalli.visibility = View.GONE
                viewDialog.appBarLayoutMafsalli.visibility = View.GONE

                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.MafsallıTente::class.java)!!
                        viewDialog.etCephe.setText(gelenData.cephe)
                        viewDialog.etAcilim.setText(gelenData.acilim)
                        viewDialog.etKumasKodu.setText(gelenData.kumaskodu)
                        viewDialog.etSacakTuruMafsalli.setText(gelenData.sacak_turu.toString())
                        viewDialog.etSacakYazisi.setText(gelenData.sacak_yazisi)
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
                viewDialog.appBarLayoutKoruklu.visibility = View.GONE
                viewDialog.spSacakTuruKoruk.visibility = View.GONE
                viewDialog.chSeritRengiVarMi.visibility = View.GONE
                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.KorukluTenteData::class.java)!!
                        viewDialog.etCephe.setText(gelenData.cephe)
                        viewDialog.etAcilim.setText(gelenData.acilim)
                        viewDialog.etKumasKodu.setText(gelenData.kumaskodu)
                        viewDialog.etSacakTuruKoruklu.setText(gelenData.sacak_turu)
                        viewDialog.etSacakBiyesRengi.setText(gelenData.sacak_biyesi_rengi)
                        viewDialog.etSeritRengiAdeti.setText(gelenData.serit_rengi_adeti)
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
            if (itemData.siparis_turu == "Pergole") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_pergole, null)
                viewDialog.imgPergole.visibility = View.GONE
                viewDialog.appBarLayoutPergole.visibility = View.GONE
                viewDialog.spPergoleTuru.visibility = View.GONE
                viewDialog.spPergoleCesidi.visibility = View.GONE
                viewDialog.spMotorYonu.visibility = View.GONE
                viewDialog.chCamKaydi.visibility = View.GONE
                viewDialog.chCamVarMiVar.visibility = View.GONE

                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.PergoleData::class.java)!!
                        viewDialog.etPergoleTuru.setText(gelenData.pergole_turu)
                        viewDialog.etCephePergole.setText(gelenData.cephe)
                        viewDialog.etArkaYukseklik.setText(gelenData.arka_yukseklik)
                        viewDialog.etOnYukseklik.setText(gelenData.on_yukseklik)
                        viewDialog.etAcilimPergole.setText(gelenData.acilim)
                        viewDialog.etKumasRengi.setText(gelenData.kumas_rengi)
                        viewDialog.etProfilRengiPergole.setText(gelenData.profil_rengi)
                        viewDialog.etLed.setText(gelenData.led)
                        viewDialog.etMotorYonu.setText(gelenData.motor_yonu)
                        viewDialog.etCamKaydi.setText(gelenData.cam_kaydi_olcusu)
                        viewDialog.etCamVarmi.setText(gelenData.etrafinda_cam_varmi)
                        viewDialog.etPergoleCesidi.setText(gelenData.pergole_cesidi)
                        viewDialog.etSiparisNotuPergole.setText(itemData.siparis_notu)
                    }
                })
                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Şemsiye") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_semsiye, null)
             //   viewDialog.imgSemsiye.visibility = View.GONE
                viewDialog.appBarLayoutSemsiye.visibility = View.GONE
                viewDialog.spSemsiyeTuru.visibility = View.GONE


                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.SemsiyeData::class.java)!!
                        viewDialog.etSemsiyeTuru.setText(gelenData.semsiye_turu)
                        viewDialog.etGenislikSemsiye.setText(gelenData.genislik)
                        viewDialog.etKumasRengiSemsiye.setText(gelenData.kumas_rengi)
                        viewDialog.etSacakYazisiSemsiye.setText(gelenData.sacak_yazisi)
                        viewDialog.etSiparisNotuSemsiye.setText(itemData.siparis_notu)
                    }
                })
                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Karpuz Tente") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_karpuz_tente, null)
             //   viewDialog.imgSemsiye.visibility = View.GONE
                viewDialog.appBarLayoutKarpuz.visibility = View.GONE
                viewDialog.spSacakTuruKarpuz.visibility = View.GONE


                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.KarpuzData::class.java)!!
                        viewDialog.etGenislikKarpuz.setText(gelenData.genislik)
                        viewDialog.etYukseklikKarpuz.setText(gelenData.yukseklik)
                        viewDialog.etKumasRengiKarpuz.setText(gelenData.kumas_rengi)
                        viewDialog.etSacakTürüKarpuz.setText(gelenData.sacak_turu)
                        viewDialog.etSacakYazisiKarpuz.setText(gelenData.sacak_yazisi)
                        viewDialog.etBiyeRengiKarpuz.setText(gelenData.biye_rengi)
                        viewDialog.etSeritRengiKarpuz.setText(gelenData.serit_rengi)
                        viewDialog.etSiparisNotuKarpuz.setText(itemData.siparis_notu)
                    }
                })
                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Şeffaf") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_seffaf_tente, null)
             //   viewDialog.imgSemsiye.visibility = View.GONE
                viewDialog.appBarLayoutSeffaf.visibility = View.GONE
                viewDialog.spFermuar.visibility = View.GONE
                viewDialog.spBoruYeri.visibility = View.GONE


                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.SeffafData::class.java)!!
                        viewDialog.etMikaEni.setText(gelenData.seffaf_mika_eni)
                        viewDialog.etPVCRengi.setText(gelenData.pvc_rengi)
                        viewDialog.etAltPvc.setText(gelenData.alt_pvc)
                        viewDialog.etUstPvc.setText(gelenData.ust_pvc)
                        viewDialog.etFermuar.setText(gelenData.fermuar)
                        viewDialog.etBoruYeri.setText(gelenData.boru_yeri)
                        viewDialog.etEksSacak.setText(gelenData.ekstra_sacak)
                        viewDialog.etSiparisNotuSeffaf.setText(itemData.siparis_notu)
                    }
                })
                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
        }
        holder.itemView.setOnLongClickListener {
            val popup = PopupMenu(myContext, holder.itemView)
            popup.inflate(R.menu.popup_menu_siparisler)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.popTeklif -> {
                        var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                        var viewDialog = inflate(myContext, R.layout.dialog_teklif_ver, null)

                        builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.dismiss()
                            }
                        })
                        builder.setPositiveButton("Teklif Ver", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {

                                var fiyat = viewDialog.etTeklifFiyati.text.toString().toInt()
                                siparisRef.child(itemData.siparis_key.toString()).child("siparis_teklif").setValue(fiyat).addOnCompleteListener {
                                    siparisRef.child(itemData.siparis_key.toString()).child("teklif_veren").setValue(kullaniciKey.toString())
                                    siparisRef.child(itemData.siparis_key.toString()).child("teklif_veren_zaman").setValue(ServerValue.TIMESTAMP)
                                    myContext.startActivity(Intent(myContext, TeklifActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

                                    if (itemData.siparis_turu == "Mafsallı Tente") {
                                        siparisRef.child(itemData.siparis_key.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {}
                                            override fun onDataChange(p0: DataSnapshot) {
                                                var data = p0.getValue(SiparisData::class.java)!!
                                                var tenteData = p0.child("tenteData").getValue(SiparisData.MafsallıTente::class.java)!!

                                                ref.child("Teklifler").child(itemData.siparis_key.toString()).setValue(data).addOnCompleteListener {
                                                    ref.child("Teklifler").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData)
                                                    siparisRef.child(itemData.siparis_key.toString()).removeValue()
                                                    Toast.makeText(myContext, "Teklif Girildi", Toast.LENGTH_LONG).show()


                                                }

                                            }
                                        })
                                    }
                                    if (itemData.siparis_turu == "Körüklü Tente") {
                                        siparisRef.child(itemData.siparis_key.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {}
                                            override fun onDataChange(p0: DataSnapshot) {
                                                var data = p0.getValue(SiparisData::class.java)!!
                                                var tenteData = p0.child("tenteData").getValue(SiparisData.KorukluTenteData::class.java)!!

                                                ref.child("Teklifler").child(itemData.siparis_key.toString()).setValue(data).addOnCompleteListener {
                                                    ref.child("Teklifler").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData)
                                                    siparisRef.child(itemData.siparis_key.toString()).removeValue()
                                                    Toast.makeText(myContext, "Teklif Girildi", Toast.LENGTH_LONG).show()


                                                }

                                            }
                                        })
                                    }
                                    if (itemData.siparis_turu == "Pergole") {
                                        siparisRef.child(itemData.siparis_key.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {}
                                            override fun onDataChange(p0: DataSnapshot) {
                                                var data = p0.getValue(SiparisData::class.java)!!
                                                var tenteData = p0.child("tenteData").getValue(SiparisData.PergoleData::class.java)!!

                                                ref.child("Teklifler").child(itemData.siparis_key.toString()).setValue(data).addOnCompleteListener {
                                                    ref.child("Teklifler").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData)
                                                    siparisRef.child(itemData.siparis_key.toString()).removeValue()
                                                    Toast.makeText(myContext, "Teklif Girildi", Toast.LENGTH_LONG).show()


                                                }

                                            }
                                        })
                                    }
                                    if (itemData.siparis_turu == "Şemsiye") {
                                        siparisRef.child(itemData.siparis_key.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {}
                                            override fun onDataChange(p0: DataSnapshot) {
                                                var data = p0.getValue(SiparisData::class.java)!!
                                                var tenteData = p0.child("tenteData").getValue(SiparisData.SemsiyeData::class.java)!!

                                                ref.child("Teklifler").child(itemData.siparis_key.toString()).setValue(data).addOnCompleteListener {
                                                    ref.child("Teklifler").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData)
                                                    siparisRef.child(itemData.siparis_key.toString()).removeValue()
                                                    Toast.makeText(myContext, "Teklif Girildi", Toast.LENGTH_LONG).show()


                                                }

                                            }
                                        })
                                    }
                                    if (itemData.siparis_turu == "Karpuz Tente") {
                                        siparisRef.child(itemData.siparis_key.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {}
                                            override fun onDataChange(p0: DataSnapshot) {
                                                var data = p0.getValue(SiparisData::class.java)!!
                                                var tenteData = p0.child("tenteData").getValue(SiparisData.KarpuzData::class.java)!!

                                                ref.child("Teklifler").child(itemData.siparis_key.toString()).setValue(data).addOnCompleteListener {
                                                    ref.child("Teklifler").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData)
                                                    siparisRef.child(itemData.siparis_key.toString()).removeValue()
                                                    Toast.makeText(myContext, "Teklif Girildi", Toast.LENGTH_LONG).show()


                                                }

                                            }
                                        })
                                    }
                                    if (itemData.siparis_turu == "Şeffaf") {
                                        siparisRef.child(itemData.siparis_key.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {}
                                            override fun onDataChange(p0: DataSnapshot) {
                                                var data = p0.getValue(SiparisData::class.java)!!
                                                var tenteData = p0.child("tenteData").getValue(SiparisData.SeffafData::class.java)!!

                                                ref.child("Teklifler").child(itemData.siparis_key.toString()).setValue(data).addOnCompleteListener {
                                                    ref.child("Teklifler").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData)
                                                    siparisRef.child(itemData.siparis_key.toString()).removeValue()
                                                    Toast.makeText(myContext, "Teklif Girildi", Toast.LENGTH_LONG).show()


                                                }

                                            }
                                        })
                                    }



                                }

                            }
                        })

                        builder.setView(viewDialog)
                        var dialogSiparisTuru: Dialog = builder.create()
                        dialogSiparisTuru.show()
                    }
                    R.id.popDüzenle -> {
                        if (itemData.siparis_turu == "Mafsallı Tente") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = inflate(myContext, R.layout.activity_tente_mafsalli, null)
                            viewDialog.spSacakTuruMafsalli.visibility = View.GONE
                            viewDialog.spSanzimanYonuMafsalli.visibility = View.GONE
                            viewDialog.appBarLayoutMafsalli.visibility = View.GONE
                            siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    var gelenData = p0.getValue(SiparisData.MafsallıTente::class.java)!!

                                    viewDialog.etCephe.setText(gelenData.cephe)
                                    viewDialog.etAcilim.setText(gelenData.acilim)
                                    viewDialog.etKumasKodu.setText(gelenData.kumaskodu)
                                    viewDialog.etSacakTuruMafsalli.setText(gelenData.sacak_turu)
                                    viewDialog.etSacakYazisi.setText(gelenData.sacak_yazisi)
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
                                    var sacakTuru = viewDialog.etSacakTuruMafsalli.text.toString()
                                    var sacakYazisi = viewDialog.etSacakYazisi.text.toString()
                                    var sanziman = viewDialog.etSanziman.text.toString()
                                    var profilRengi = viewDialog.etProfilRengi.text.toString()
                                    var siparisNot = viewDialog.etSiparisNotu.text.toString()
                                    var guncelData = SiparisData.MafsallıTente(cephe, acilim, kumasKodu, sacakTuru, sacakYazisi, sanziman, profilRengi, itemData.siparis_key)
                                    siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                    myContext.startActivity(Intent(myContext, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

                                }
                            })

                            builder.setView(viewDialog)
                            var dialogSiparisTuru: Dialog = builder.create()
                            dialogSiparisTuru.show()
                        }
                        if (itemData.siparis_turu == "Körüklü Tente") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = inflate(myContext, R.layout.activity_koruklu_tente, null)
                            viewDialog.appBarLayoutKoruklu.visibility = View.GONE
                            viewDialog.spSacakTuruKoruk.visibility = View.GONE
                            viewDialog.appBarLayoutKoruklu.visibility = View.GONE
                            viewDialog.etSeritRengiAdeti.visibility = View.VISIBLE
                            viewDialog.chSeritRengiVarMi.visibility = View.GONE

                            siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    var gelenData = p0.getValue(SiparisData.KorukluTenteData::class.java)!!
                                    viewDialog.etCephe.setText(gelenData.cephe)
                                    viewDialog.etAcilim.setText(gelenData.acilim)
                                    viewDialog.etKumasKodu.setText(gelenData.kumaskodu)
                                    viewDialog.etSacakTuruKoruklu.setText(gelenData.sacak_turu)
                                    viewDialog.etSacakBiyesRengi.setText(gelenData.sacak_biyesi_rengi)
                                    viewDialog.etSeritRengiAdeti.setText(gelenData.serit_rengi_adeti)
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
                                    var sacakTuru = viewDialog.etSacakTuruKoruklu.text.toString()
                                    var sacakBiyesiRengi = viewDialog.etSacakBiyesRengi.text.toString()
                                    var seritRengiAdeti = viewDialog.etSeritRengiAdeti.text.toString()
                                    var sacakYazisi = viewDialog.etSacakYazisi.text.toString()
                                    var ipyonu = viewDialog.etİpYonu.text.toString()
                                    var profilRengi = viewDialog.etProfilRengi.text.toString()
                                    var siparisNot = viewDialog.etSiparisNotu.text.toString()
                                    var guncelData = SiparisData.KorukluTenteData(cephe, acilim, kumasKodu, sacakTuru, sacakBiyesiRengi, seritRengiAdeti, sacakYazisi, ipyonu, profilRengi, itemData.siparis_key.toString())
                                    siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                    myContext.startActivity(Intent(myContext, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                                }
                            })


                            builder.setView(viewDialog)
                            var dialogSiparisTuru: Dialog = builder.create()
                            dialogSiparisTuru.show()
                        }
                        if (itemData.siparis_turu == "Pergole") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = inflate(myContext, R.layout.activity_pergole, null)
                            viewDialog.imgPergole.visibility = View.GONE
                            viewDialog.appBarLayoutPergole.visibility = View.GONE
                            viewDialog.spPergoleTuru.visibility = View.GONE
                            viewDialog.spPergoleCesidi.visibility = View.GONE
                            viewDialog.spMotorYonu.visibility = View.GONE
                            viewDialog.chCamKaydi.visibility = View.GONE
                            viewDialog.chCamVarMiVar.visibility = View.GONE


                            siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    var gelenData = p0.getValue(SiparisData.PergoleData::class.java)!!
                                    viewDialog.etPergoleTuru.setText(gelenData.pergole_turu)
                                    viewDialog.etCephePergole.setText(gelenData.cephe)
                                    viewDialog.etArkaYukseklik.setText(gelenData.arka_yukseklik)
                                    viewDialog.etOnYukseklik.setText(gelenData.on_yukseklik)
                                    viewDialog.etAcilimPergole.setText(gelenData.acilim)
                                    viewDialog.etKumasRengi.setText(gelenData.kumas_rengi)
                                    viewDialog.etProfilRengiPergole.setText(gelenData.profil_rengi)
                                    viewDialog.etLed.setText(gelenData.led)
                                    viewDialog.etMotorYonu.setText(gelenData.motor_yonu)
                                    viewDialog.etCamKaydi.setText(gelenData.cam_kaydi_olcusu)
                                    viewDialog.etCamVarmi.setText(gelenData.etrafinda_cam_varmi)
                                    viewDialog.etPergoleCesidi.setText(gelenData.pergole_cesidi)
                                    viewDialog.etSiparisNotuPergole.setText(itemData.siparis_notu)
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
                                    var pergoleTuru = viewDialog.etPergoleTuru.text.toString()
                                    var cephe = viewDialog.etCephePergole.text.toString()
                                    var arka = viewDialog.etArkaYukseklik.text.toString()
                                    var on = viewDialog.etOnYukseklik.text.toString()
                                    var acilim = viewDialog.etAcilimPergole.text.toString()
                                    var kumasRengi = viewDialog.etKumasRengi.text.toString()
                                    var profilRengi = viewDialog.etProfilRengiPergole.text.toString()
                                    var led = viewDialog.etLed.text.toString()
                                    var motorYonu = viewDialog.etMotorYonu.text.toString()
                                    var camKaydiOlcusu = viewDialog.etCamKaydi.text.toString()
                                    var pergoleCesidi = viewDialog.etPergoleCesidi.text.toString()
                                    var etrafindaCamVar = viewDialog.etCamVarmi.text.toString()


                                    var siparisNot = viewDialog.etSiparisNotuPergole.text.toString()
                                    var guncelData = SiparisData.PergoleData(pergoleTuru, cephe, acilim, arka, on, kumasRengi, profilRengi,
                                        led, motorYonu, camKaydiOlcusu, pergoleCesidi, etrafindaCamVar, itemData.siparis_key.toString())
                                    siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                    myContext.startActivity(Intent(myContext, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

                                }

                            })
                            builder.setView(viewDialog)
                            var dialogSiparisTuru: Dialog = builder.create()
                            dialogSiparisTuru.show()
                        }
                        if (itemData.siparis_turu == "Şemsiye") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = inflate(myContext, R.layout.activity_semsiye, null)
                            //   viewDialog.imgSemsiye.visibility = View.GONE
                            viewDialog.appBarLayoutSemsiye.visibility = View.GONE
                            viewDialog.spSemsiyeTuru.visibility = View.GONE


                            siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    var gelenData = p0.getValue(SiparisData.SemsiyeData::class.java)!!
                                    viewDialog.etSemsiyeTuru.setText(gelenData.semsiye_turu)
                                    viewDialog.etGenislikSemsiye.setText(gelenData.genislik)
                                    viewDialog.etKumasRengiSemsiye.setText(gelenData.kumas_rengi)
                                    viewDialog.etSacakYazisiSemsiye.setText(gelenData.sacak_yazisi)
                                    viewDialog.etSiparisNotuSemsiye.setText(itemData.siparis_notu)
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
                                    var semsiyeTuru = viewDialog.etSemsiyeTuru.text.toString()
                                    var genislik = viewDialog.etGenislikSemsiye.text.toString()
                                    var kumasRengi = viewDialog.etKumasRengiSemsiye.text.toString()
                                    var sacakYazisi = viewDialog.etSacakYazisiSemsiye.text.toString()
                                    var siparisNot = viewDialog.etSiparisNotuSemsiye.text.toString()

                                    var guncelData = SiparisData.SemsiyeData(semsiyeTuru,genislik,kumasRengi,sacakYazisi,itemData.siparis_key.toString())
                                    siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                    myContext.startActivity(Intent(myContext, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

                                }

                            })


                            builder.setView(viewDialog)
                            var dialogSiparisTuru: Dialog = builder.create()
                            dialogSiparisTuru.show()
                        }
                        if (itemData.siparis_turu == "Karpuz Tente") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = inflate(myContext, R.layout.activity_karpuz_tente, null)
                            //   viewDialog.imgSemsiye.visibility = View.GONE
                            viewDialog.appBarLayoutKarpuz.visibility = View.GONE
                            viewDialog.spSacakTuruKarpuz.visibility = View.GONE


                            siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    var gelenData = p0.getValue(SiparisData.KarpuzData::class.java)!!
                                    viewDialog.etGenislikKarpuz.setText(gelenData.genislik)
                                    viewDialog.etYukseklikKarpuz.setText(gelenData.yukseklik)
                                    viewDialog.etKumasRengiKarpuz.setText(gelenData.kumas_rengi)
                                    viewDialog.etSacakTürüKarpuz.setText(gelenData.sacak_turu)
                                    viewDialog.etSacakYazisiKarpuz.setText(gelenData.sacak_yazisi)
                                    viewDialog.etBiyeRengiKarpuz.setText(gelenData.biye_rengi)
                                    viewDialog.etSeritRengiKarpuz.setText(gelenData.serit_rengi)
                                    viewDialog.etSiparisNotuKarpuz.setText(itemData.siparis_notu)
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


                                    var genislik = viewDialog.etGenislikKarpuz.text.toString()
                                    var yukseklik = viewDialog.etYukseklikKarpuz.text.toString()
                                    var kumasRengi = viewDialog.etKumasRengiKarpuz.text.toString()
                                    var sacakTuru = viewDialog.etSacakTürüKarpuz.text.toString()
                                    var sacakBiyesiRengi = viewDialog.etBiyeRengiKarpuz.text.toString()
                                    var seritRengi = viewDialog.etSeritRengiKarpuz.text.toString()
                                    var sacakYazisi = viewDialog.etSacakYazisiKarpuz.text.toString()
                                    var siparisNot = viewDialog.etSiparisNotuKarpuz.text.toString()

                                    var guncelData = SiparisData.KarpuzData(genislik,yukseklik,kumasRengi,sacakTuru,sacakYazisi,sacakBiyesiRengi,seritRengi, itemData.siparis_key.toString())
                                    siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                    myContext.startActivity(Intent(myContext, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                                }
                            })

                            builder.setView(viewDialog)
                            var dialogSiparisTuru: Dialog = builder.create()
                            dialogSiparisTuru.show()
                        }
                        if (itemData.siparis_turu == "Şeffaf") {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                            var viewDialog = inflate(myContext, R.layout.activity_seffaf_tente, null)
                            //   viewDialog.imgSemsiye.visibility = View.GONE
                            viewDialog.appBarLayoutSeffaf.visibility = View.GONE
                            viewDialog.spFermuar.visibility = View.GONE
                            viewDialog.spBoruYeri.visibility = View.GONE


                            siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    var gelenData = p0.getValue(SiparisData.SeffafData::class.java)!!
                                    viewDialog.etMikaEni.setText(gelenData.seffaf_mika_eni)
                                    viewDialog.etPVCRengi.setText(gelenData.pvc_rengi)
                                    viewDialog.etAltPvc.setText(gelenData.alt_pvc)
                                    viewDialog.etUstPvc.setText(gelenData.ust_pvc)
                                    viewDialog.etFermuar.setText(gelenData.fermuar)
                                    viewDialog.etBoruYeri.setText(gelenData.boru_yeri)
                                    viewDialog.etEksSacak.setText(gelenData.ekstra_sacak)
                                    viewDialog.etSiparisNotuSeffaf.setText(itemData.siparis_notu)
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
                                    var mika = viewDialog.etMikaEni.text.toString()
                                    var pvcRengi = viewDialog.etPVCRengi.text.toString()
                                    var altPvc = viewDialog.etAltPvc.text.toString()
                                    var ustPvc = viewDialog.etUstPvc.text.toString()
                                    var fermuar = viewDialog.etFermuar.text.toString()
                                    var boruyeri = viewDialog.etBoruYeri.text.toString()
                                    var eksSacak = viewDialog.etEksSacak.text.toString()

                                    var siparisNot = viewDialog.etSiparisNotuSeffaf.text.toString()

                                    var guncelData = SiparisData.SeffafData(mika,pvcRengi,altPvc,ustPvc,fermuar,boruyeri,eksSacak, itemData.siparis_key.toString())

                                    siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                    siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
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
                                    ref.child("SilinenSiparisler").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                        siparisRef.child(itemData.siparis_key.toString()).removeValue()
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



        holder.telNo.setOnClickListener {
            val arama =
                Intent(Intent.ACTION_DIAL)//Bu kod satırımız bizi rehbere telefon numarası ile yönlendiri.
            arama.data = Uri.parse("tel:" + musteriTelNo)
            myContext.startActivity(arama)
        }
    }

    inner class SiparisHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var musteriAd = itemView.tvMusteriAdi
        var telNo = itemView.tvMusteriTel
        var siparisTuru = itemView.tvSiparisTuru
        var siparisGiren = itemView.tvSiparisGirenAdi
        var siparisGirmeZamani = itemView.tvSiparisGirmeZamani


        fun setData(siparisData: SiparisData) {
            try {
                siparisTuru.text = siparisData.siparis_turu
                if (!siparisData.siparis_girme_zamani.toString().isNullOrEmpty()) {
                    siparisGirmeZamani.text = formatData(siparisData.siparis_girme_zamani.toString().toLong())
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

            ref.child("users").child(siparisData.siparis_giren.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.hasChildren()) {
                        siparisGiren.text = p0.child("user_name").value.toString()
                    }
                }

            })
        }
    }

}