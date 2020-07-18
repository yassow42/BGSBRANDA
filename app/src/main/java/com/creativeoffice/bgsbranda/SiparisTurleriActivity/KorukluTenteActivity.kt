package com.creativeoffice.bgsbranda.SiparisTurleriActivity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_koruklu_tente.*

class KorukluTenteActivity : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog

    var profilPhotoUri1: Uri? = null
    var profilPhotoUri2: Uri? = null
    var profilPhotoUri3: Uri? = null
    var profilPhotoUri4: Uri? = null

    var downloadUrl1: String? = null
    var downloadUrl2: String? = null
    var downloadUrl3: String? = null
    var downloadUrl4: String? = null

    val RESIM_SEC1 = 1
    val RESIM_SEC2 = 2
    val RESIM_SEC3 = 3
    val RESIM_SEC4 = 4

    val ref = FirebaseDatabase.getInstance().reference
    var siparisKey = ref.child("Siparisler").push().key.toString()
    var stRef = FirebaseStorage.getInstance().reference


    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    var sacakTuru = ""
    var seritRengiAdeti = "Yok"
    var ipYonu = ""
    var ayakTuru = ""

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





    private fun siparisEkle(musteriKey: String?) {
        tvSiparisEkle.setOnClickListener {

            var siparisNotu = etSiparisNotu.text.toString()
            var siparisKey = ref.child("Siparisler").push().key.toString()
            var siparisTuru = "Körüklü Tente"
            var siparisData = SiparisData(
                siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID,
                null, null, null, null,
                null, null, null, null, null, downloadUrl1, downloadUrl2, downloadUrl3, downloadUrl4
            )


            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)
                val cephe = etCephe.text.toString()
                val acilim = etAcilim.text.toString()
                val kumasKodu = etKumasKodu.text.toString()
                val sacakBiyesiRengi = etSacakBiyesRengi.text.toString()
                val sacakYazisi = etSacakYazisi.text.toString()
                val profilRengi = etProfilRengi.text.toString()
                var eksikler = etEksiklerKoruklu.text.toString()

                val seritRengi = etSeritRengi.text.toString()
                if (!etSeritRengiAdeti.text.isNullOrEmpty()) {
                    seritRengiAdeti = etSeritRengiAdeti.text.toString()
                }


                var tenteData = SiparisData.KorukluTenteData(cephe, acilim, kumasKodu, sacakTuru, sacakBiyesiRengi, seritRengi, seritRengiAdeti, sacakYazisi, ipYonu, profilRengi, ayakTuru, siparisKey,eksikler)

                ref.child("Siparisler").child(siparisKey).child("tenteData").setValue(tenteData).addOnCompleteListener {
                    val intent = Intent(this, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "Sipariş Girilemedi", Toast.LENGTH_LONG).show()
                    ref.child("Siparisler").child(siparisKey).removeValue()

                }


            }

        }

        foto1Koruklu.setOnClickListener {
            var intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, RESIM_SEC1)
        }
        foto2Koruklu.setOnClickListener {
            var intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, RESIM_SEC2)
        }
        foto3Koruklu.setOnClickListener {
            var intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, RESIM_SEC3)
        }
        foto4Koruklu.setOnClickListener {
            var intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, RESIM_SEC4)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var size = 250
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESIM_SEC1 && resultCode == AppCompatActivity.RESULT_OK && data!!.data != null) {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Resim Yükleniyor... Lütfen bekleyin...")
            progressDialog.setCancelable(false)
            progressDialog.show()


            profilPhotoUri1 = data.data
            Picasso.get().load(profilPhotoUri1).resize(size, size).into(foto1Koruklu)




            if (profilPhotoUri1 != null) {
                stRef.child(siparisKey).child("foto1").putFile(profilPhotoUri1!!) // burada fotografı kaydettik veritabanına.
                    .addOnSuccessListener { UploadTask ->
                        UploadTask.storage.downloadUrl.addOnSuccessListener { itUri ->
                            downloadUrl1 = itUri.toString()
                            Toast.makeText(this, "Resim 1 Yüklendi", Toast.LENGTH_LONG).show()

                            progressDialog.dismiss()

                        }
                    }
            }

        }
        if (requestCode == RESIM_SEC2 && resultCode == AppCompatActivity.RESULT_OK && data!!.data != null) {

            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Resim Yükleniyor... Lütfen bekleyin...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            profilPhotoUri2 = data.data
            Picasso.get().load(profilPhotoUri2).resize(size, size).into(foto2Koruklu)

            if (profilPhotoUri2 != null) {
                stRef.child(siparisKey).child("foto2").putFile(profilPhotoUri2!!) // burada fotografı kaydettik veritabanına.
                    .addOnSuccessListener { UploadTask ->
                        UploadTask.storage.downloadUrl.addOnSuccessListener { itUri ->
                            downloadUrl2 = itUri.toString()
                            Toast.makeText(this, "Resim 2 Yüklendi", Toast.LENGTH_LONG).show()

                            progressDialog.dismiss()

                        }
                    }
            }


        }
        if (requestCode == RESIM_SEC3 && resultCode == AppCompatActivity.RESULT_OK && data!!.data != null) {

            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Resim Yükleniyor... Lütfen bekleyin...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            profilPhotoUri3 = data.data
            Picasso.get().load(profilPhotoUri3).resize(size, size).into(foto3Koruklu)

            if (profilPhotoUri3 != null) {
                stRef.child(siparisKey).child("foto3").putFile(profilPhotoUri3!!) // burada fotografı kaydettik veritabanına.
                    .addOnSuccessListener { UploadTask ->
                        UploadTask.storage.downloadUrl.addOnSuccessListener { itUri ->
                            downloadUrl3 = itUri.toString()
                            Toast.makeText(this, "Resim 3 Yüklendi", Toast.LENGTH_LONG).show()

                            progressDialog.dismiss()
                        }
                    }
            }


        }
        if (requestCode == RESIM_SEC4 && resultCode == AppCompatActivity.RESULT_OK && data!!.data != null) {

            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Resim Yükleniyor... Lütfen bekleyin...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            profilPhotoUri4 = data.data
            Picasso.get().load(profilPhotoUri4).resize(size, size).into(foto4Koruklu)


            if (profilPhotoUri4 != null) {
                stRef.child(siparisKey).child("foto4").putFile(profilPhotoUri4!!) // burada fotografı kaydettik veritabanına.
                    .addOnSuccessListener { UploadTask ->
                        UploadTask.storage.downloadUrl.addOnSuccessListener { itUri ->
                            downloadUrl4 = itUri.toString()
                            Toast.makeText(this, "Resim 4 Tamam", Toast.LENGTH_LONG).show()
                            progressDialog.dismiss()


                        }
                    }
            }

        }


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
                etSeritRengiAdeti.text.clear()
            }
        }

        etIpYonu.visibility = View.GONE
        var ipYonleri = arrayOf("Sağ", "Sol")
        spIpYonu.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ipYonleri)
        spIpYonu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                ipYonu = "Sağ"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ipYonu = ipYonleri[position]
            }

        }

        etAyakTuru.visibility = View.GONE
        var ayakTurleri = arrayOf("Duvar", "Tavan", "Özel")
        spAyakYonu.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ayakTurleri)
        spAyakYonu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                ayakTuru = "Duvar"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ayakTuru = ayakTurleri[position]
            }

        }


    }

}
