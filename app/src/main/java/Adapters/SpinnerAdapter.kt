package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ellimonysucausa.R

class SpinnerAdapter: BaseAdapter {
    private var context: Context
    private var lista: ArrayList<String>

    constructor(context: Context, lista: ArrayList<String>) : super() {
        this.context = context
        this.lista = lista
    }


    override fun getCount(): Int {
        return lista.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rootView = LayoutInflater.from(context).inflate(R.layout.item_producto_spinner, p2, false)
        val tvCategoria = rootView.findViewById<TextView>(R.id.tvCategoria)
        tvCategoria.text = lista[p0]
        return rootView
    }
}