package Adapters

import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ellimonysucausa.R
import entidades.detalleResponse
import entidades.pedidoResponse

class PlatoCarritoAdapter:RecyclerView.Adapter<PlatoCarritoAdapter.MyViewHolder>() {
    private var listData = ArrayList<detalleResponse>()
    var onItemClick : ((detalleResponse) -> Unit)? = null

    fun setListData(data:ArrayList<detalleResponse>){
        this.listData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_producto_carrito, parent, false)
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
        private val tvNombreProducto: TextView = view.findViewById(R.id.tvNombreProductoCarrito)
        private val tvEstadoProducto: TextView = view.findViewById(R.id.tvEstadoProductoCarrito)
        private val tvPrecioProducto: TextView = view.findViewById(R.id.tvPrecioProductoCarrito)
        fun bind(data: detalleResponse){
            val df = DecimalFormat("#.00").format(data.producto.precioProducto)
            tvNombreProducto.text = data.producto.nombreProducto
            tvPrecioProducto.text = "S/.${df}"
            tvEstadoProducto.text = data.estadoPedido.descripcionEstadoPedido
        }
    }
}