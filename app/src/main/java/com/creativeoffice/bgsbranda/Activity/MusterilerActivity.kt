package com.creativeoffice.bgsbranda.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.WindowManager

import androidx.recyclerview.widget.LinearLayoutManager
import com.creativeoffice.bgsbranda.Adapter.MusteriAdapter
import com.creativeoffice.bgsbranda.BottomNavigationViewHelper
import com.creativeoffice.bgsbranda.Datalar.MusteriData
import com.creativeoffice.bgsbranda.LoadingDialog
import com.creativeoffice.bgsbranda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_musteriler.*
import kotlinx.android.synthetic.main.dialog_musteri_ekle.view.*
import java.lang.Exception


class MusterilerActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    lateinit var musteriler: ArrayList<MusteriData>

    val ref = FirebaseDatabase.getInstance().reference

    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var kullaniciAdi: String

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
        Handler().postDelayed({ setupVeri() }, 1500)
        Handler().postDelayed({ dialogGizle() }, 5000)

    }

    fun dialogGizle() {
        loading?.let { if (it.isShowing) it.cancel() }

    }

    fun dialogCalistir() {
        dialogGizle()
        loading = LoadingDialog.startDialog(this)
    }

    fun setupKullaniciAdi() {
        FirebaseDatabase.getInstance().reference.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                kullaniciAdi = p0.child("user_name").value.toString()
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
                    Log.e("sadd", musteriData.musteri_ad_soyad!!)
                }
            })

            var dialog: Dialog = builder.create()
            dialog.show()
        }
    }

    private fun setupVeri() {
        musteriler = ArrayList()
        ref.child("Musteriler").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.hasChildren()) {
                    for (ds in p0.children) {

                        try {
                            var gelenData = ds.getValue(MusteriData::class.java)!!
                            musteriler.add(gelenData)

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
        musteriler.sortBy { it.musteri_ad_soyad }
        rcMusteri.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = MusteriAdapter(this, musteriler, kullaniciAdi)
        rcMusteri.adapter = adapter
        rcMusteri.setHasFixedSize(true)

    }


    fun setupNavigationView() {

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNav)
        BottomNavigationViewHelper.setupNavigation(this, bottomNav) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu: Menu = bottomNav.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

}
