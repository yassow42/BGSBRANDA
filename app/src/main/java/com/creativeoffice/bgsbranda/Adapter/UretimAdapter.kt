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
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.creativeoffice.bgsbranda.Activity.MontajActivity
import com.creativeoffice.bgsbranda.Activity.SiparislerActivity
import com.creativeoffice.bgsbranda.Activity.TeklifActivity
import com.creativeoffice.bgsbranda.Activity.UretimActivity

import com.creativeoffice.bgsbranda.Datalar.SiparisData
import com.creativeoffice.bgsbranda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_diger.view.*
import kotlinx.android.synthetic.main.activity_karpuz_tente.view.*
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.*
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.etAcilim
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.etCephe
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.etKumasKodu
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.etProfilRengi
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.etSacakYazisi
import kotlinx.android.synthetic.main.activity_koruklu_tente.view.etSiparisNotu
import kotlinx.android.synthetic.main.activity_pergole.view.*
import kotlinx.android.synthetic.main.activity_seffaf_tente.view.*
import kotlinx.android.synthetic.main.activity_semsiye.view.*
import kotlinx.android.synthetic.main.activity_tamir.view.*
import kotlinx.android.synthetic.main.activity_tente_mafsalli.view.*
import kotlinx.android.synthetic.main.activity_wintent.view.*
import kotlinx.android.synthetic.main.dialog_photo.view.*
import kotlinx.android.synthetic.main.dialog_teklif_ver.view.*

