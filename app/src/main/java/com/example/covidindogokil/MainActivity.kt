package com.example.covidindogokil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covidindogokil.RetrofitBuilder.retrofit
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var ascending = true
    companion object {
        lateinit var adapters: AdapterProvince
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapters.filter.filter(p0)
                return false
            }
        })
        swipe_refresh.setOnRefreshListener {
            getProvince()
            swipe_refresh.isRefreshing = false
        }
        getProvince()
        initializedView()
    }

    private fun initializedView() {
        btnSequence.setOnClickListener {
            sequenceWithoutInternet(ascending)
        }
    }

    private fun sequenceWithoutInternet(ascending: Boolean) {
        rv_province.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            if (ascending) {
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = true
            } else {
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = false
            }

            adapter = adapter
        }
    }

    private fun getProvince() {
        val api = retrofit.create(APIService::class.java)
        api.getAllProvince().enqueue(object : Callback<ResponseProvince> {
            override fun onFailure(call: Call<ResponseProvince>, t: Throwable) {
                progress_bar.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<ResponseProvince>,
                response: Response<ResponseProvince>
            ) {
                if (response.isSuccessful) {
                    rv_province.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        progress_bar.visibility = View.GONE
                        adapters = AdapterProvince(
                            response.body()!!.data as ArrayList<DataItem>
                        ) {}
                        adapter = adapters
                    }
                }else {
                    progress_bar?.visibility = View.GONE
                }
            }

        })
    }
}