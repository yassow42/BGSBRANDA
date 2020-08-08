package com.creativeoffice.bgsbranda.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import com.creativeoffice.bgsbranda.Adapter.MusteriAdapter
import com.creativeoffice.bgsbranda.BottomNavigationViewHelper
import com.creativeoffice.bgsbranda.Datalar.MusteriData
import com.creativeoffice.bgsbranda.LoadingDialog
import com.creativeoffice.bgsbranda.R
import com.creativeoffice.bgsbranda.SiparisTurleriActivity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_musteriler.*
import kotlinx.android.synthetic.main.dialog_gidilen_musteri.view.*
import kotlinx.android.synthetic.main.dialog_musteri_ekle.view.*
import kotlinx.android.synthetic.main.dialog_siparis_ekle.view.*
import java.lang.Exception
import kotlin.collections.ArrayList


class MusterilerActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    var musteriList: ArrayList<MusteriData> = ArrayList()
    var musteriAdList: ArrayList<String> = ArrayList()
    lateinit var dialogViewSpArama: View

    val ref = FirebaseDatabase.getInstance().reference

    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var kullaniciAdi: String
    lateinit var yetki: String

    var loading: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musteriler)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        setupKullaniciAdi()
        setupNavigationView()
        butonlar()

        dialogCalistir()
        setupVeri()
        Handler().postDelayed({ dialogGizle() }, 5000)


    }

    private fun setupSpinner() {
        var siraList = ArrayList<String>()
        siraList.add("İsme A -> Z")
        siraList.add("İsme Z -> A")

        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, siraList)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var secilenMarka = siraList[position]
                if (secilenMarka == "İsme A -> Z") {
                    musteriList.sortBy { it.musteri_ad_soyad }
                    rcMusteri.layoutManager = LinearLayoutManager(this@MusterilerActivity, LinearLayoutManager.VERTICAL, false)
                    val Adapter = MusteriAdapter(this@MusterilerActivity, musteriList, kullaniciAdi)
                    rcMusteri.adapter = Adapter
                    rcMusteri.setHasFixedSize(true)
                } else if (secilenMarka == "İsme Z -> A") {
                    musteriList.sortByDescending { it.musteri_ad_soyad }
                    rcMusteri.layoutManager = LinearLayoutManager(this@MusterilerActivity, LinearLayoutManager.VERTICAL, false)
                    val Adapter = MusteriAdapter(this@MusterilerActivity, musteriList, kullaniciAdi)
                    rcMusteri.adapter = Adapter
                    rcMusteri.setHasFixedSize(true)

                }
            }
        }
    }


    fun setupKullaniciAdi() {
        FirebaseDatabase.getInstance().reference.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                kullaniciAdi = p0.child("user_name").value.toString()
                yetki = p0.child("yetki").value.toString()

                setupSpinner()
            }
        })
    }


    private fun butonlar() {
        imgMusteriEkle.setOnClickListener {

            var builder: AlertDialog.Builder = AlertDialog.Builder(this)
            var inflater: LayoutInflater = layoutInflater
            val viewDialog: View = inflater.inflate(R.layout.dialog_musteri_ekle, null)

            builder.setView(viewDialog)
            builder.setTitle("Müşteri Ekle")
            builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.dismiss()
                }

            })
            builder.setPositiveButton("Ekle", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    var musteriAdi = viewDialog.etMusteriAdSoyad.text.toString()
                    var musteriTel = viewDialog.etTelefon.text.toString()
                    var musteriAdres = viewDialog.etAdres.text.toString()
                    val musteriKey = ref.child("Musteriler").push().key
                    val musteriData = MusteriData(musteriAdi, musteriAdres, musteriTel, musteriKey)
                    ref.child("Musteriler").child(musteriKey.toString()).setValue(musteriData)

                }
            })

            var dialog: Dialog = builder.create()
            dialog.show()
        }

        ref.child("Musteriler").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                for (ds in p0.children) {
                    var gelenData = ds.getValue(MusteriData::class.java)!!
                    musteriAdList.add(gelenData.musteri_ad_soyad.toString())
                }
                var adapterSearch = ArrayAdapter<String>(this@MusterilerActivity, android.R.layout.simple_expandable_list_item_1, musteriAdList)
                searchMs.setAdapter(adapterSearch)
            }
        })
        imgMusteriAra.setOnClickListener {
            var arananMusteriAdi = searchMs.text.toString()
            //  Log.e("ass1",arananMusteriAdi)
            val arananMusteriVarMi = musteriAdList.containsAll(listOf(arananMusteriAdi))

            if (arananMusteriVarMi) {
                var musteriKey: String
                ref.child("Musteriler").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}

                    override fun onDataChange(p0: DataSnapshot) {
                        for (ds in p0.children) {
                            var itemData = ds.getValue(MusteriData::class.java)!!
                            if (itemData.musteri_ad_soyad == arananMusteriAdi) {

                                val popupMenu = PopupMenu(this@MusterilerActivity, imgMusteriAra)
                                popupMenu.inflate(R.menu.search_menu)
                                popupMenu.setOnMenuItemClickListener {
                                    musteriKey = itemData.musteri_key.toString()
                                    when (it.itemId) {
                                        R.id.popSiparisGir -> {

                                            var builder: AlertDialog.Builder = AlertDialog.Builder(this@MusterilerActivity)
                                            var viewDialog = View.inflate(this@MusterilerActivity, R.layout.dialog_siparis_ekle, null)

                                            builder.setTitle(itemData.musteri_ad_soyad)

                                            viewDialog.tvTente.setOnClickListener {
                                                val intent = Intent(this@MusterilerActivity, MafsalliTenteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                intent.putExtra("musteriKey", itemData.musteri_key)
                                                startActivity(intent)
                                            }

                                            viewDialog.tvKorukluTente.setOnClickListener {
                                                val intent = Intent(this@MusterilerActivity, KorukluTenteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                intent.putExtra("musteriKey", itemData.musteri_key)
                                                startActivity(intent)
                                            }

                                            viewDialog.tvKisBahcesi.setOnClickListener {
                                                val intent = Intent(this@MusterilerActivity, PergoleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                intent.putExtra("musteriKey", itemData.musteri_key)
                                                startActivity(intent)
                                            }
                                            viewDialog.tvSemsiye.setOnClickListener {
                                                val intent = Intent(this@MusterilerActivity, SemsiyeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                intent.putExtra("musteriKey", itemData.musteri_key)
                                                startActivity(intent)
                                            }
                                            viewDialog.tvKarpuz.setOnClickListener {
                                                val intent = Intent(this@MusterilerActivity, KarpuzTenteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                intent.putExtra("musteriKey", itemData.musteri_key)
                                                startActivity(intent)
                                            }
                                            viewDialog.tvSeffaf.setOnClickListener {
                                                val intent = Intent(this@MusterilerActivity, SeffafTenteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                intent.putExtra("musteriKey", itemData.musteri_key)
                                                startActivity(intent)
                                            }
                                            viewDialog.tvWintent.setOnClickListener {
                                                val intent = Intent(this@MusterilerActivity, WintentActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                intent.putExtra("musteriKey", itemData.musteri_key)
                                                startActivity(intent)

                                            }
                                            viewDialog.tvDiger.setOnClickListener {
                                                val intent = Intent(this@MusterilerActivity, DigerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                intent.putExtra("musteriKey", itemData.musteri_key)
                                                startActivity(intent)

                                            }
                                            viewDialog.tvTamir.setOnClickListener {
                                                val intent = Intent(this@MusterilerActivity, TamirActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                intent.putExtra("musteriKey", itemData.musteri_key)
                                                startActivity(intent)

                                            }

                                            builder.setView(viewDialog)
                                            var dialogSiparisTuru: Dialog = builder.create()
                                            dialogSiparisTuru.show()

                                        }
                                        R.id.popMusteriDuzenle -> {
                                            var dialogMsDznle: Dialog
                                            var builder: AlertDialog.Builder = AlertDialog.Builder(this@MusterilerActivity)

                                            var dialogView: View = View.inflate(this@MusterilerActivity, R.layout.dialog_gidilen_musteri, null)
                                            builder.setView(dialogView)

                                            dialogMsDznle = builder.create()
                                            dialogView.tvAdSoyad.text = itemData.musteri_ad_soyad.toString()
                                            dialogView.etAdresGidilen.setText(itemData.musteri_adres.toString())
                                            dialogView.etTelefonGidilen.setText(itemData.musteri_tel.toString())

                                            dialogView.imgCheck.setOnClickListener {

                                                if (dialogView.etAdresGidilen.text.toString().isNotEmpty() && dialogView.etTelefonGidilen.text.toString().isNotEmpty()) {
                                                    var adres = dialogView.etAdresGidilen.text.toString()
                                                    var telefon = dialogView.etTelefonGidilen.text.toString()

                                                    ref.child("Musteriler").child(musteriKey).child("musteri_adres").setValue(adres)
                                                    ref.child("Musteriler").child(musteriKey).child("musteri_tel").setValue(telefon)
                                                    Toast.makeText(this@MusterilerActivity, "Müşteri Bilgileri Güncellendi", Toast.LENGTH_LONG).show()
                                                    dialogMsDznle.dismiss()

                                                } else {
                                                    Toast.makeText(this@MusterilerActivity, "Bilgilerde boşluklar var", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                            dialogView.imgBack.setOnClickListener {

                                                dialogMsDznle.dismiss()
                                            }

                                            dialogMsDznle.setCancelable(false)

                                            dialogMsDznle.show()
                                        }
                                        R.id.popSil -> {
                                            if (yetki == "Yönetici") {
                                                var alert = AlertDialog.Builder(this@MusterilerActivity)
                                                    .setTitle("Müşteriyi Sil")
                                                    .setMessage("Emin Misin ?")
                                                    .setPositiveButton("Sil", object : DialogInterface.OnClickListener {
                                                        override fun onClick(p0: DialogInterface?, p1: Int) {
                                                            ref.child("Musteriler").child(itemData.musteri_key.toString()).removeValue()
                                                            startActivity(Intent(this@MusterilerActivity, MusterilerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
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
                                    }

                                    return@setOnMenuItemClickListener true
                                }

                                popupMenu.show()


                            }
                        }

                    }
                })

/*
                ref.child("Musteriler").child(arananMusteriAdi).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0.hasChildren()) {
                            var musteriData = p0.getValue(MusteriData::class.java)!!



                        }


                    }

                })
*/

            } else {
                Toast.makeText(this, "Böyle Bir Müşteri Yok", Toast.LENGTH_SHORT).show()
            }


        }

    }

    private fun setupVeri() {
        musteriList = ArrayList()
        ref.child("Musteriler").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.hasChildren()) {
                    for (ds in p0.children) {

                        try {
                            var gelenData = ds.getValue(MusteriData::class.java)!!
                            musteriList.add(gelenData)

                        } catch (ex: Exception) {

                        }
                    }
                    dialogGizle()
                    setupRecyclerView()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        musteriList.sortBy { it.musteri_ad_soyad }
        rcMusteri.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = MusteriAdapter(this, musteriList, kullaniciAdi)
        rcMusteri.adapter = adapter
        rcMusteri.setHasFixedSize(true)

    }

    fun dialogGizle() {
        loading?.let { if (it.isShowing) it.cancel() }

    }

    fun dialogCalistir() {
        dialogGizle()
        loading = LoadingDialog.startDialog(this)
    }

    fun setupNavigationView() {

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNav)
        BottomNavigationViewHelper.setupNavigation(this, bottomNav) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu: Menu = bottomNav.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

}