import kotlinx.android.synthetic.main.item_siparis.view.tvMusteriAdi
import kotlinx.android.synthetic.main.item_siparis.view.tvMusteriTel
import kotlinx.android.synthetic.main.item_siparis.view.tvSiparisTuru
import kotlinx.android.synthetic.main.item_uretim.view.*
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UretimAdapter(val myContext: Context, val uretimler: ArrayList<SiparisData>, val kullaniciKey: String) : RecyclerView.Adapter<UretimAdapter.UretimHolder>() {
    val ref = FirebaseDatabase.getInstance().reference
    val uretimRef = FirebaseDatabase.getInstance().reference.child("Uretim")
    val siparisRef = FirebaseDatabase.getInstance().reference.child("Uretim")

    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    var yetki = ""

    var musteriAdi: String? = null


    var size = 450

    init {
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid

        ref.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                yetki = p0.child("yetki").value.toString()
            }

        })
    }

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

        holder.itemView.setOnClickListener {
            if (itemData.siparis_turu == "Mafsallı Tente") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_tente_mafsalli, null)
                viewDialog.spSanzimanYonuMafsalli.visibility = View.GONE
                viewDialog.spSacakTuruMafsalli.visibility = View.GONE
                viewDialog.spMotorMafsalli.visibility = View.GONE
                viewDialog.spAyakYonuMafsalli.visibility = View.GONE
                viewDialog.spMantolamaMafsalli.visibility = View.GONE
                viewDialog.appBarLayoutMafsalli.visibility = View.GONE

                viewDialog.foto1Mafsalli.visibility = View.GONE
                viewDialog.foto2Mafsalli.visibility = View.GONE
                viewDialog.foto3Mafsalli.visibility = View.GONE
                viewDialog.foto4Mafsalli.visibility = View.GONE

                viewDialog.foto1Mafsalli.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto1).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto2Mafsalli.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto2).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto3Mafsalli.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto3).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto4Mafsalli.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto4).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }


                if (!itemData.foto1.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto1).resize(size, size).into(viewDialog.foto1Mafsalli)
                    viewDialog.foto1Mafsalli.visibility = View.VISIBLE
                }
                if (!itemData.foto2.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto2).resize(size, size).into(viewDialog.foto2Mafsalli)
                    viewDialog.foto2Mafsalli.visibility = View.VISIBLE
                }

                if (!itemData.foto3.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto3).resize(size, size).into(viewDialog.foto3Mafsalli)
                    viewDialog.foto3Mafsalli.visibility = View.VISIBLE
                }

                if (!itemData.foto4.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto4).resize(size, size).into(viewDialog.foto4Mafsalli)
                    viewDialog.foto4Mafsalli.visibility = View.VISIBLE
                }
                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.MafsallıTente::class.java)!!
                        viewDialog.etCephe.setText(gelenData.cephe)
                        viewDialog.etAcilim.setText(gelenData.acilim)
                        viewDialog.etKumasKodu.setText(gelenData.kumaskodu)
                        viewDialog.etSacakTuruMafsalli.setText(gelenData.sacak_turu)
                        viewDialog.etSacakYazisi.setText(gelenData.sacak_yazisi)
                        viewDialog.etMotor.setText(gelenData.motor)
                        viewDialog.etSanziman.setText(gelenData.sanzimanYonu)
                        viewDialog.etAyakTuruMafsalli.setText(gelenData.ayakTuru)
                        viewDialog.etMantolamaMafsalli.setText(gelenData.mantolama)
                        viewDialog.etProfilRengi.setText(gelenData.profilRengi)
                        viewDialog.etSiparisNotu.setText(itemData.siparis_notu)
                        viewDialog.etEksiklerMafsalli.setText(gelenData.eksikler)
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
                viewDialog.spIpYonu.visibility = View.GONE
                viewDialog.spAyakYonu.visibility = View.GONE
                viewDialog.appBarLayoutKoruklu.visibility = View.GONE
                viewDialog.etSeritRengiAdeti.visibility = View.VISIBLE
                viewDialog.chSeritRengiVarMi.visibility = View.GONE

                viewDialog.foto1Koruklu.visibility = View.GONE
                viewDialog.foto2Koruklu.visibility = View.GONE
                viewDialog.foto3Koruklu.visibility = View.GONE
                viewDialog.foto4Koruklu.visibility = View.GONE


                viewDialog.foto1Koruklu.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto1).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto2Koruklu.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto2).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto3Koruklu.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto3).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto4Koruklu.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto4).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }


                if (!itemData.foto1.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto1).resize(size, size).into(viewDialog.foto1Koruklu)
                    viewDialog.foto1Koruklu.visibility = View.VISIBLE
                }
                if (!itemData.foto2.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto2).resize(size, size).into(viewDialog.foto2Koruklu)
                    viewDialog.foto2Koruklu.visibility = View.VISIBLE
                }

                if (!itemData.foto3.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto3).resize(size, size).into(viewDialog.foto3Koruklu)
                    viewDialog.foto3Koruklu.visibility = View.VISIBLE
                }

                if (!itemData.foto4.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto4).resize(size, size).into(viewDialog.foto4Koruklu)
                    viewDialog.foto4Koruklu.visibility = View.VISIBLE
                }
                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.KorukluTenteData::class.java)!!
                        viewDialog.etCephe.setText(gelenData.cephe)
                        viewDialog.etAcilim.setText(gelenData.acilim)
                        viewDialog.etKumasKodu.setText(gelenData.kumaskodu)
                        viewDialog.etSacakTuruKoruklu.setText(gelenData.sacak_turu)
                        viewDialog.etSacakBiyesRengi.setText(gelenData.sacak_biyesi_rengi)
                        viewDialog.etSeritRengi.setText(gelenData.serit_rengi)
                        viewDialog.etSeritRengiAdeti.setText(gelenData.serit_rengi_adeti)
                        viewDialog.etSacakYazisi.setText(gelenData.tente_sacak_yazisi)
                        viewDialog.etIpYonu.setText(gelenData.ipYonu)
                        viewDialog.etAyakTuru.setText(gelenData.ayakTuru)
                        viewDialog.etProfilRengi.setText(gelenData.profilRengi)
                        viewDialog.etSiparisNotu.setText(itemData.siparis_notu)
                        viewDialog.etEksiklerKoruklu.setText(gelenData.eksikler)

                    }
                })

                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Pergole") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_pergole, null)
                //  viewDialog.imgPergole.visibility = View.GONE
                viewDialog.appBarLayoutPergole.visibility = View.GONE
                viewDialog.spPergoleTuru.visibility = View.GONE
                viewDialog.spPergoleCesidi.visibility = View.GONE
                viewDialog.spMotorYonu.visibility = View.GONE
                viewDialog.chCamKaydi.visibility = View.GONE
                viewDialog.chKornerDirek.visibility = View.GONE
                viewDialog.chCamVarMiVar.visibility = View.GONE

                viewDialog.foto1Pergole.visibility = View.GONE
                viewDialog.foto2Pergole.visibility = View.GONE
                viewDialog.foto3Pergole.visibility = View.GONE
                viewDialog.foto4Pergole.visibility = View.GONE

                viewDialog.foto1Pergole.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto1).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto2Pergole.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto2).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto3Pergole.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto3).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto4Pergole.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto4).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }


                if (!itemData.foto1.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto1).resize(size, size).into(viewDialog.foto1Pergole)
                    viewDialog.foto1Pergole.visibility = View.VISIBLE
                }
                if (!itemData.foto2.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto2).resize(size, size).into(viewDialog.foto2Pergole)
                    viewDialog.foto2Pergole.visibility = View.VISIBLE
                }

                if (!itemData.foto3.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto3).resize(size, size).into(viewDialog.foto3Pergole)
                    viewDialog.foto3Pergole.visibility = View.VISIBLE
                }

                if (!itemData.foto4.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto4).resize(size, size).into(viewDialog.foto4Pergole)
                    viewDialog.foto4Pergole.visibility = View.VISIBLE
                }
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
                        viewDialog.etKornerDirekOlcu.setText(gelenData.korner_direk_olcusu)
                        viewDialog.etKornerDirekAdet.setText(gelenData.korner_direk_adeti)
                        viewDialog.etCamKaydi.setText(gelenData.cam_kaydi_olcusu)
                        viewDialog.etCamKaydiAdet.setText(gelenData.cam_kaydi_adeti)
                        viewDialog.etCamVarmi.setText(gelenData.etrafinda_cam_varmi)
                        viewDialog.etPergoleCesidi.setText(gelenData.pergole_cesidi)
                        viewDialog.etSiparisNotuPergole.setText(itemData.siparis_notu)
                        viewDialog.etEksiklerPergole.setText(gelenData.eksikler)

                        if (gelenData.korner_direk_adeti.isNullOrEmpty()) {
                            viewDialog.etKornerDirekAdet.visibility = View.GONE
                        }
                        if (gelenData.korner_direk_olcusu.isNullOrEmpty()) {
                            viewDialog.etKornerDirekOlcu.visibility = View.GONE
                        }

                        if (gelenData.cam_kaydi_adeti.isNullOrEmpty()) {
                            viewDialog.etCamKaydiAdet.visibility = View.GONE
                        }
                        if (gelenData.cam_kaydi_olcusu.isNullOrEmpty()) {
                            viewDialog.etCamKaydi.visibility = View.GONE
                        }
                    }
                })
                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Şemsiye") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_semsiye, null)
                viewDialog.appBarLayoutSemsiye.visibility = View.GONE
                viewDialog.spSemsiyeTuru.visibility = View.GONE

                viewDialog.foto1Semsiye.visibility = View.GONE
                viewDialog.foto2Semsiye.visibility = View.GONE
                viewDialog.foto3Semsiye.visibility = View.GONE
                viewDialog.foto4Semsiye.visibility = View.GONE

                viewDialog.foto1Semsiye.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto1).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto2Semsiye.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto2).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto3Semsiye.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto3).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto4Semsiye.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto4).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }

                if (!itemData.foto1.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto1).resize(size, size).into(viewDialog.foto1Semsiye)
                    viewDialog.foto1Semsiye.visibility = View.VISIBLE
                }
                if (!itemData.foto2.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto2).resize(size, size).into(viewDialog.foto2Semsiye)
                    viewDialog.foto2Semsiye.visibility = View.VISIBLE
                }

                if (!itemData.foto3.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto3).resize(size, size).into(viewDialog.foto3Semsiye)
                    viewDialog.foto3Semsiye.visibility = View.VISIBLE
                }

                if (!itemData.foto4.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto4).resize(size, size).into(viewDialog.foto4Semsiye)
                    viewDialog.foto4Semsiye.visibility = View.VISIBLE
                }

                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.SemsiyeData::class.java)!!
                        viewDialog.etSemsiyeTuru.setText(gelenData.semsiye_turu)
                        viewDialog.etGenislikSemsiye.setText(gelenData.genislik)
                        viewDialog.etKumasRengiSemsiye.setText(gelenData.kumas_rengi)
                        viewDialog.etSacakYazisiSemsiye.setText(gelenData.sacak_yazisi)
                        viewDialog.etSiparisNotuSemsiye.setText(itemData.siparis_notu)
                        viewDialog.etEksiklerSemsiye.setText(gelenData.eksikler)
                    }
                })
                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Karpuz Tente") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_karpuz_tente, null)
                viewDialog.appBarLayoutKarpuz.visibility = View.GONE
                viewDialog.spSacakTuruKarpuz.visibility = View.GONE

                viewDialog.foto1Karpuz.visibility = View.GONE
                viewDialog.foto2Karpuz.visibility = View.GONE
                viewDialog.foto3Karpuz.visibility = View.GONE
                viewDialog.foto4Karpuz.visibility = View.GONE

                viewDialog.foto1Karpuz.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto1).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto2Karpuz.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto2).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto3Karpuz.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto3).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto4Karpuz.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto4).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }


                if (!itemData.foto1.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto1).resize(size, size).into(viewDialog.foto1Karpuz)
                    viewDialog.foto1Karpuz.visibility = View.VISIBLE
                }
                if (!itemData.foto2.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto2).resize(size, size).into(viewDialog.foto2Karpuz)
                    viewDialog.foto2Karpuz.visibility = View.VISIBLE
                }

                if (!itemData.foto3.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto3).resize(size, size).into(viewDialog.foto3Karpuz)
                    viewDialog.foto3Karpuz.visibility = View.VISIBLE
                }

                if (!itemData.foto4.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto4).resize(size, size).into(viewDialog.foto4Karpuz)
                    viewDialog.foto4Karpuz.visibility = View.VISIBLE
                }

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
                        viewDialog.etEksiklerKarpuz.setText(gelenData.eksikler)
                    }
                })
                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Şeffaf") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_seffaf_tente, null)

                viewDialog.appBarLayoutSeffaf.visibility = View.GONE
                viewDialog.spFermuar.visibility = View.GONE
                viewDialog.spBoruYeri.visibility = View.GONE

                viewDialog.foto1Seffaf.visibility = View.GONE
                viewDialog.foto2Seffaf.visibility = View.GONE
                viewDialog.foto3Seffaf.visibility = View.GONE
                viewDialog.foto4Seffaf.visibility = View.GONE

                viewDialog.foto1Seffaf.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto1).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto2Seffaf.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto2).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto3Seffaf.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto3).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto4Seffaf.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto4).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }

                if (!itemData.foto1.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto1).resize(size, size).into(viewDialog.foto1Seffaf)
                    viewDialog.foto1Seffaf.visibility = View.VISIBLE
                }
                if (!itemData.foto2.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto2).resize(size, size).into(viewDialog.foto2Seffaf)
                    viewDialog.foto2Seffaf.visibility = View.VISIBLE
                }

                if (!itemData.foto3.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto3).resize(size, size).into(viewDialog.foto3Seffaf)
                    viewDialog.foto3Seffaf.visibility = View.VISIBLE
                }

                if (!itemData.foto4.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto4).resize(size, size).into(viewDialog.foto4Seffaf)
                    viewDialog.foto4Seffaf.visibility = View.VISIBLE
                }

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
                        viewDialog.etEksiklerSeffaf.setText(gelenData.eksikler)
                    }
                })
                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Wintend") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_wintent, null)
                viewDialog.spSanzimanYonuWintend.visibility = View.GONE
                viewDialog.spSacakTuruWintend.visibility = View.GONE
                viewDialog.spMotorWintend.visibility = View.GONE
                viewDialog.spAyakTuruWintend.visibility = View.GONE
                viewDialog.spMantolamaWintend.visibility = View.GONE
                viewDialog.appBarLayoutWintend.visibility = View.GONE

                viewDialog.foto1Wintent.visibility = View.GONE
                viewDialog.foto2Wintent.visibility = View.GONE
                viewDialog.foto3Wintent.visibility = View.GONE
                viewDialog.foto4Wintent.visibility = View.GONE


                viewDialog.foto1Wintent.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto1).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto2Wintent.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto2).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto3Wintent.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto3).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto4Wintent.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto4).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }


                if (!itemData.foto1.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto1).resize(size, size).into(viewDialog.foto1Wintent)
                    viewDialog.foto1Wintent.visibility = View.VISIBLE
                }
                if (!itemData.foto2.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto2).resize(size, size).into(viewDialog.foto2Wintent)
                    viewDialog.foto2Wintent.visibility = View.VISIBLE
                }

                if (!itemData.foto3.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto3).resize(size, size).into(viewDialog.foto3Wintent)
                    viewDialog.foto3Wintent.visibility = View.VISIBLE
                }

                if (!itemData.foto4.isNullOrEmpty()) {
                    Picasso.get().load(itemData.foto4).resize(size, size).into(viewDialog.foto4Wintent)
                    viewDialog.foto4Wintent.visibility = View.VISIBLE
                }
                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.Wintend::class.java)!!
                        viewDialog.etCepheWintend.setText(gelenData.cephe)
                        viewDialog.etKolBoyuWintend.setText(gelenData.kolboyu)
                        viewDialog.etKumasKoduWintend.setText(gelenData.kumaskodu)
                        viewDialog.etSacakTuruWintend.setText(gelenData.sacak_turu)
                        viewDialog.etSacakYazisiWintend.setText(gelenData.sacak_yazisi)
                        viewDialog.etMotorWintend.setText(gelenData.motor)
                        viewDialog.etSanzimanWintend.setText(gelenData.sanzimanYonu)
                        viewDialog.etAyakTuruWintend.setText(gelenData.ayakTuru)
                        viewDialog.etMantolamaWintend.setText(gelenData.mantolama)
                        viewDialog.etProfilRengiWintend.setText(gelenData.profilRengi)
                        viewDialog.etSiparisNotuWintend.setText(itemData.siparis_notu)
                        viewDialog.etEksiklerWintent.setText(gelenData.eksikler)
                    }
                })

                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()
            }
            if (itemData.siparis_turu == "Diğer") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_diger, null)
                viewDialog.appBarLayoutDiger.visibility = View.GONE
                viewDialog.foto1.visibility = View.GONE
                viewDialog.foto2.visibility = View.GONE
                viewDialog.foto3.visibility = View.GONE
                viewDialog.foto4.visibility = View.GONE

                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.Diger::class.java)!!
                        viewDialog.etOlculerDiger.setText(gelenData.olculer)
                        viewDialog.etSiparisNotuDiger.setText(itemData.siparis_notu)

                        if (!itemData.foto1.isNullOrEmpty()) {
                            Picasso.get().load(itemData.foto1).resize(size, size).into(viewDialog.foto1)
                            viewDialog.foto1.visibility = View.VISIBLE
                        }
                        if (!itemData.foto2.isNullOrEmpty()) {
                            Picasso.get().load(itemData.foto2).resize(size, size).into(viewDialog.foto2)
                            viewDialog.foto2.visibility = View.VISIBLE
                        }

                        if (!itemData.foto3.isNullOrEmpty()) {
                            Picasso.get().load(itemData.foto3).resize(size, size).into(viewDialog.foto3)
                            viewDialog.foto3.visibility = View.VISIBLE
                        }

                        if (!itemData.foto4.isNullOrEmpty()) {
                            Picasso.get().load(itemData.foto4).resize(size, size).into(viewDialog.foto4)
                            viewDialog.foto4.visibility = View.VISIBLE
                        }


                    }
                })

                viewDialog.foto1.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto1).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto2.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto2).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto3.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto3).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto4.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto4).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }

                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()

            }
            if (itemData.siparis_turu == "Tamir") {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                var viewDialog = inflate(myContext, R.layout.activity_tamir, null)
                viewDialog.appBarLayoutTamir.visibility = View.GONE
                viewDialog.foto1Tamir.visibility = View.GONE
                viewDialog.foto2Tamir.visibility = View.GONE
                viewDialog.foto3Tamir.visibility = View.GONE
                viewDialog.foto4Tamir.visibility = View.GONE

                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        var gelenData = p0.getValue(SiparisData.Diger::class.java)!!
                        viewDialog.etTamir.setText(gelenData.olculer)
                        viewDialog.etSiparisNotuTamir.setText(itemData.siparis_notu)
                        viewDialog.etEksiklerTamir.setText(gelenData.eksikler)

                        if (!itemData.foto1.isNullOrEmpty()) {
                            Picasso.get().load(itemData.foto1).resize(size, size).into(viewDialog.foto1Tamir)
                            viewDialog.foto1Tamir.visibility = View.VISIBLE
                        }
                        if (!itemData.foto2.isNullOrEmpty()) {
                            Picasso.get().load(itemData.foto2).resize(size, size).into(viewDialog.foto2Tamir)
                            viewDialog.foto2Tamir.visibility = View.VISIBLE
                        }

                        if (!itemData.foto3.isNullOrEmpty()) {
                            Picasso.get().load(itemData.foto3).resize(size, size).into(viewDialog.foto3Tamir)
                            viewDialog.foto3Tamir.visibility = View.VISIBLE
                        }

                        if (!itemData.foto4.isNullOrEmpty()) {
                            Picasso.get().load(itemData.foto4).resize(size, size).into(viewDialog.foto4Tamir)
                            viewDialog.foto4Tamir.visibility = View.VISIBLE
                        }


                    }
                })

                viewDialog.foto1Tamir.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto1).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto2Tamir.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto2).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto3Tamir.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto3).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }
                viewDialog.foto4Tamir.setOnLongClickListener {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                    var viewDialog = inflate(myContext, R.layout.dialog_photo, null)
                    Picasso.get().load(itemData.foto4).into(viewDialog.imgFoto)
                    builder.setView(viewDialog)
                    var dialogSiparisTuru: Dialog = builder.create()
                    dialogSiparisTuru.show()
                    return@setOnLongClickListener true
                }

                builder.setView(viewDialog)
                var dialogSiparisTuru: Dialog = builder.create()
                dialogSiparisTuru.show()

            }
        }

        holder.itemView.setOnLongClickListener {
            val popup = PopupMenu(myContext, holder.itemView)
            popup.inflate(R.menu.popup_menu_uretim)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.popUretimiTamamla -> {
                        if (itemData.siparis_turu == "Mafsallı Tente") {
                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                            ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var tenteData = p0.getValue(SiparisData.MafsallıTente::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()

                                                        myContext.startActivity(Intent(myContext, MontajActivity::class.java))
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
                        if (itemData.siparis_turu == "Körüklü Tente") {
                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                            ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var tenteData = p0.getValue(SiparisData.KorukluTenteData::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()
                                                        myContext.startActivity(Intent(myContext, MontajActivity::class.java))

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
                        if (itemData.siparis_turu == "Pergole") {
                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                            ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var tenteData = p0.getValue(SiparisData.PergoleData::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()
                                                        myContext.startActivity(Intent(myContext, MontajActivity::class.java))

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
                        if (itemData.siparis_turu == "Şemsiye") {
                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                            ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var tenteData = p0.getValue(SiparisData.SemsiyeData::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()
                                                        myContext.startActivity(Intent(myContext, MontajActivity::class.java))
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
                        if (itemData.siparis_turu == "Karpuz Tente") {
                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                            ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var tenteData = p0.getValue(SiparisData.KarpuzData::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()
                                                        myContext.startActivity(Intent(myContext, MontajActivity::class.java))
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
                        if (itemData.siparis_turu == "Şeffaf") {
                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                            ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var tenteData = p0.getValue(SiparisData.SeffafData::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()
                                                        myContext.startActivity(Intent(myContext, MontajActivity::class.java))

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
                        if (itemData.siparis_turu == "Wintend") {
                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                            ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var tenteData = p0.getValue(SiparisData.Wintend::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()
                                                        myContext.startActivity(Intent(myContext, MontajActivity::class.java))

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
                        if (itemData.siparis_turu == "Diğer") {
                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                            ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var tenteData = p0.getValue(SiparisData.Diger::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()
                                                        myContext.startActivity(Intent(myContext, MontajActivity::class.java))

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
                        if (itemData.siparis_turu == "Tamir") {
                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Üretimi tamamla").setMessage("Emin Misin ?").setPositiveButton("Gönder", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        ref.child("Montaj").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                            ref.child("Uretim").child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var tenteData = p0.getValue(SiparisData.Diger::class.java)!!
                                                    ref.child("Montaj").child(itemData.siparis_key.toString()).child("tenteData").setValue(tenteData).addOnCompleteListener {
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten").setValue(kullaniciKey)
                                                        ref.child("Montaj").child(itemData.siparis_key.toString()).child("ureten_zaman").setValue(ServerValue.TIMESTAMP)
                                                        ref.child("Uretim").child(itemData.siparis_key.toString()).removeValue()
                                                        myContext.startActivity(Intent(myContext, MontajActivity::class.java))

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
                                uretimRef.child(itemData.siparis_key.toString()).child("siparis_teklif").setValue(fiyat).addOnCompleteListener {
                                    myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                }

                            }
                        })

                        builder.setView(viewDialog)
                        var dialog: Dialog = builder.create()
                        dialog.show()
                    }
                    R.id.popDüzenle -> {
                        if (yetki == "Yönetici") {
                            if (itemData.siparis_turu == "Mafsallı Tente") {
                                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                                var viewDialog = inflate(myContext, R.layout.activity_tente_mafsalli, null)
                                viewDialog.spSanzimanYonuMafsalli.visibility = View.GONE
                                viewDialog.spSacakTuruMafsalli.visibility = View.GONE
                                viewDialog.spMotorMafsalli.visibility = View.GONE
                                viewDialog.spAyakYonuMafsalli.visibility = View.GONE
                                viewDialog.spMantolamaMafsalli.visibility = View.GONE
                                viewDialog.appBarLayoutMafsalli.visibility = View.GONE
                                viewDialog.foto1Mafsalli.visibility = View.GONE
                                viewDialog.foto2Mafsalli.visibility = View.GONE
                                viewDialog.foto3Mafsalli.visibility = View.GONE
                                viewDialog.foto4Mafsalli.visibility = View.GONE

                                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onDataChange(p0: DataSnapshot) {
                                        var gelenData = p0.getValue(SiparisData.MafsallıTente::class.java)!!

                                        viewDialog.etCephe.setText(gelenData.cephe)
                                        viewDialog.etAcilim.setText(gelenData.acilim)
                                        viewDialog.etKumasKodu.setText(gelenData.kumaskodu)
                                        viewDialog.etSacakTuruMafsalli.setText(gelenData.sacak_turu)
                                        viewDialog.etSacakYazisi.setText(gelenData.sacak_yazisi)
                                        viewDialog.etMotor.setText(gelenData.motor)
                                        viewDialog.etSanziman.setText(gelenData.sanzimanYonu)
                                        viewDialog.etAyakTuruMafsalli.setText(gelenData.ayakTuru)
                                        viewDialog.etMantolamaMafsalli.setText(gelenData.mantolama)
                                        viewDialog.etProfilRengi.setText(gelenData.profilRengi)
                                        viewDialog.etSiparisNotu.setText(itemData.siparis_notu)
                                        viewDialog.etEksiklerMafsalli.setText(gelenData.eksikler)
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
                                        var motorVar = viewDialog.etMotor.text.toString()
                                        var ayakTuru = viewDialog.etAyakTuruMafsalli.text.toString()
                                        var mantolama = viewDialog.etMantolamaMafsalli.text.toString()

                                        var eksikler = viewDialog.etEksiklerMafsalli.text.toString()
                                        var guncelData = SiparisData.MafsallıTente(
                                            cephe, acilim, kumasKodu, sacakTuru, sacakYazisi,
                                            motorVar, sanziman, ayakTuru, mantolama, profilRengi, itemData.siparis_key.toString(), eksikler
                                        )
                                        siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                        siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                        myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

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
                                viewDialog.spIpYonu.visibility = View.GONE
                                viewDialog.spAyakYonu.visibility = View.GONE
                                viewDialog.appBarLayoutKoruklu.visibility = View.GONE
                                viewDialog.etSeritRengiAdeti.visibility = View.VISIBLE
                                viewDialog.chSeritRengiVarMi.visibility = View.GONE
                                viewDialog.foto1Koruklu.visibility = View.GONE
                                viewDialog.foto2Koruklu.visibility = View.GONE
                                viewDialog.foto3Koruklu.visibility = View.GONE
                                viewDialog.foto4Koruklu.visibility = View.GONE
                                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onDataChange(p0: DataSnapshot) {
                                        var gelenData = p0.getValue(SiparisData.KorukluTenteData::class.java)!!
                                        viewDialog.etCephe.setText(gelenData.cephe)
                                        viewDialog.etAcilim.setText(gelenData.acilim)
                                        viewDialog.etKumasKodu.setText(gelenData.kumaskodu)
                                        viewDialog.etSacakTuruKoruklu.setText(gelenData.sacak_turu)
                                        viewDialog.etSacakBiyesRengi.setText(gelenData.sacak_biyesi_rengi)
                                        viewDialog.etSeritRengi.setText(gelenData.serit_rengi)
                                        viewDialog.etSeritRengiAdeti.setText(gelenData.serit_rengi_adeti)
                                        viewDialog.etSacakYazisi.setText(gelenData.tente_sacak_yazisi)
                                        viewDialog.etIpYonu.setText(gelenData.ipYonu)
                                        viewDialog.etAyakTuru.setText(gelenData.ayakTuru)
                                        viewDialog.etProfilRengi.setText(gelenData.profilRengi)
                                        viewDialog.etSiparisNotu.setText(itemData.siparis_notu)
                                        viewDialog.etEksiklerKoruklu.setText(gelenData.eksikler)

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
                                        var seritRengi = viewDialog.etSeritRengi.text.toString()
                                        var seritRengiAdeti = viewDialog.etSeritRengiAdeti.text.toString()
                                        var sacakYazisi = viewDialog.etSacakYazisi.text.toString()
                                        var ipyonu = viewDialog.etIpYonu.text.toString()
                                        var ayakTuru = viewDialog.etAyakTuru.text.toString()
                                        var profilRengi = viewDialog.etProfilRengi.text.toString()
                                        var siparisNot = viewDialog.etSiparisNotu.text.toString()

                                        var eksikler = viewDialog.etEksiklerKoruklu.text.toString()

                                        var guncelData = SiparisData.KorukluTenteData(
                                            cephe,
                                            acilim,
                                            kumasKodu,
                                            sacakTuru,
                                            sacakBiyesiRengi,
                                            seritRengi,
                                            seritRengiAdeti,
                                            sacakYazisi,
                                            ipyonu,
                                            profilRengi,
                                            ayakTuru,
                                            itemData.siparis_key.toString(), eksikler
                                        )
                                        siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                        siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                        myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

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
                                viewDialog.chKornerDirek.visibility = View.GONE
                                viewDialog.chCamVarMiVar.visibility = View.GONE


                                viewDialog.foto1Pergole.visibility = View.GONE
                                viewDialog.foto2Pergole.visibility = View.GONE
                                viewDialog.foto3Pergole.visibility = View.GONE
                                viewDialog.foto4Pergole.visibility = View.GONE
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
                                        viewDialog.etKornerDirekOlcu.setText(gelenData.korner_direk_olcusu)
                                        viewDialog.etKornerDirekAdet.setText(gelenData.korner_direk_adeti)
                                        viewDialog.etCamKaydi.setText(gelenData.cam_kaydi_olcusu)
                                        viewDialog.etCamKaydiAdet.setText(gelenData.cam_kaydi_adeti)
                                        viewDialog.etCamVarmi.setText(gelenData.etrafinda_cam_varmi)
                                        viewDialog.etPergoleCesidi.setText(gelenData.pergole_cesidi)
                                        viewDialog.etSiparisNotuPergole.setText(itemData.siparis_notu)
                                        viewDialog.etEksiklerPergole.setText(gelenData.eksikler)


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
                                        var kornerDirekOlcusu = viewDialog.etKornerDirekOlcu.text.toString()
                                        var kornerDirekAdet = viewDialog.etKornerDirekAdet.text.toString()
                                        var camKaydiOlcusu = viewDialog.etCamKaydi.text.toString()
                                        var camKaydiAdet = viewDialog.etCamKaydiAdet.text.toString()
                                        var pergoleCesidi = viewDialog.etPergoleCesidi.text.toString()
                                        var etrafindaCamVar = viewDialog.etCamVarmi.text.toString()


                                        var siparisNot = viewDialog.etSiparisNotuPergole.text.toString()
                                        var eksikler = viewDialog.etEksiklerPergole.text.toString()

                                        var guncelData = SiparisData.PergoleData(
                                            pergoleTuru, cephe, acilim, arka, on, kumasRengi, profilRengi,
                                            led, motorYonu, kornerDirekOlcusu, kornerDirekAdet, camKaydiOlcusu, camKaydiAdet, pergoleCesidi, etrafindaCamVar, itemData.siparis_key.toString(), eksikler
                                        )
                                        siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                        siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                        myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))


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

                                viewDialog.foto1Semsiye.visibility = View.GONE
                                viewDialog.foto2Semsiye.visibility = View.GONE
                                viewDialog.foto3Semsiye.visibility = View.GONE
                                viewDialog.foto4Semsiye.visibility = View.GONE

                                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onDataChange(p0: DataSnapshot) {
                                        var gelenData = p0.getValue(SiparisData.SemsiyeData::class.java)!!
                                        viewDialog.etSemsiyeTuru.setText(gelenData.semsiye_turu)
                                        viewDialog.etGenislikSemsiye.setText(gelenData.genislik)
                                        viewDialog.etKumasRengiSemsiye.setText(gelenData.kumas_rengi)
                                        viewDialog.etSacakYazisiSemsiye.setText(gelenData.sacak_yazisi)
                                        viewDialog.etSiparisNotuSemsiye.setText(itemData.siparis_notu)
                                        viewDialog.etEksiklerSemsiye.setText(gelenData.eksikler)

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
                                        var eksikler = viewDialog.etEksiklerSemsiye.text.toString()

                                        var guncelData = SiparisData.SemsiyeData(semsiyeTuru, genislik, kumasRengi, sacakYazisi, itemData.siparis_key.toString(), eksikler)
                                        siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                        siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                        myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))


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

                                viewDialog.foto1Karpuz.visibility = View.GONE
                                viewDialog.foto2Karpuz.visibility = View.GONE
                                viewDialog.foto3Karpuz.visibility = View.GONE
                                viewDialog.foto4Karpuz.visibility = View.GONE
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
                                        viewDialog.etEksiklerKarpuz.setText(gelenData.eksikler)

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
                                        var eksikler = viewDialog.etEksiklerKarpuz.text.toString()

                                        var guncelData = SiparisData.KarpuzData(genislik, yukseklik, kumasRengi, sacakTuru, sacakYazisi, sacakBiyesiRengi, seritRengi, itemData.siparis_key.toString(), eksikler)
                                        siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                        siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                        myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

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


                                viewDialog.foto1Seffaf.visibility = View.GONE
                                viewDialog.foto2Seffaf.visibility = View.GONE
                                viewDialog.foto3Seffaf.visibility = View.GONE
                                viewDialog.foto4Seffaf.visibility = View.GONE
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
                                        viewDialog.etEksiklerSeffaf.setText(gelenData.eksikler)

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
                                        var eksikler = viewDialog.etEksiklerSeffaf.text.toString()

                                        var guncelData = SiparisData.SeffafData(mika, pvcRengi, altPvc, ustPvc, fermuar, boruyeri, eksSacak, itemData.siparis_key.toString(), eksikler)

                                        siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                        siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                        myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

                                    }
                                })


                                builder.setView(viewDialog)
                                var dialogSiparisTuru: Dialog = builder.create()
                                dialogSiparisTuru.show()
                            }
                            if (itemData.siparis_turu == "Wintend") {
                                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                                var viewDialog = inflate(myContext, R.layout.activity_wintent, null)
                                viewDialog.spSanzimanYonuWintend.visibility = View.GONE
                                viewDialog.spSacakTuruWintend.visibility = View.GONE
                                viewDialog.spMotorWintend.visibility = View.GONE
                                viewDialog.spAyakTuruWintend.visibility = View.GONE
                                viewDialog.spMantolamaWintend.visibility = View.GONE
                                viewDialog.appBarLayoutWintend.visibility = View.GONE

                                viewDialog.foto1Wintent.visibility = View.GONE
                                viewDialog.foto2Wintent.visibility = View.GONE
                                viewDialog.foto3Wintent.visibility = View.GONE
                                viewDialog.foto4Wintent.visibility = View.GONE
                                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onDataChange(p0: DataSnapshot) {
                                        var gelenData = p0.getValue(SiparisData.Wintend::class.java)!!
                                        viewDialog.etCepheWintend.setText(gelenData.cephe)
                                        viewDialog.etKolBoyuWintend.setText(gelenData.kolboyu)
                                        viewDialog.etKumasKoduWintend.setText(gelenData.kumaskodu)
                                        viewDialog.etSacakTuruWintend.setText(gelenData.sacak_turu)
                                        viewDialog.etSacakYazisiWintend.setText(gelenData.sacak_yazisi)
                                        viewDialog.etMotorWintend.setText(gelenData.motor)
                                        viewDialog.etSanzimanWintend.setText(gelenData.sanzimanYonu)
                                        viewDialog.etAyakTuruWintend.setText(gelenData.ayakTuru)
                                        viewDialog.etMantolamaWintend.setText(gelenData.mantolama)
                                        viewDialog.etProfilRengiWintend.setText(gelenData.profilRengi)
                                        viewDialog.etSiparisNotuWintend.setText(itemData.siparis_notu)
                                        viewDialog.etEksiklerWintent.setText(gelenData.eksikler)

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

                                        var cephe = viewDialog.etCepheWintend.text.toString()
                                        var kolboyu = viewDialog.etKolBoyuWintend.text.toString()
                                        var kumasKodu = viewDialog.etKumasKoduWintend.text.toString()
                                        var sacakTuru = viewDialog.etSacakTuruWintend.text.toString()
                                        var sacakYazisi = viewDialog.etSacakYazisiWintend.text.toString()
                                        var sanziman = viewDialog.etSanzimanWintend.text.toString()
                                        var profilRengi = viewDialog.etProfilRengiWintend.text.toString()
                                        var motorVar = viewDialog.etMotorWintend.text.toString()
                                        var ayakTuru = viewDialog.etAyakTuruWintend.text.toString()
                                        var mantolama = viewDialog.etMantolamaWintend.text.toString()

                                        var siparisNot = viewDialog.etSiparisNotuWintend.text.toString()
                                        var eksikler = viewDialog.etEksiklerWintent.text.toString()

                                        var guncelData = SiparisData.Wintend(
                                            cephe, kolboyu, kumasKodu, sacakTuru, sacakYazisi, motorVar, sanziman,
                                            ayakTuru, mantolama, profilRengi, itemData.siparis_key.toString(), eksikler
                                        )

                                        siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                        siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                        myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

                                    }
                                })


                                builder.setView(viewDialog)
                                var dialogSiparisTuru: Dialog = builder.create()
                                dialogSiparisTuru.show()
                            }
                            if (itemData.siparis_turu == "Diğer") {
                                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                                var viewDialog = inflate(myContext, R.layout.activity_diger, null)
                                viewDialog.appBarLayoutDiger.visibility = View.GONE
                                viewDialog.foto1.visibility = View.GONE
                                viewDialog.foto2.visibility = View.GONE
                                viewDialog.foto3.visibility = View.GONE
                                viewDialog.foto4.visibility = View.GONE

                                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onDataChange(p0: DataSnapshot) {
                                        var gelenData = p0.getValue(SiparisData.Diger::class.java)!!
                                        viewDialog.etOlculerDiger.setText(gelenData.olculer)
                                        viewDialog.etSiparisNotuDiger.setText(itemData.siparis_notu)
                                        viewDialog.etEksiklerDiger.setText(gelenData.eksikler)


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
                                        var olcu = viewDialog.etOlculerDiger.text.toString()
                                        var siparisNot = viewDialog.etSiparisNotuDiger.text.toString()
                                        var eksikler = viewDialog.etEksiklerDiger.text.toString()

                                        var guncelData = SiparisData.Diger(olcu, itemData.siparis_key.toString(), eksikler)
                                        siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                        siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                        myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))


                                    }
                                })
                                builder.setView(viewDialog)
                                var dialogSiparisTuru: Dialog = builder.create()
                                dialogSiparisTuru.show()

                            }
                            if (itemData.siparis_turu == "Tamir") {
                                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                                var viewDialog = inflate(myContext, R.layout.activity_tamir, null)
                                viewDialog.appBarLayoutTamir.visibility = View.GONE
                                viewDialog.foto1Tamir.visibility = View.GONE
                                viewDialog.foto2Tamir.visibility = View.GONE
                                viewDialog.foto3Tamir.visibility = View.GONE
                                viewDialog.foto4Tamir.visibility = View.GONE

                                siparisRef.child(itemData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onDataChange(p0: DataSnapshot) {
                                        var gelenData = p0.getValue(SiparisData.Diger::class.java)!!
                                        viewDialog.etTamir.setText(gelenData.olculer)
                                        viewDialog.etSiparisNotuTamir.setText(itemData.siparis_notu)
                                        viewDialog.etEksiklerTamir.setText(gelenData.eksikler)


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
                                        var tamir = viewDialog.etTamir.text.toString()
                                        var siparisNot = viewDialog.etSiparisNotuTamir.text.toString()
                                        var eksikler = viewDialog.etEksiklerTamir.text.toString()

                                        var guncelData = SiparisData.Diger(tamir, itemData.siparis_key.toString(), eksikler)
                                        siparisRef.child(itemData.siparis_key.toString()).child("siparis_notu").setValue(siparisNot)
                                        siparisRef.child(itemData.siparis_key.toString()).child("tenteData").setValue(guncelData)
                                        myContext.startActivity(Intent(myContext, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))


                                    }
                                })
                                builder.setView(viewDialog)
                                var dialogSiparisTuru: Dialog = builder.create()
                                dialogSiparisTuru.show()

                            }
                        } else {
                            Toast.makeText(myContext, "Yetkin yok. Yönetici ile görüş.!!", Toast.LENGTH_LONG).show()
                        }

                    }
                    R.id.popSil -> {

                        var alert = AlertDialog.Builder(myContext)
                            .setTitle("Siparişi Sil")
                            .setMessage("Emin Misin ?")
                            .setPositiveButton("Sil", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    ref.child("Silinenler/Uretim").child(itemData.siparis_key.toString()).setValue(itemData).addOnCompleteListener {
                                        uretimRef.child(itemData.siparis_key.toString()).removeValue()
                                        uretimler.removeAt(position)
                                        notifyDataSetChanged()
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

    }

    inner class UretimHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tumLayout = itemView.tumLayout
        var musteriAd = itemView.tvMusteriAdi
        var telNo = itemView.tvMusteriTel
        var siparisTuru = itemView.tvSiparisTuru
        var ureten = itemView.tvUretenAdi
        var uretenZaman = itemView.tvUretenZamani
        var tvKumasKodu = itemView.tvKumasKodu
        var not = itemView.tvNot
        var tvEksikler = itemView.tvEksik


        fun setData(siparisData: SiparisData) {
            try {
                not.text = siparisData.siparis_notu
                siparisTuru.text = siparisData.siparis_turu
                if (!siparisData.uretime_gonderen_zaman.toString().isNullOrEmpty()) {
                    uretenZaman.text = formatData(siparisData.uretime_gonderen_zaman.toString().toLong()).toString()
                }
                musteriBilgileri(siparisData)


            } catch (e: IOException) {
                Log.e("sad", siparisData.siparis_key.toString())
            }


            uretimRef.child(siparisData.siparis_key.toString()).child("tenteData").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    var eksikler = p0.child("eksikler").value.toString()

                    if (eksikler.toString() != "null") {
                        if (eksikler.length > 0) {
                            tumLayout.setBackgroundColor(ContextCompat.getColor(myContext, R.color.mavi))
                            tvEksikler.visibility = View.VISIBLE
                            tvEksikler.text = "Eksikler; " + eksikler
                        }
                    } else tvEksikler.visibility = View.GONE



                    var kumas_rengi = p0.child("kumas_rengi").value.toString()
                    var kumas_kodu = p0.child("kumaskodu").value.toString()

                    Log.e("kumas rengi",kumas_rengi.toString())
                    Log.e("kumas kodu",kumas_kodu.toString())


                    if (kumas_rengi.toString() != "null") {
                        tvKumasKodu.text = kumas_rengi.toString()
                    } else if (kumas_kodu.toString() != "null") {
                        tvKumasKodu.text = kumas_kodu.toString()
                    }



                }
            })
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

                    musteriAdi = p0.child("musteri_ad_soyad").value.toString()
                    var musteriTelNo = p0.child("musteri_tel").value.toString()

                    telNo.setOnClickListener {
                        val arama = Intent(Intent.ACTION_DIAL) //Bu kod satırımız bizi rehbere telefon numarası ile yönlendiri.
                        arama.data = Uri.parse("tel:" + musteriTelNo)
                        myContext.startActivity(arama)
                    }

                    musteriAd.text = p0.child("musteri_ad_soyad").value.toString()
                    telNo.text = p0.child("musteri_tel").value.toString()


                }
            })


            ref.child("users").child(siparisData.uretime_gonderen.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
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