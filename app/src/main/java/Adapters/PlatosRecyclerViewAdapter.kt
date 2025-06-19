package Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ellimonysucausa.R
import entidades.producto

class PlatosRecyclerViewAdapter:RecyclerView.Adapter<PlatosRecyclerViewAdapter.MyViewHolder>(){

    private var listData = ArrayList<producto>()
    var onItemClick : ((producto) -> Unit)? = null

    fun setListData(data:ArrayList<producto>){
        this.listData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return MyViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val plato = listData[position]
        holder.bind(listData[position])
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(plato)
        }
    }

    class MyViewHolder(view:View): RecyclerView.ViewHolder(view){

        val tvNombreProducto = view.findViewById<TextView>(R.id.tvNombreProducto)
        val tvCategoriaProducto = view.findViewById<TextView>(R.id.tvCategoriaProducto)
        val tvPrecioProducto = view.findViewById<TextView>(R.id.tvPrecioProducto)
        val tvId = view.findViewById<TextView>(R.id.tvId)
        fun bind(data:producto){
            val formatedString = String.format("%.2f", data.precioProducto)
            tvNombreProducto.text = data.nombreProducto
            tvCategoriaProducto.text = data.categoria!!.nombreCategoria
            tvPrecioProducto.text = formatedString
            tvId.text = data.idProducto.toString()
        }
    }
}