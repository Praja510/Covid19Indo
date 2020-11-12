package com.example.covidindogokil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_province.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterProvince (private val province : ArrayList<DataItem>, private val clickListener : (DataItem) -> Unit) :
    RecyclerView.Adapter<ProvinceViewHolder>(), Filterable{

    var countryfirstlist = ArrayList<DataItem>()

    init {
        countryfirstlist = province
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_province, parent, false)
        return ProvinceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countryfirstlist.size
    }

    override fun onBindViewHolder(holder: ProvinceViewHolder, position: Int) {
        holder.bind(countryfirstlist[position], clickListener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                countryfirstlist = if (charSearch.isEmpty()) {
                    province
                } else {
                    val resultList = ArrayList<DataItem>()
                    for (row in province) {
                        val search = row.provinsi?.toLowerCase(Locale.ROOT) ?: ""
                        if (search.contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)

                        }
                    }
                    resultList
                }
                val filterResult = FilterResults()
                filterResult.values = countryfirstlist
                return filterResult
            }

            override fun publishResults(p0: CharSequence?, result: FilterResults?) {
                countryfirstlist = result?.values as ArrayList<DataItem>
                notifyDataSetChanged()
            }

        }
    }
}

class ProvinceViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
    fun bind (provinsi : DataItem, clickListener : (DataItem) -> Unit) {
        val name_countryrv : TextView = itemView.tv_provinceName
        val country_totalCase : TextView = itemView.tv_provinceCase
        val country_totalRecovered : TextView = itemView.tv_provinceRecovered
        val country_totalDeath : TextView = itemView.tv_provinceDeath

        val formatter : NumberFormat = DecimalFormat("#,###")

        name_countryrv.tv_provinceName.text = provinsi.provinsi
        country_totalCase.tv_provinceCase.text = formatter.format(provinsi.kasusPosi?.toDouble())
        country_totalRecovered.tv_provinceRecovered.text = formatter.format(provinsi.kasusSemb?.toDouble())
        country_totalDeath.tv_provinceDeath.text = formatter.format(provinsi.kasusMeni?.toDouble())

    }
}
