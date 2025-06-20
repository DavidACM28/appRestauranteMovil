package com.example.ellimonysucausa

import Adapters.PlatosRecyclerViewAdapter
import Adapters.SpinnerAdapter
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ellimonysucausa.databinding.ActivityCategoriesBinding
import entidades.detalle
import entidades.estadoPedido
import entidades.pedidoBody
import entidades.pedidoResponse
import entidades.producto
import entidades.trabajador
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utilidades.APIService
import utilidades.HeaderInterceptor
import utilidades.NullOnEmptyConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CategoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var recyclerViewAdapter: PlatosRecyclerViewAdapter
    private lateinit var token:String
    private lateinit var adapter: SpinnerAdapter
    private lateinit var listaCategorias: ArrayList<String>
    private lateinit var item: ArrayList<producto>
    private lateinit var listaFiltrada: ArrayList<producto>
    private lateinit var dialog:Dialog
    private lateinit var btnMas: ImageButton
    private lateinit var btnMenos: ImageButton
    private lateinit var btnAgregar: Button
    private lateinit var etDetalle: EditText
    private lateinit var tvCantidad: TextView
    private lateinit var tvProducto: TextView
    private lateinit var producto: producto
    private var pedido = pedidoResponse()
    private var tienePedido = false

    private var mesa = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        listaFiltrada = ArrayList()
        listaCategorias = ArrayList()
        item = ArrayList()
        producto = producto()
        listaCategorias.add("Todos")
        val trabajador = intent.getSerializableExtra("trabajador", trabajador::class.java)!!
        mesa = intent.getIntExtra("mesa", -1)
        token = intent.getStringExtra("token").toString()
        initRecyclerView()
        createData()
        adapter = SpinnerAdapter(this, listaCategorias)
        binding.spinnerCategorias.adapter = adapter
        if(mesa != -1)
            binding.tvMesa.text = "Añadir productos a la mesa: ${mesa}"
        dialog = Dialog(this)
        dialog.setContentView(R.layout.menu_agregar)
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)

        btnMas = dialog.findViewById(R.id.btnMas)
        btnMenos= dialog.findViewById(R.id.btnMenos)
        btnAgregar = dialog.findViewById(R.id.btnAgregar)
        etDetalle = dialog.findViewById(R.id.etDetalles)
        tvProducto = dialog.findViewById(R.id.tvNombreProducto)
        tvCantidad = dialog.findViewById(R.id.tvCantidad)

        recyclerViewAdapter.onItemClick = {
            if (mesa != -1){
                producto = it
                tvProducto.text = it.nombreProducto
                etDetalle.setText("")
                tvCantidad.text = "1"
                dialog.show()
            }
        }

        btnAgregar.setOnClickListener(){
            if (!tienePedido){
                lifecycleScope.launch {
                    crearPedido(trabajador)
                    crearDetalle(tvCantidad.text.toString().toInt(), etDetalle.text.toString())
                }

            }
            else
                lifecycleScope.launch {
                    crearDetalle(tvCantidad.text.toString().toInt(), etDetalle.text.toString())
                }
        }
        btnMas.setOnClickListener(){
            if (tvCantidad.text.toString().toInt() < 10)
                tvCantidad.text = (tvCantidad.text.toString().toInt() + 1).toString()
        }

        btnMenos.setOnClickListener(){
            if (tvCantidad.text.toString().toInt() != 1)
                tvCantidad.text = (tvCantidad.text.toString().toInt() - 1).toString()
        }

        binding.spinnerCategorias.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                (p1 as? TextView)?.setTextColor(Color.WHITE)
                filtrar(listaCategorias[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.etBuscarProducto.doOnTextChanged { text, _, _, _ ->
            if (text!!.isEmpty()){
                recyclerViewAdapter.setListData(item)
                recyclerViewAdapter.notifyDataSetChanged()
            }
            val listaBusqueda = ArrayList(listaFiltrada.filter { it.nombreProducto!!.contains(text, ignoreCase = true) })
            recyclerViewAdapter.setListData(listaBusqueda)
            recyclerViewAdapter.notifyDataSetChanged()
        }
        binding.btnHome.setOnClickListener(){
            var intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("trabajador", trabajador)
            startActivity(intent)
        }
        binding.btnCart.setOnClickListener(){
            val intent = Intent(this, CarritoActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("trabajador", trabajador)
            intent.putExtra("mesa", mesa)
            startActivity(intent)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://proyectorestauranteback.onrender.com/")
            .addConverterFactory(NullOnEmptyConverterFactory(GsonConverterFactory.create()))
            .client(getClient())
            .build()
    }
    private fun getRetrofit2(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://proyectorestauranteback.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }
    private fun getClient():OkHttpClient {
        val interceptor = HeaderInterceptor()
        interceptor.inicializarToken(token)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
    private val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
    private fun initRecyclerView(){
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@CategoriesActivity)
            setHasFixedSize(true)
            recyclerViewAdapter = PlatosRecyclerViewAdapter()
            adapter = recyclerViewAdapter
        }
    }
    private fun createData(){
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val call = getRetrofit().create(APIService::class.java).productos( "producto/productos")
            val call2 = getRetrofit().create(APIService::class.java).ultimoPedido( "pedido/ultimoPedido/${mesa}")
            val body = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    if (call2.isSuccessful) {
                        val body2:pedidoResponse? = call2.body()
                        if (body2 != null){
                            pedido = body2
                            if (body2.estadoPedido!!.idEstadoPedido != 10)
                                tienePedido = true
                        }
                    }
                    item = body!!
                    listaFiltrada = item
                    for (i in item){
                        if (!listaCategorias.contains(i.categoria!!.nombreCategoria))
                            listaCategorias.add(i.categoria!!.nombreCategoria)
                    }
                    recyclerViewAdapter.setListData(item)
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }
    }
    private fun filtrar(categoria:String){
        if (categoria == "Todos"){
            binding.etBuscarProducto.setText("")
            listaFiltrada = item
            recyclerViewAdapter.setListData(item)
            recyclerViewAdapter.notifyDataSetChanged()
            return
        }
        listaFiltrada= ArrayList(item.filter { it.categoria!!.nombreCategoria.contains(categoria, ignoreCase = true) })
        recyclerViewAdapter.setListData(listaFiltrada)
        recyclerViewAdapter.notifyDataSetChanged()
    }
    private suspend fun crearPedido(trabajador: trabajador) {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDate.format(formatter)

        val estado = estadoPedido(11, "Creado")
        val pedidoBody = pedidoBody(trabajador, estado, "Comer en local", formattedDate, mesa, 0.0)

        val response = getRetrofit2().create(APIService::class.java)
            .crearPedido(pedidoBody, "pedido/nuevoPedido")

        if (response.isSuccessful) {
            withContext(Dispatchers.Main) {
                this@CategoriesActivity.pedido = response.body()!!
                tienePedido = true
            }
        }
    }
    private suspend fun crearDetalle(cantidad: Int, detalleTexto: String) {
        val estado = estadoPedido(12, "Añadido")
        val detalle1 = detalle(pedido, producto, estado, producto.precioProducto!!, detalleTexto)

        val api = getRetrofit2().create(APIService::class.java)

        for (i in 1..cantidad){
            api.crearDetalle(detalle1, "detalle/nuevoDetalle")
        }
        val response2 = api.crearDetalle(detalle1, "pedido/actualizarPedido/${pedido.idPedido}/${producto.precioProducto!!*cantidad}")
        if (response2.isSuccessful) {
            withContext(Dispatchers.Main) {
                dialog.hide()
                Toast.makeText(this@CategoriesActivity, "Producto agregado", Toast.LENGTH_SHORT).show()
            }
        }
    }


}