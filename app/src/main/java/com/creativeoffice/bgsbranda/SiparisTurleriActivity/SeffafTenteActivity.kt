package com.creativeoffice.bgsbranda.SiparisTurleriActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.creativeoffice.bgsbranda.Activity.SiparislerActivity
import com.creativeoffice.bgsbranda.Datalar.SiparisData
import com.creativeoffice.bgsbranda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_seffaf_tente.*
import kotlinx.android.synthetic.main.activity_seffaf_tente.tvSiparisEkle


class SeffafTenteActivity : AppCompatActivity() {
    val ref = FirebaseDatabase.getInstance().reference
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    var fermuar = ""
    var boruYeri = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seffaf_tente)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        var musteriKey = intent.getStringExtra("musteriKey")
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        spinnerAyarlari()
        siparisEkle(musteriKey)

    }

    private fun siparisEkle(musteriKey: String?) {
        tvSiparisEkle.setOnClickListener {
            var siparisNotu = etSiparisNotuSeffaf.text.toString()
            var siparisKey = ref.child("Siparisler").push().key.toString()
            var siparisTuru = "Şeffaf"
            var siparisData = SiparisData(siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID, null, null, null, null, null, null, null, null, null)

            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)


                val seffafMika = etMikaEni.text.toString()
                val pvcRengi = etPVCRengi.text.toString()
                val altPvc = etAltPvc.text.toString()
                val ustPvc = etUstPvc.text.toString()
                val ekstraSacak = etEksSacak.text.toString()


                var tenteData = SiparisData.SeffafData(seffafMika, pvcRengi, altPvc, ustPvc, fermuar, boruYeri, ekstraSacak,siparisKey)

                ref.child("Siparisler").child(siparisKey).child("tenteData").setValue(tenteData).addOnCompleteListener {
                    val intent = Intent(this, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "Sipariş Girilemedi", Toast.LENGTH_LONG).show()
                    ref.child("Siparisler").child(siparisKey).removeValue()

                }


            }
        }
    }


    private fun spinnerAyarlari() {
        etFermuar.visibility = View.GONE
        var fermuarTurleri = arrayOf("Düz", "Kapaklı")
        spFermuar.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fermuarTurleri)
        spFermuar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                fermuar = "Düz"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fermuar = fermuarTurleri[position]
            }

        }

        etBoruYeri.visibility = View.GONE
        var boruYeriTurleri = arrayOf("Yok", "Var")
        spBoruYeri.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, boruYeriTurleri)
        spBoruYeri.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                boruYeri = "Yok"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                boruYeri = boruYeriTurleri[position]
            }

        }
    }
}