package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.ellimonysucausa.R
import com.google.android.material.card.MaterialCardView

class MesaAdapter (private val context: Context, private val mesas:List<String>): ArrayAdapter<String>(context,
    R.layout.item_mesa, mesas) {
    private var selectedPosition = -1 // Para manejar selección única

    // ViewHolder para mejor rendimiento
    private inner class ViewHolder(view: View) {
        val cardMesa: MaterialCardView = view.findViewById(R.id.cardMesa)
        val ivMesa: ImageView = view.findViewById(R.id.ivMesa)
        val tvMesa: TextView = view.findViewById(R.id.tvMesa)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val holder: ViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_mesa, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        }
        else{
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        // 1. Asignar datos
        val mesaActual = mesas[position]
        holder.tvMesa.text = mesaActual
        holder.ivMesa.setImageResource(R.drawable.mesa1)

        // 2. Resaltar selección
        if (position == selectedPosition) {
            holder.cardMesa.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.verde_limon)
            )
        }
        else{
            holder.cardMesa.setCardBackgroundColor(
                ContextCompat.getColor(context, android.R.color.white)
            )
        }
        return view
    }




}





