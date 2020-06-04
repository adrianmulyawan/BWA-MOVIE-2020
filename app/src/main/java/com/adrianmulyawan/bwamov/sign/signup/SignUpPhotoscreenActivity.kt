package com.adrianmulyawan.bwamov.sign.signup

import android.annotation.SuppressLint
import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.adrianmulyawan.bwamov.home.HomeActivity
import com.adrianmulyawan.bwamov.R
import com.adrianmulyawan.bwamov.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*

class SignUpPhotoscreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd: Boolean = false
    lateinit var filePath: Uri

    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        // Hasil pelemparan data dari SignUpActivity hasil intent di fungsi checkingUsername()
        // Dan ditampilkan sesuai nama
        tv_hello.text = "Selamat Datang\n"+intent.getStringExtra("nama")

        iv_add.setOnClickListener {
            if (statusAdd) { // untuk mengecek sudah pernah upload photo / belum
                // jika benar belum pernah upload photo ->
                statusAdd = false
                // Kalau belum pernah btn_save (simpan dan lanjutkan akan dihilangkan)
                btn_save.visibility = View.INVISIBLE
                // icon add akan diubah menjadi btn_upload
                iv_add.setImageResource(R.drawable.ic_btn_upload_photo)
                // dan photo profilenya menjadi userpic yang bulat
                iv_profile.setImageResource(R.drawable.user_pic)

            } else {
                // Jika salah dia akan mencari function kamera
                // Jika tidak diizinkan akan stuck di fungsi kamera
                // Jika diizinkan, akan dialihkan fungsi onPermissionGranted()
                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(this)
                    .check()
            }
        }

        btn_home.setOnClickListener {

            finishAffinity()

            val intent = Intent(this@SignUpPhotoscreenActivity,
                HomeActivity::class.java)
            startActivity(intent)
        }

        btn_save.setOnClickListener {
            if (filePath != null) {
                val progressDialog = ProgressDialog(this)
                // menampilkan progress dialog uploading
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                // dan disimpan di Firebase/Storage/images/
                val ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this@SignUpPhotoscreenActivity, "Uploaded", Toast.LENGTH_SHORT).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValue("url", it.toString())
                        }

                        finishAffinity()
                        val intent = Intent(this@SignUpPhotoscreenActivity,
                            HomeActivity::class.java)
                        startActivity(intent)

                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(this@SignUpPhotoscreenActivity, "Failed " + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                     // Fungsi untuk menampilkan progress
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                    }
            }

        }
    }

    // Jika diizinkan untuk mengakses kamera dia akan membuka fungsi bawaan kamera hp
    // dan jika berhasil akan ditampilkan kedalam fungsi onActivityCreated
    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        //To change body of created functions use File | Settings | File Templates.
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {
        //To change body of created functions use File | Settings | File Templates.
    }

    // misalnya tidak di aktifkan akan muncul Toast berikut ini
    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan photo profile", Toast.LENGTH_LONG).show()
    }

    // misalnya menekan tombol back akan menampilkan Toast di fungsi ini
    override fun onBackPressed() {
        Toast.makeText(this, "Tergesah? Klik Tombol Upload Nanti Saja", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            var bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true

            filePath = data.getData()!!
            // akan ditampilkan disini hasil photo yang diambil
            Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            // dan btn_save ditampilkan
            // dan tombol add akan diganti dengan hapus photo
            btn_save.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete_photo)
        }
    }
}
