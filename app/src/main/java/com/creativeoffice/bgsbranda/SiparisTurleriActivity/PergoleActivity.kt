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
import kotlinx.android.synthetic.main.activity_pergole.*
import kotlinx.android.synthetic.main.activity_pergole.etSiparisNotu
import kotlinx.android.synthetic.main.activity_pergole.tvSiparisEkle

class PergoleActivity : AppCompatActivity() {
    val ref = FirebaseDatabase.getInstance().reference
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String

    var pergoleTuru = ""
    var etrafindaCamVarmi = ""
    var pergoleCesidi = ""
    var motorYonu = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pergole)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        var musteriKey = intent.getStringExtra("musteriKey")
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid


        spinnerAyarlari()
        siparisEkle(musteriKey)
    }

    private fun siparisEkle(musteriKey: String?) {
        tvSiparisEkle.setOnClickListener {
            var siparisNotu = etSiparisNotu.text.toString()
            var siparisKey = ref.child("Siparisler").push().key.toString()
            var siparisTuru = "Pergole"
            var siparisData = SiparisData(siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID, null, null, null, null, null, null, null, null, null)

            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)


                val cephe = etCephePergole.text.toString()
                val acilim = etAcilimPergole.text.toString()
                val arkaYukseklik =  etArkaYukseklik.text.toString()
                val onYukseklik =  etOnYukseklik.text.toString()
                val kumasRengi = etKumasRengi.text.toString()
                val profilRengi = etProfilRengiPergole.text.toString()
                val led = etLed.text.toString()
                val camkaydiOlcusu = etCamKaydi.text.toString()


                var tenteData = SiparisData.PergoleData(pergoleTuru,cephe, acilim,arkaYukseklik,onYukseklik, kumasRengi, profilRengi, led,motorYonu,camkaydiOlcusu, pergoleCesidi,etrafindaCamVarmi,siparisKey)

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
        etPergoleTuru.visibility = View.GONE
        var pergoleTurleri = arrayOf("Düz","Oval", "Aliminyum Çatı")
        spPergoleTuru.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pergoleTurleri)
        spPergoleTuru.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                pergoleTuru = "Düz"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pergoleTuru = pergoleTurleri[position]
            }

        }


        etCamKaydi.visibility=View.GONE
        chCamKaydi.setOnClickListener {
            if (chCamKaydi.isChecked) {
                etCamKaydi.visibility = View.VISIBLE
            } else {
                etCamKaydi.visibility = View.GONE
            }
        }

        etCamVarmi.visibility=View.GONE
        chCamVarMiVar.setOnClickListener {
            if (chCamVarMiVar.isChecked) {
                etrafindaCamVarmi = "Var"
            } else {
                etrafindaCamVarmi = "Yok"

            }
        }

        etPergoleCesidi.visibility = View.GONE
        var pergoleCesitleri = arrayOf("Ayaklı","Askılı")
        spPergoleCesidi.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pergoleCesitleri)
        spPergoleCesidi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                pergoleCesidi = "Ayaklı"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pergoleCesidi = pergoleCesitleri[position]
            }

        }

        etMotorYonu.visibility = View.GONE
        var motorYonCesitleri = arrayOf("Sağ","Sol")
        spMotorYonu.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, motorYonCesitleri)
        spMotorYonu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                motorYonu = "Sağ"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                motorYonu = motorYonCesitleri[position]
            }

        }




    }
}