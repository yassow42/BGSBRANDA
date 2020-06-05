package com.creativeoffice.bgsbranda.SiparisTurleriActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.creativeoffice.bgsbranda.Activity.SiparislerActivity
import com.creativeoffice.bgsbranda.Datalar.SiparisData
import com.creativeoffice.bgsbranda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_tente.*

class TenteActivity : AppCompatActivity() {
    val ref = FirebaseDatabase.getInstance().reference
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var kullaniciAdi: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tente)
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        var musteriKey = intent.getStringExtra("musteriKey")

        tvSiparisEkle.setOnClickListener {

            var siparisNotu = etSiparisNotu.text.toString()
            var siparisKey = ref.child("Siparisler").push().key.toString()
            var siparisTuru = "Tente"
            var siparisData = SiparisData(siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID,null,
                null,null,null, null, null,
                null,null,null)


            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)
                val cephe = etCephe.text.toString()
                val acilim = etAcilim.text.toString()
                val kumasKodu = etKumasKodu.text.toString()
                val sacak = etSacakTuru.text.toString()
                val sacakYazisi = etSacakYazisi.text.toString()
                val sanzimanYonu = etSanziman.text.toString()
                val profilRengi = etProfilRengi.text.toString()
                var tenteData = SiparisData.TenteData(cephe, acilim, kumasKodu, sacak, sacakYazisi, sanzimanYonu, profilRengi, siparisKey)

                ref.child("Siparisler").child(siparisKey).child("tenteData").setValue(tenteData).addOnCompleteListener {
                    val intent = Intent(this, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "Sipariş Girilemedi",Toast.LENGTH_LONG).show()
                    ref.child("Siparisler").child(siparisKey).removeValue()

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
            }
        })
    }
}
