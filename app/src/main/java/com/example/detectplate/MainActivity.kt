package com.example.detectplate

import android.app.Activity
import android.content.Intent
import android.database.Cursor

import android.net.Uri
import android.opengl.Visibility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import com.github.dhaval2404.imagepicker.ImagePicker

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.get
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    lateinit var no:EditText
    lateinit var click_picture:ImageView
    lateinit var submit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        click_picture=findViewById(R.id.scan)
        submit = findViewById(R.id.submit)

        no=findViewById(R.id.car_no)
        click_picture.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        submit.setOnClickListener(){

            var carNo=no.text.toString()
            if (!carNo.isEmpty()) {
                val intent = Intent(this, DisplayOwnerDetail::class.java)
                intent.putExtra("carNo", carNo)
                startActivity(intent)
            }
        }

//        owner_detail.setOnClickListener {
//
//            val intent=Intent(this,DisplayOwnerDetail::class.java)
//            startActivity(intent)
//
//        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
//            image.setImageURI(uri)
            uploadFile(fileUri = uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    fun uploadFile(fileUri: Uri){

     //   val file= File(fileUri.getPath());
        val file= File(fileUri.path);
     val requestFile= RequestBody.create(contentResolver.getType(fileUri)?.let { it.toMediaTypeOrNull() },file)
    val body= MultipartBody.Part.createFormData("upload",file.name,requestFile)

        val call = RetrofitInstance.api.upload(body)
        call?.enqueue(object: Callback<ResponseBody?>{
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {

                if(response.isSuccessful){

                  val message= response.body()?.string()
                    if(message!=null){
                    val jsonObject=JSONObject(message)
                       val jsonarray= jsonObject.getJSONArray("results")
                      val no_plate=  jsonarray.getJSONObject(0).getString("plate")
                        no.setText(no_plate.uppercase())
                    }
                    //Toast.makeText(this@MainActivity,"${message}",Toast.LENGTH_LONG).show()
                    Log.v("Mainactivity","success ${message}")
//                    click_picture.visibility=View.GONE
//                    owner_detail.visibility=View.VISIBLE
//                    simple_text.visibility=View.VISIBLE

                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {

                Toast.makeText(this@MainActivity,"${t.message}",Toast.LENGTH_LONG).show()
              //  Log.v("${t.message}","failure")
                Log.v("${t.message}","failure")

            }
        })


    }
//    fun getPath(uri: Uri?): String? {
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = contentResolver.query(uri!!, projection, null, null, null) ?: return null
//        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor.moveToFirst()
//        val s = cursor.getString(column_index)
//        cursor.close()
//        return s
//    }
}