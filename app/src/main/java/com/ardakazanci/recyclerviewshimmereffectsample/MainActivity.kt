package com.ardakazanci.recyclerviewshimmereffectsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.ardakazanci.recyclerviewshimmereffectsample.adapter.MainAdapter
import com.ardakazanci.recyclerviewshimmereffectsample.data.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidNetworking.initialize(applicationContext)
        setupUI()
        setupAPICall()

    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupAPICall() {

        AndroidNetworking.get("https://5e510330f2c0d300147c034c.mockapi.io/users")
            .build()
            .getAsObjectList(User::class.java, object : ParsedRequestListener<List<User>> {
                override fun onResponse(users: List<User>) {
                    Log.e("Response", "Success")
                    shimmerFrameLayout.stopShimmerAnimation()
                    shimmerFrameLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.addData(users)
                    adapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError) {
                    Log.e("Response", "Error")
                    shimmerFrameLayout.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayout.stopShimmerAnimation()
        super.onPause()
    }
}
