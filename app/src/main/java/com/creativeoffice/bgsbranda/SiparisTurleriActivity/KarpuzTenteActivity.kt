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
import kotlinx.android.synthetic.main.activity_karpuz_tente.*
import kotlinx.android.synthetic.main.activity_karpuz_tente.tvSiparisEkle

class KarpuzTenteActivity : AppCompatActivity() {
    val ref = FirebaseDatabase.getInstance().reference
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    var sacakTuru = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_karpuz_tente)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        var musteriKey = intent.getStringExtra("musteriKey")
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid

        spinnerAyarlari()
       siparisEkle(musteriKey)


    }

    private fun siparisEkle(musteriKey: String?) {
        tvSiparisEkle.setOnClickListener {
            var siparisNotu = etSiparisNotuKarpuz.text.toString()
            var siparisKey = ref.child("Siparisler").push().key.toString()
            var siparisTuru = "Karpuz Tente"
            var siparisData = SiparisData(siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID, null, null, null, null, null, null, null, null, null)

            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)


                val genislik = etGenislikKarpuz.text.toString()
                val yukseklik = etYukseklikKarpuz.text.toString()
                val sacakYazisi = etSacakYazisiKarpuz.text.toString()
                val biyeRengi = etBiyeRengiKarpuz.text.toString()
                val seritRengi = etSeritRengiKarpuz.text.toString()
                val kumasRengi = etKumasRengiKarpuz.text.toString()

                var tenteData = SiparisData.KarpuzData(genislik,yukseklik,kumasRengi,sacakTuru,sacakYazisi,biyeRengi,seritRengi,siparisKey)

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
        etSacakTürüKarpuz.visibility = View.GONE
        var sacakTurleri = arrayOf("Düz", "Dalgalı")
        spSacakTuruKarpuz.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sacakTurleri)
        spSacakTuruKarpuz.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                sacakTuru = "Düz"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sacakTuru = sacakTurleri[position]
            }

        }
    }
}