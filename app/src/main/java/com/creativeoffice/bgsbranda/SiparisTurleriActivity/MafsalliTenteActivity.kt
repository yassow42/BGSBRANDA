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
import kotlinx.android.synthetic.main.activity_koruklu_tente.*
import kotlinx.android.synthetic.main.activity_tente_mafsalli.*
import kotlinx.android.synthetic.main.activity_tente_mafsalli.etAcilim
import kotlinx.android.synthetic.main.activity_tente_mafsalli.etCephe
import kotlinx.android.synthetic.main.activity_tente_mafsalli.etKumasKodu
import kotlinx.android.synthetic.main.activity_tente_mafsalli.etProfilRengi
import kotlinx.android.synthetic.main.activity_tente_mafsalli.etSacakYazisi
import kotlinx.android.synthetic.main.activity_tente_mafsalli.etSiparisNotu
import kotlinx.android.synthetic.main.activity_tente_mafsalli.tvSiparisEkle

class MafsalliTenteActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_tente_mafsalli)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        var musteriKey = intent.getStringExtra("musteriKey")


        setupKullaniciAdi()
        siparisEkle(musteriKey)
        spinnerAyarlari()
    }

    private fun spinnerAyarlari() {
        etSacakTuruMafsalli.visibility = View.GONE
        var sacakturlerı = arrayOf("Düz", "Dalgalı")
        spSacakTuruMafsalli.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sacakturlerı)
        spSacakTuruMafsalli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                sacakTuru = "Düz"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sacakTuru = sacakturlerı[position]
            }

        }

        etSanziman.visibility = View.GONE
        var sanzımanYonleri = arrayOf("Sağ", "Sol")
        spSanzimanYonuMafsalli.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sanzımanYonleri)
        spSanzimanYonuMafsalli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                sanzimanYonu = "Sağ"

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sanzimanYonu = sanzımanYonleri[position]
            }
        }

        etAyakTuruMafsalli.visibility = View.GONE
        var ayakTurleri = arrayOf("Duvar", "Tavan", "Özel")
        spAyakYonuMafsalli.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ayakTurleri)
        spAyakYonuMafsalli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                ayakTuru = "Duvar"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ayakTuru = ayakTurleri[position]
            }

        }

        etMotor.visibility = View.GONE
        var motorVar = arrayOf("Yok", "Var")
        spMotorMafsalli.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, motorVar)
        spMotorMafsalli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                motorVarMi = "Yok"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                motorVarMi = motorVar[position]
            }

        }
        etMantolamaMafsalli.visibility = View.GONE
        var mantolama = arrayOf("Var", "Yok")
        spMantolamaMafsalli.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mantolama)
        spMantolamaMafsalli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mantolamaVar = "Yok"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mantolamaVar = mantolama[position]
            }

        }

    }

    private fun siparisEkle(musteriKey: String?) {
        tvSiparisEkle.setOnClickListener {

            var siparisNotu = etSiparisNotu.text.toString()
            var siparisKey = ref.child("Siparisler").push().key.toString()
            var siparisTuru = "Mafsallı Tente"
            var siparisData = SiparisData(
                siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID, null,
                null, null, null, null, null,
                null, null, null
            )


            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)
                val cephe = etCephe.text.toString()
                val acilim = etAcilim.text.toString()
                val kumasKodu = etKumasKodu.text.toString()
                val sacakYazisi = etSacakYazisi.text.toString()

                val profilRengi = etProfilRengi.text.toString()
                var tenteData = SiparisData.MafsallıTente(cephe, acilim, kumasKodu, sacakTuru, sacakYazisi, motorVarMi, sanzimanYonu, ayakTuru, mantolamaVar, profilRengi, siparisKey)

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
