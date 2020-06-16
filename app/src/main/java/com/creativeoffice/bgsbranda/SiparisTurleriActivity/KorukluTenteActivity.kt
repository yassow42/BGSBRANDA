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
import kotlinx.android.synthetic.main.activity_koruklu_tente.*

class KorukluTenteActivity : AppCompatActivity() {
    val ref = FirebaseDatabase.getInstance().reference
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    var sacakTuru = ""
    var seritRengiAdeti = "Yok"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_koruklu_tente)
        var musteriKey = intent.getStringExtra("musteriKey")
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        siparisEkle(musteriKey)
        spinnerAyarlari()

    }

    private fun spinnerAyarlari() {
        etSacakTuruKoruklu.visibility = View.GONE
        var sacakTurleri = arrayOf("Düz", "Dalgalı")
        spSacakTuruKoruk.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sacakTurleri)
        spSacakTuruKoruk.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                sacakTuru = "Düz"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sacakTuru = sacakTurleri[position]
            }
        }

        etSeritRengiAdeti.visibility = View.GONE
        chSeritRengiVarMi.setOnClickListener {
            if (chSeritRengiVarMi.isChecked) {
                etSeritRengiAdeti.visibility = View.VISIBLE
            } else {
                etSeritRengiAdeti.visibility = View.GONE
            }
        }


    }

    private fun siparisEkle(musteriKey: String?) {
        tvSiparisEkle.setOnClickListener {

            var siparisNotu = etSiparisNotu.text.toString()
            var siparisKey = ref.child("Siparisler").push().key.toString()
            var siparisTuru = "Körüklü Tente"
            var siparisData = SiparisData(
                siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID,
                null, null, null, null,
                null, null, null, null, null
            )


            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)
                val cephe = etCephe.text.toString()
                val acilim = etAcilim.text.toString()
                val kumasKodu = etKumasKodu.text.toString()
                val sacakBiyesiRengi = etSacakBiyesRengi.text.toString()
                val sacakYazisi = etSacakYazisi.text.toString()
                val ipYonu = etİpYonu.text.toString()
                val profilRengi = etProfilRengi.text.toString()

                if (!etSeritRengiAdeti.text.isNullOrEmpty()){
                    seritRengiAdeti =  etSeritRengiAdeti.text.toString()
                }


                var tenteData = SiparisData.KorukluTenteData(cephe, acilim, kumasKodu, sacakTuru, sacakBiyesiRengi, seritRengiAdeti, sacakYazisi, ipYonu, profilRengi, siparisKey)

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
}
