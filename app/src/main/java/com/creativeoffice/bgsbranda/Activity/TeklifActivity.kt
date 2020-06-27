package com.creativeoffice.bgsbranda.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativeoffice.bgsbranda.Adapter.TeklifAdapter
import com.creativeoffice.bgsbranda.BottomNavigationViewHelper
import com.creativeoffice.bgsbranda.Datalar.SiparisData
import com.creativeoffice.bgsbranda.LoadingDialog
import com.creativeoffice.bgsbranda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_teklif.*
import java.lang.Exception
import kotlin.math.log

class TeklifActivity : AppCompatActivity() {

    val ACTIVITY_NO = 1
    val ref = FirebaseDatabase.getInstance().reference
    lateinit var teklifList: ArrayList<SiparisData>
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String


    var loading: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teklif)
        setupNavigationView()
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        dialogCalistir()

        ref.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var yetki = p0.child("yetki").value.toString()

                if (yetki == "Yönetici") {/*
                    val intent = Intent(this@TeklifActivity, SiparislerActivity::class.java)
                    startActivity(intent)
                    finish()*/
                    Handler().postDelayed({ setupVeri() }, 1500)
                    Handler().postDelayed({ dialogGizle() }, 5000)

                } else {
                    Toast.makeText(this@TeklifActivity,"$yetki Görmek için yetkili değilsin!!..!!",Toast.LENGTH_LONG).show()
                }
            }

        })


    }

    fun dialogGizle() {
        loading?.let { if (it.isShowing) it.cancel() }

    }

    fun dialogCalistir() {
        dialogGizle()
        loading = LoadingDialog.startDialog(this)
    }

    private fun setupVeri() {
        teklifList = ArrayList()
        ref.child("Teklifler").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    for (ds in p0.children) {
                        try {
                            var gelenData = ds.getValue(SiparisData::class.java)!!
                            teklifList.add(gelenData)
                            dialogGizle()
                        } catch (e: Exception) {
                            Log.e("teklifData", "Teklif data alırken hata var. $e")
                        }
                    }
                    setupRecyclerView()
                }
            }

        })
    }


    private fun setupRecyclerView() {
        rcTeklifler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = TeklifAdapter(this, teklifList, userID)
        rcTeklifler.adapter = adapter
        rcTeklifler.setHasFixedSize(true)
    }


    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNav)
        BottomNavigationViewHelper.setupNavigation(this, bottomNav) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu: Menu = bottomNav.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

}
