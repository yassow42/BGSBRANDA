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
import kotlinx.android.synthetic.main.activity_semsiye.*


class SemsiyeActivity : AppCompatActivity() {
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
            var siparisData = SiparisData(siparisNotu, siparisTuru, 0, siparisKey, musteriKey, userID, null, null, null, null,
                null, null, null, null, null, downloadUrl1, downloadUrl2, downloadUrl3, downloadUrl4
            )


            ref.child("Siparisler").child(siparisKey).setValue(siparisData).addOnCompleteListener {
                ref.child("Siparisler").child(siparisKey).child("siparis_girme_zamani").setValue(ServerValue.TIMESTAMP)


                val genislik = etGenislikSemsiye.text.toString()
                val sacakYazisi = etSacakYazisiSemsiye.text.toString()
                val kumasRengi = etKumasRengiSemsiye.text.toString()
                var eksikler = etEksiklerSemsiye.text.toString()

                var tenteData = SiparisData.SemsiyeData(semsiyeTuru, genislik, kumasRengi, sacakYazisi, siparisKey,eksikler)

                ref.child("Siparisler").child(siparisKey).child("tenteData").setValue(tenteData).addOnCompleteListener {
                    val intent = Intent(this, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "Sipariş Girilemedi", Toast.LENGTH_LONG).show()
                    ref.child("Siparisler").child(siparisKey).removeValue()

                }


            }
        }

        foto1Semsiye.setOnClickListener {
            var intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, RESIM_SEC1)
        }
        foto2Semsiye.setOnClickListener {
            var intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, RESIM_SEC2)
        }
        foto3Semsiye.setOnClickListener {
            var intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, RESIM_SEC3)
        }
        foto4Semsiye.setOnClickListener {
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
            Picasso.get().load(profilPhotoUri1).resize(size, size).into(foto1Semsiye)




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
            Picasso.get().load(profilPhotoUri2).resize(size, size).into(foto2Semsiye)

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
            Picasso.get().load(profilPhotoUri3).resize(size, size).into(foto3Semsiye)

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
            Picasso.get().load(profilPhotoUri4).resize(size, size).into(foto4Semsiye)


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
        etSemsiyeTuru.visibility = View.GONE
        var semsiyeTurleri = arrayOf("Kare Klasik","Kare Profesyonel","Yuvarlak Klasik","Yuvarlak Profesyonel")
        spSemsiyeTuru.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semsiyeTurleri)
        spSemsiyeTuru.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                semsiyeTuru = "Kare Klasik"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                semsiyeTuru = semsiyeTurleri[position]
            }

        }
    }
}