 package com.example.pedidosya.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedidosya.R
import com.example.pedidosya.adapters.RestaurantsRecyclerAdapter
import com.example.pedidosya.fragments.LoadingDialogFragment
import com.example.pedidosya.presenter.MainActivityPresenter
import com.example.pedidosya.repository.Responses.RestaurantsResponse
import com.example.pedidosya.utils.DialogControl
import com.example.pedidosya.utils.DialogGeneral
import com.example.pedidosya.utils.LocationGps
import com.example.pedidosya.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.lang.NullPointerException

 class MainActivity : AppCompatActivity(), MainActivityPresenter.View, LocationGps.location {

     lateinit var mainActivityPresenter : MainActivityPresenter
     protected lateinit  var dialogControl : DialogControl
     private lateinit var locationGps: LocationGps
     var adapter: RestaurantsRecyclerAdapter? = null
     var offset : Int = 0
     lateinit var dialogFragment : DialogFragment
     var loading = false
     var actualLocation : String = ""
     companion object {
         var max = 20
         var minInvisibleUntilLoad = 3
     }


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Listado de Restaurantes")

        dialogControl = LoadingDialogFragment()
        recyclerRestaurants.layoutManager = LinearLayoutManager(applicationContext)
        locationGps = LocationGps(this,applicationContext,this)
        mainActivityPresenter = MainActivityPresenter(this)
        mainActivityPresenter.getToken(applicationContext.getString(R.string.clientid),applicationContext.getString(R.string.clientpassword))

         recyclerRestaurants.addOnScrollListener(object : RecyclerView.OnScrollListener(){

             override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                 super.onScrolled(recyclerView, dx, dy)
                 val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                 val totalitem = linearLayoutManager.itemCount
                 val lastItemVisible = linearLayoutManager.findLastVisibleItemPosition()
                 if (!loading and (totalitem <= (lastItemVisible+ minInvisibleUntilLoad))){
                     offset += 20
                     mainActivityPresenter.getRestaurants(SharedPreferencesUtils.getToken(applicationContext),actualLocation,1,max,offset)
                     loading = true
                 }


             }


         })

         fab.setOnClickListener {

             val intent = Intent(this,MapsActivity::class.java)
             startActivityForResult(intent,MapsActivity.requestCode)

         }


    }



     override fun onLoad() {
        dialogControl.showDialogFragment(supportFragmentManager)
     }

     override fun onTokenSuccess(token : String) {
         SharedPreferencesUtils.saveToken(applicationContext,token)
         locationGps.getLastKnownLocation()
     }

     override fun onError() {
         //show dialog error
         dialogControl.dismissDialogFragment()
         dialogFragment = DialogGeneral(getString(R.string.general_error_text))
         dialogFragment.show(supportFragmentManager,getString(R.string.dialog))
     }

     override fun onRestaurantsSuccess(response: RestaurantsResponse) {
         dialogControl.dismissDialogFragment()
         if (recyclerRestaurants.visibility == View.GONE) {
             recyclerRestaurants.visibility = View.VISIBLE
             no_data_layout.visibility = View.GONE
         }
         if (adapter != null && offset > 0) {
             adapter?.restaurants?.data?.remove(null)
             adapter?.restaurants?.data?.addAll(response.data!!)
             SharedPreferencesUtils.saveRestaurants(applicationContext,adapter?.restaurants?.data!!)
             adapter?.notifyDataSetChanged()
         }
         else   {
             SharedPreferencesUtils.saveRestaurants(applicationContext,response.data!!)
             adapter = RestaurantsRecyclerAdapter(applicationContext,response)
             recyclerRestaurants.adapter = adapter
         }
         loading = false

     }


     override fun onErrorRestaurantsResponse() {
         dialogControl.dismissDialogFragment()
         dialogFragment = DialogGeneral(getString(R.string.error_restaurants_text))
         dialogFragment.show(supportFragmentManager,getString(R.string.dialog))


     }

     override fun noRestaurantsData() {
         dialogControl.dismissDialogFragment()
        recyclerRestaurants.visibility = View.GONE
        no_data_layout.visibility = View.VISIBLE
     }

     override fun successLastKnownLocation(location: Location) {
         val locationString : String = location.latitude.toString()+","+location.longitude.toString()
         actualLocation = locationString
         mainActivityPresenter.getRestaurants(SharedPreferencesUtils.getToken(applicationContext),locationString,1,
                 max,offset)
     }

     override fun onErrorFindingLocation(error: String) {
         dialogControl.dismissDialogFragment()
         dialogFragment = DialogGeneral(getString(R.string.error_location_text))
         dialogFragment.show(supportFragmentManager,getString(R.string.dialog))


     }

     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
         if (requestCode == LocationGps.REQUEST_PERMISSION_LOCATION) {
             if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 locationGps.getLastKnownLocation()
             }else{
                Toast.makeText(applicationContext,getString(R.string.not_granted_permission_text),Toast.LENGTH_SHORT).show()
             }
         }else{
             Toast.makeText(applicationContext,getString(R.string.not_granted_permission_text),Toast.LENGTH_SHORT).show()
         }
     }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         if (requestCode == MapsActivity.requestCode){
             if (resultCode == MapsActivity.resultActivity){
                 val string = data?.getStringExtra(getString(R.string.location))
                 offset = 0
                 try {
                     actualLocation = string!!
                     SharedPreferencesUtils.cleanRestaurants(applicationContext)
                     mainActivityPresenter.getRestaurants(SharedPreferencesUtils.getToken(applicationContext),string,1,
                         max,offset)
                 }catch (e : NullPointerException){
                     Log.w("MainActivity","Location in activity result null")
                 }

             }
         }
     }

     override fun onDestroy() {
         super.onDestroy()
         SharedPreferencesUtils.cleanData(applicationContext)
     }


 }
