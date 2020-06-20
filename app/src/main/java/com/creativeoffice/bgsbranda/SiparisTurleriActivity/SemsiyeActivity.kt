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

import kotlinx.android.synthetic.main.activity_semsiye.*
import kotlinx.android.synthetic.main.activity_semsiye.tvSiparisEkle


class SemsiyeActivity : AppCompatActivity() {
    val ref = FirebaseDatabase.getInstance().reference
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    var semsiyeTuru = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semsiye)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        var musteriKey = intent.getStringExtra("musteriKey")
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        spinnerAyarlari()
        siparisEkle(musteriKey)

    }

    private fun siparisEkle(musteriKey: String?) {
        tvSiparisEkle.setOnClickListener {
            var siparisNotu = etSiparisNotuSemsiye.text.toString()
            var siparisKey = ref.child("Siparisler").push().key.toString()
            var siparisTuru = "Şemsiye"
            var siparisData = SiparisData(siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID, null, null, null, null, null, null, null, null, null)

            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)


                val genislik = etGenislikSemsiye.text.toString()
                val sacakYazisi = etSacakYazisiSemsiye.text.toString()
                val kumasRengi = etKumasRengiSemsiye.text.toString()

                var tenteData = SiparisData.SemsiyeData(semsiyeTuru, genislik, kumasRengi, sacakYazisi, siparisKey)

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
        etSemsiyeTuru.visibility = View.GONE
        var semsiyeTurleri = arrayOf("Kare", "Yuvarlak")
        spSemsiyeTuru.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semsiyeTurleri)
        spSemsiyeTuru.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                semsiyeTuru = "Kare"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                semsiyeTuru = semsiyeTurleri[position]
            }

        }
    }
}