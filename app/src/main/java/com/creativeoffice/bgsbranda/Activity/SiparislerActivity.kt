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
import com.creativeoffice.bgsbranda.Adapter.SiparisAdapter
import com.creativeoffice.bgsbranda.BottomNavigationViewHelper
import com.creativeoffice.bgsbranda.Datalar.SiparisData
import com.creativeoffice.bgsbranda.LoadingDialog
import com.creativeoffice.bgsbranda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_siparisler.*


class SiparislerActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 0
    var siparislerList = ArrayList<SiparisData>()
    val ref = FirebaseDatabase.getInstance().reference
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var userID: String

    var loading: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siparisler)
        setupNavigationView()
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        Toast.makeText(this, "Kırmızılar; Tamir, \nSarılar; Ölçü Alınacak,\nMavi;Eksik var", Toast.LENGTH_LONG).show()

        Handler().postDelayed({ dialogGizle() }, 3000)
        initMyAuthStateListener()

        mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser
        if (user != null) {
            userID = mAuth.currentUser!!.uid
            setupVeri()
            dialogCalistir()
        } else {
            var intent = Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

    }


    private fun setupVeri() {

        imgBack.setOnClickListener {
            mAuth.signOut()
            var user = mAuth.currentUser
            if (user != null) {
                userID = mAuth.currentUser!!.uid
                dialogCalistir()
            } else {
                var intent = Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
        }

        ref.child("Siparisler").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    for (ds in p0.children) {
                        try {
                            var gelenData = ds.getValue(SiparisData::class.java)!!
                            siparislerList.add(gelenData)

                        } catch (ex: Exception) {
                            Log.e("siparisDataHatası ", ex.message.toString())
                        }
                    }
                    dialogGizle()
                    setupRecyclerView()
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

    private fun setupRecyclerView() {
        siparislerList.sortBy { it.siparis_girme_zamani }
        rcSiparisler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = SiparisAdapter(this, siparislerList, userID)
        rcSiparisler.adapter = adapter
        rcSiparisler.setHasFixedSize(true)
    }


    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNav)
        BottomNavigationViewHelper.setupNavigation(this, bottomNav) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu: Menu = bottomNav.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    private fun initMyAuthStateListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val kullaniciGirisi = p0.currentUser
                if (kullaniciGirisi != null) { //eğer kişi giriş yaptıysa nul gorunmez. giriş yapmadıysa null olur
                } else {
                    startActivity(Intent(this@SiparislerActivity, LoginActivity::class.java))
                }
            }
        }
    }

}
