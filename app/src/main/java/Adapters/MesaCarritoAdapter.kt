package Adapters

import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ellimonysucausa.R
import entidades.pedidoResponse

class MesaCarritoAdapter:RecyclerView.Adapter<MesaCarritoAdapter.MyViewHolder>(){
    private var listData = ArrayList<pedidoResponse>()
    var onItemClick : ((pedidoResponse) -> Unit)? = null

    fun setListData(data:ArrayList<pedidoResponse>){
        this.listData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_mesa_carrito, parent, false)
        return MyViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pedido = listData[position]
        holder.bind(listData[position])
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(pedido)
        }
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvMesaCarrito = view.findViewById<TextView>(R.id.tvMesaCarrito)
        val tvPrecioCarrito = view.findViewById<TextView>(R.id.tvPrecioCarrito)
        fun bind(data: pedidoResponse){
            val df = DecimalFormat("#.00").format(data.totalPedido)
            tvMesaCarrito.text = "Mesa ${data.mesaPedido}"
            tvPrecioCarrito.text = "S/.${df}"
        }
    }
}