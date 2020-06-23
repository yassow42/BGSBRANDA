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
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_wintent.*

class WintentActivity : AppCompatActivity() {

    val ref = FirebaseDatabase.getInstance().reference
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var kullaniciAdi: String

    var sacakTuru = ""
    var sanzimanYonu = ""
    var ayakTuru = ""
    var motorVarMi = ""
    var mantolamaVar = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wintent)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        var musteriKey = intent.getStringExtra("musteriKey")


        setupKullaniciAdi()
        siparisEkle(musteriKey)
        spinnerAyarlari()
    }

    private fun spinnerAyarlari() {
        etSacakTuruWintend.visibility = View.GONE
        var sacakturlerı = arrayOf("Düz", "Dalgalı")
        spSacakTuruWintend.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sacakturlerı)
        spSacakTuruWintend.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                sacakTuru = "Düz"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sacakTuru = sacakturlerı[position]
            }

        }

        etSanzimanWintend.visibility = View.GONE
        var sanzımanYonleri = arrayOf("Sağ", "Sol")
        spSanzimanYonuWintend.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sanzımanYonleri)
        spSanzimanYonuWintend.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                sanzimanYonu = "Sağ"

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sanzimanYonu = sanzımanYonleri[position]
            }
        }

        etAyakTuruWintend.visibility = View.GONE
        var ayakTurleri = arrayOf("Duvar", "Tavan", "Özel")
        spAyakTuruWintend.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ayakTurleri)
        spAyakTuruWintend.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                ayakTuru = "Duvar"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ayakTuru = ayakTurleri[position]
            }

        }

        etMotorWintend.visibility = View.GONE
        var motorVar = arrayOf("Yok", "Var")
        spMotorWintend.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, motorVar)
        spMotorWintend.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                motorVarMi = "Yok"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                motorVarMi = motorVar[position]
            }

        }
        etMantolamaWintend.visibility = View.GONE
        var mantolama = arrayOf("Var", "Yok")
        spMantolamaWintend.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mantolama)
        spMantolamaWintend.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mantolamaVar = "Yok"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mantolamaVar = mantolama[position]
            }

        }

    }

    private fun siparisEkle(musteriKey: String?) {
        tvSiparisEkleWintend.setOnClickListener {

            var siparisNotu = etSiparisNotuWintend.text.toString()
            var siparisKey = ref.child("Siparisler").push().key.toString()
            var siparisTuru = "Wintend"
            var siparisData = SiparisData(
                siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID, null,
                null, null, null, null, null,
                null, null, null
            )


            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)
                val cephe = etCepheWintend.text.toString()
                val kolBoyu = etKolBoyuWintend.text.toString()
                val kumasKodu = etKumasKoduWintend.text.toString()
                val sacakYazisi = etSacakYazisiWintend.text.toString()

                val profilRengi = etProfilRengiWintend.text.toString()
                var tenteData = SiparisData.Wintend(cephe, kolBoyu, kumasKodu, sacakTuru, sacakYazisi, motorVarMi, sanzimanYonu, ayakTuru, mantolamaVar, profilRengi, siparisKey)

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
