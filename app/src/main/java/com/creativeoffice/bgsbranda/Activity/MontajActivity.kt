package com.creativeoffice.bgsbranda.Activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativeoffice.bgsbranda.Adapter.MontajAdapter
import com.creativeoffice.bgsbranda.BottomNavigationViewHelper
import com.creativeoffice.bgsbranda.Datalar.SiparisData
import com.creativeoffice.bgsbranda.LoadingDialog
import com.creativeoffice.bgsbranda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_montaj.*


class MontajActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 3
    val ref = FirebaseDatabase.getInstance().reference
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var kullaniciAdi: String

    lateinit var montajList: ArrayList<SiparisData>
    var loading :Dialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_montaj)
        setupNavigationView()
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

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


    private fun setupVeri() {
        montajList = ArrayList()
        ref.child("Montaj").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    for (ds in p0.children) {
                        try {
                            var gelenData = ds.getValue(SiparisData::class.java)!!
                            montajList.add(gelenData)

                        } catch (ex: Exception) {
                            Log.e("montajDataHatası ", ex.message.toString())
                        }
                    }
                    dialogGizle()
                    setupRecyclerView()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        rcMontaj.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = MontajAdapter(this, montajList, userID)
        rcMontaj.adapter = adapter
        rcMontaj.setHasFixedSize(true)
    }


    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNav)
        BottomNavigationViewHelper.setupNavigation(this, bottomNav) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu: Menu = bottomNav.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}