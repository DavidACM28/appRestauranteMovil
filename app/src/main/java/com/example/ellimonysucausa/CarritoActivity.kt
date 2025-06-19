package com.example.ellimonysucausa

import Adapters.MesaCarritoAdapter
import Adapters.PlatoCarritoAdapter
import Adapters.SpinnerAdapter
import android.app.Dialog
import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ellimonysucausa.databinding.ActivityCarritoBinding
import com.google.gson.Gson
import entidades.detalleResponse
import entidades.pedidoResponse
import entidades.tipoComprobante
import entidades.tipoPago
import entidades.trabajador
import entidades.venta
import entidades.ventaResponse
import entidadesDNIRUC.dniSuccess
import entidadesDNIRUC.responseFail
import entidadesDNIRUC.rucSuccess
import entidadesPago.Address
import entidadesPago.Client
import entidadesPago.Company
import entidadesPago.Detail
import entidadesPago.FormaPago
import entidadesPago.Legend
import entidadesPago.comprobanteBody
import entidadesPago.login
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utilidades.APIService
import utilidades.HeaderInterceptor
import utilidades.NullOnEmptyConverterFactory
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.round

class CarritoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarritoBinding
    private lateinit var item: ArrayList<pedidoResponse>
    private lateinit var item2: ArrayList<detalleResponse>
    private lateinit var mesaCarritoAdapter: MesaCarritoAdapter
    private lateinit var platoCarritoAdapter: PlatoCarritoAdapter
    private lateinit var spinnerMedioPagoAdapter:SpinnerAdapter
    private lateinit var spinnerTipoComprobanteAdapter:SpinnerAdapter
    private lateinit var dialog: Dialog
    private lateinit var dialog2: Dialog
    private lateinit var dialog3: Dialog
    private lateinit var tvProductoEliminar: TextView
    private lateinit var tvProductoEntregar: TextView
    private lateinit var tvMesaEliminar: TextView
    private lateinit var btnCancelar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnConfirmar: Button
    private lateinit var btnCancelarEntregar: Button
    private lateinit var btnCancelarSeguro: Button
    private lateinit var btnConfirmarSeguro: Button

    private var tokenBoleta = ""
    private var token = ""
    private var mesa = -1
    private var idDetalle = 0
    private var idPedido = 0
    private var precioProducto:Double = 0.0
    private var totalPedido:Double = 0.0
    private var trabajador = trabajador()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        trabajador = intent.getSerializableExtra("trabajador", trabajador::class.java)!!
        val listaMedioPago = arrayListOf("Efectivo", "Billetera electronica", "Tarjeta")
        val listaTipoComprobante = arrayListOf("Boleta", "Factura")
        spinnerMedioPagoAdapter = SpinnerAdapter(this, listaMedioPago)
        spinnerTipoComprobanteAdapter = SpinnerAdapter(this, listaTipoComprobante)
        mesa = intent.getIntExtra("mesa", -1)
        item = ArrayList()
        item2 = ArrayList()
        token = intent.getStringExtra("token").toString()
        initRecyclerView()
        initRecyclerView2()
        createData()
        traerToken()
        binding.spMedioPago.adapter = spinnerMedioPagoAdapter
        binding.spTipoComprobante.adapter = spinnerTipoComprobanteAdapter

        dialog = Dialog(this)
        dialog.setContentView(R.layout.menu_eliminar)
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)
        dialog2 = Dialog(this)
        dialog2.setContentView(R.layout.menu_entregar)
        dialog2.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog2.setCancelable(true)
        dialog3 = Dialog(this)
        dialog3.setContentView(R.layout.menu_seguro)
        dialog3.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog3.setCancelable(true)


        tvProductoEliminar = dialog.findViewById(R.id.tvProductoEliminar)
        tvMesaEliminar = dialog.findViewById(R.id.tvMesaEliminar)
        btnCancelar = dialog.findViewById(R.id.btnCancelar)
        btnEliminar = dialog.findViewById(R.id.btnEliminar)
        tvProductoEntregar = dialog2.findViewById(R.id.tvProductoEntregar)
        btnConfirmar = dialog2.findViewById(R.id.btnConfirmar)
        btnCancelarEntregar = dialog2.findViewById(R.id.btnCancelarEntregar)
        btnCancelarSeguro = dialog3.findViewById(R.id.btnCancelarSeguro)
        btnConfirmarSeguro = dialog3.findViewById(R.id.btnConfirmarSeguro)


        if (mesa != -1){
            binding.lyMesas.visibility = View.INVISIBLE
            binding.lyDetalleMesa.visibility = View.VISIBLE
            binding.tvMesa.text = "Mesa ${mesa}"
            initRecyclerView2()
            createData3(mesa)
        }
        btnConfirmarSeguro.setOnClickListener(){
            binding.tvMesaFinalizar.text = "Mesa ${mesa}"
            binding.etDocumento.setText("")
            binding.tvCliente.text = ""
            binding.lyDetalleMesa.visibility = View.INVISIBLE
            binding.lyFinalizar.visibility = View.VISIBLE
            dialog3.hide()
        }
        mesaCarritoAdapter.onItemClick = {
            totalPedido = it.totalPedido!!
            idPedido = it.idPedido!!
            mesa = it.mesaPedido!!
            binding.lyMesas.visibility = View.INVISIBLE
            binding.lyDetalleMesa.visibility = View.VISIBLE
            binding.tvMesa.text = "Mesa ${mesa}"
            createData2(it.idPedido!!)
        }
        platoCarritoAdapter.onItemClick = {
            idDetalle = it.idDetalle
            idPedido = it.pedido.idPedido!!
            precioProducto = it.producto.precioProducto!!
            if(it.estadoPedido.idEstadoPedido == 8){
                tvProductoEntregar.text = it.producto.nombreProducto
                dialog2.show()
            }
            else if (it.estadoPedido.idEstadoPedido == 9){
                Toast.makeText(this, "No se puede eliminar este producto", Toast.LENGTH_SHORT).show()
            }
            else{
                tvProductoEliminar.text = it.producto.nombreProducto
                tvMesaEliminar.text = "De la mesa ${mesa}"
                dialog.show()
            }
        }
        btnCancelar.setOnClickListener(){
            dialog.hide()
        }
        btnCancelarEntregar.setOnClickListener(){
            dialog2.hide()
        }
        btnEliminar.setOnClickListener(){
            if (item2.size == 1){
                eliminarPedido(idDetalle, idPedido, precioProducto)
            }
            else
                eliminarDetalle(idDetalle, idPedido, precioProducto)
        }
        btnConfirmar.setOnClickListener(){
            actualizarEstado2(idDetalle)
        }
        binding.btnHome.setOnClickListener(){
            intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("trabajador", trabajador)
            intent.putExtra("token", token)
            startActivity(intent)
        }
        binding.btnCategories.setOnClickListener(){
            intent = Intent(this, CategoriesActivity::class.java)
            intent.putExtra("trabajador", trabajador)
            intent.putExtra("token", token)
            intent.putExtra("mesa", mesa)
            startActivity(intent)
        }
        binding.btnEnviar.setOnClickListener(){
            actualizarEstado()
        }
        binding.btnAtras.setOnClickListener(){
            binding.lyMesas.visibility = View.VISIBLE
            binding.lyDetalleMesa.visibility = View.INVISIBLE
        }
        binding.btnAtrasFinalizar.setOnClickListener(){
            binding.lyFinalizar.visibility = View.INVISIBLE
            binding.lyDetalleMesa.visibility = View.VISIBLE
        }
        binding.btnFinalizar.setOnClickListener(){
            if (item2.isEmpty()){
                Toast.makeText(this, "No puedes finalizar un pedido vacío", Toast.LENGTH_SHORT).show()
            }
            else{
                for(i in item2){
                    if (i.estadoPedido.idEstadoPedido == 12){
                        Toast.makeText(this, "Aún hay productos sin enviar a cocina", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else if (i.estadoPedido.idEstadoPedido != 9){
                        dialog3.show()
                        return@setOnClickListener
                    }
                }
            }
        }
        binding.cbSinDocumento.setOnCheckedChangeListener(){_, isChecked->
            if (isChecked){
                binding.etDocumento.setText("")
                binding.etDocumento.isEnabled = false
                binding.tvCliente.text = ""
            }
            else{
                binding.etDocumento.isEnabled = true
            }
        }
        binding.spTipoComprobante.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                binding.etDocumento.setText("")
                binding.tvCliente.text = ""
                if (p2 == 1){
                    binding.cbSinDocumento.isChecked = false
                    binding.cbSinDocumento.isEnabled = false
                }
                else{
                    binding.cbSinDocumento.isEnabled = true
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        binding.btnConsultar.setOnClickListener(){
            consultarDocumento(binding.etDocumento.text.toString(), (binding.spTipoComprobante.selectedItem as? Int)!!)
        }
        binding.btnTerminarPedido.setOnClickListener(){
            if(binding.spTipoComprobante.selectedItem as? Int == 0) {
                if ((!binding.cbSinDocumento.isChecked && binding.tvCliente.text != "" && binding.etDocumento.length() == 8) || binding.cbSinDocumento.isChecked)
                    enviarBoleta(binding.spMedioPago.selectedItem as Int)
                else{
                    Toast.makeText(this, "Si deseas hacer una boleta sin datos de cliente, marca la casilla", Toast.LENGTH_LONG).show()
                }
            }
            else{
                if (binding.etDocumento.length() != 11){
                    Toast.makeText(this, "El RUC debe tener 11 dígitos", Toast.LENGTH_SHORT).show()
                }
                else if(binding.tvCliente.text == "")
                    Toast.makeText(this, "Tiene que consultar el ruc antes de continuar", Toast.LENGTH_SHORT).show()
                else
                    enviarFactura(binding.spMedioPago.selectedItem as Int)
            }
        }
    }

    private fun initRecyclerView(){
        binding.rvMesas.apply {
            layoutManager = LinearLayoutManager(this@CarritoActivity)
            setHasFixedSize(true)
            mesaCarritoAdapter = MesaCarritoAdapter()
            adapter = mesaCarritoAdapter
        }
    }
    private fun initRecyclerView2(){
        binding.rvProductos.apply {
            layoutManager = LinearLayoutManager(this@CarritoActivity)
            setHasFixedSize(true)
            platoCarritoAdapter = PlatoCarritoAdapter()
            adapter = platoCarritoAdapter
        }
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory((GsonConverterFactory.create()))
            .client(getClient())
            .build()
    }
    private fun getRetrofit2(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dniruc.apisperu.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun getRetrofit3(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://facturacion.apisperu.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun getRetrofit4(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://facturacion.apisperu.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient2())
            .build()
    }
    private fun getClient(): OkHttpClient {
        val interceptor = HeaderInterceptor()
        interceptor.inicializarToken(token)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
    private fun getClient2(): OkHttpClient {
        val interceptor = HeaderInterceptor()
        interceptor.inicializarToken(tokenBoleta)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
    private val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
    private fun createData(){
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val call = getRetrofit().create(APIService::class.java).pedidosActivos( "pedido/pedidosActivos")
            val body = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    item = body!!
                    mesaCarritoAdapter.setListData(item)
                    mesaCarritoAdapter.notifyDataSetChanged()
                    val asd = item.find {it.mesaPedido == mesa}
                    if (asd != null){
                        totalPedido = asd.totalPedido!!
                        idPedido = asd.idPedido!!
                    }
                }
            }
        }
    }
    private fun createData2(idPedido: Int){
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val call = getRetrofit().create(APIService::class.java).detallesPorPedido( "detalle/detalles/${idPedido}")
            val body = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    item2 = body!!
                    platoCarritoAdapter.setListData(item2)
                    platoCarritoAdapter.notifyDataSetChanged()
                }
            }
        }
    }
    private fun createData3(mesa: Int){
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val call = getRetrofit().create(APIService::class.java).detallesPorMesa("detalle/detallesMesa/${mesa}")
            val body = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    item2 = body!!
                    platoCarritoAdapter.setListData(item2)
                    platoCarritoAdapter.notifyDataSetChanged()
                }
            }
        }
    }
    private fun actualizarEstado(){
        var call = Any()
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch{
            for(i in item2){
                if(i.estadoPedido.idEstadoPedido == 12){
                    call = getRetrofit().create(APIService::class.java).actualizarEstado("detalle/actualizarEstado/${i.idDetalle}")
                }
            }
            createData3(mesa)
            runOnUiThread {
                if ((call as? Response<*>)!!.isSuccessful){
                    Toast.makeText(this@CarritoActivity, "Productos enviados a cocina", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun eliminarDetalle(idDetalle:Int, idPedido:Int, precioProducto:Double){
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch{
            val call = getRetrofit().create(APIService::class.java).eliminarDetale("detalle/eliminar/${idDetalle}/${idPedido}/${precioProducto}")
            createData3(mesa)
            createData()
            runOnUiThread {
                if (call.isSuccessful){
                    val body = call.body()!!
                    if (body == 1){
                        Toast.makeText(this@CarritoActivity, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show()
                        dialog.hide()
                    }
                    else{
                        Toast.makeText(this@CarritoActivity, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show()
                        dialog.hide()
                    }

                }
                else
                    Toast.makeText(this@CarritoActivity, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun eliminarPedido(idDetalle:Int, idPedido:Int, precioProducto:Double){
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch{
            val call = getRetrofit().create(APIService::class.java).eliminarDetale("detalle/eliminarPedido/${idDetalle}/${idPedido}/${precioProducto}")
            createData3(mesa)
            createData()
            runOnUiThread {
                if (call.isSuccessful){
                    if (call.body() == 1)
                        Toast.makeText(this@CarritoActivity, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this@CarritoActivity, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show()
                    dialog.hide()
                }
            }
        }
    }
    private fun actualizarEstado2(id:Int){
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch{
            val call =getRetrofit().create(APIService::class.java).eliminarDetale("detalle/actualizarEstado2/${id}")
            createData3(mesa)
            runOnUiThread {
                if(call.isSuccessful){
                    Toast.makeText(this@CarritoActivity, "Entrega confirmada correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun consultarDocumento(documento: String, tipoConsulta: Int) {
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val url = if (tipoConsulta == 0) {
                "dni/$documento?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRhY2FzbW9yZUBob3RtYWlsLmNvbSJ9.1TaDZFXzcZfTvpkvAxXjwYDYSVfp_dSyNnS_N958-KY"
            } else {
                "ruc/$documento?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRhY2FzbW9yZUBob3RtYWlsLmNvbSJ9.1TaDZFXzcZfTvpkvAxXjwYDYSVfp_dSyNnS_N958-KY"
            }
            try {
                val response = getRetrofit2().create(APIService::class.java).consultarDocumento(url)

                val bodyString = response.body()?.string()

                if (bodyString != null) {
                    val json = JSONObject(bodyString)
                    val isSuccess = json.optBoolean("success", false)
                    val message = json.optString("message")

                    withContext(Dispatchers.Main) {
                        if (tipoConsulta == 0){
                            if (isSuccess) {
                                val successResponse = Gson().fromJson(bodyString, dniSuccess::class.java)
                                val nombreCompleto = "${successResponse.nombres} ${successResponse.apellidoPaterno} ${successResponse.apellidoMaterno}"
                                binding.tvCliente.text = nombreCompleto
                            } else {
                                val errorResponse = Gson().fromJson(bodyString, responseFail::class.java)
                                binding.tvCliente.text = ""
                                Toast.makeText(this@CarritoActivity, "Error: ${errorResponse.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            if (message.isEmpty()) {
                                val successResponse = Gson().fromJson(bodyString, rucSuccess::class.java)
                                binding.tvCliente.text = successResponse.razonSocial
                            } else {
                                val errorResponse = Gson().fromJson(bodyString, responseFail::class.java)
                                binding.tvCliente.text = ""
                                Toast.makeText(this@CarritoActivity, "Error: ${errorResponse.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CarritoActivity, "Respuesta vacía del servidor", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CarritoActivity, "Excepción: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun traerToken(){
        val login = login("DavidCastro", "ezpz1406")
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch{
            val call =getRetrofit3().create(APIService::class.java).loginVenta(login,"auth/login")
            runOnUiThread {
                if(call.isSuccessful){
                    tokenBoleta = call.body()!!.token
                }
            }
        }
    }
    private fun convertirNumeroALetras(numero: Int): String {
        val unidades = listOf(
            "", "UNO", "DOS", "TRES", "CUATRO", "CINCO",
            "SEIS", "SIETE", "OCHO", "NUEVE", "DIEZ", "ONCE",
            "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISÉIS",
            "DIECISIETE", "DIECIOCHO", "DIECINUEVE"
        )

        val decenas = listOf(
            "", "", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA",
            "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"
        )

        val centenas = listOf(
            "", "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS",
            "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"
        )

        if (numero == 0) return "CERO"
        if (numero == 100) return "CIEN"

        val sb = StringBuilder()
        val miles = numero / 1000
        val restoMiles = numero % 1000

        if (miles > 0) {
            if (miles == 1) {
                sb.append("MIL")
            } else {
                sb.append("${convertirNumeroALetras(miles)} MIL")
            }
            if (restoMiles > 0) sb.append(" ")
        }

        val centenasValor = restoMiles / 100
        val decenasUnidad = restoMiles % 100

        if (centenasValor > 0) {
            sb.append(centenas[centenasValor])
            if (decenasUnidad > 0) sb.append(" ")
        }

        if (decenasUnidad < 20) {
            if (decenasUnidad > 0) sb.append(unidades[decenasUnidad])
        } else {
            val decena = decenasUnidad / 10
            val unidad = decenasUnidad % 10
            if (decena == 2 && unidad > 0) {
                sb.append("VEINTI${unidades[unidad].lowercase()}")
            } else {
                sb.append(decenas[decena])
                if (unidad > 0) sb.append(" Y ${unidades[unidad]}")
            }
        }

        return sb.toString().uppercase()
    }
    private fun montoEnLetras(monto: Double): String {
        val parteEntera = monto.toInt()
        val parteDecimal = round((monto - parteEntera) * 100).toInt()
        val letras = convertirNumeroALetras(parteEntera)
        return "SON $letras CON %02d/100 SOLES".format(parteDecimal)
    }
    private fun crearBoleta(): comprobanteBody {
        var rznSocial = ""
        rznSocial = if (binding.tvCliente.text != ""){
            binding.tvCliente.text.toString()
        } else{
            "Clientes varios"
        }
        val correlativo = "00000001" // Temporal, se sobrescribe después
        val zone = ZoneId.of("America/Lima")
        val now = ZonedDateTime.now(zone)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        val fechaEmision = now.format(formatter)

        val formaPago = FormaPago(moneda = "PEN", tipo = "Contado")

        val addressCliente = Address(
            direccion = "Direccion cliente",
            provincia = "TRUJILLO",
            departamento = "LA LIBERTAD",
            distrito = "TRUJILLO",
            ubigueo = "130101"
        )

        val numDoc = binding.etDocumento.text.toString().toLongOrNull() ?: 10000000L

        val client = Client(
            tipoDoc = "1",
            numDoc = numDoc,
            rznSocial = rznSocial,
            address = addressCliente
        )

        val addressCompany = Address(
            direccion = "Los Diamantes 486",
            provincia = "TRUJILLO",
            departamento = "LA LIBERTAD",
            distrito = "TRUJILLO",
            ubigueo = "130101"
        )

        val company = Company(
            ruc = 10295835465L,
            razonSocial = "El Limon y su Causa",
            nombreComercial = "El Limon y su Causa",
            address = addressCompany
        )

        val mtoOperGravadas = totalPedido / 1.18
        val mtoIGV = totalPedido - mtoOperGravadas
        val subTotal = totalPedido
        val mtoImpVenta = totalPedido

        val detalles = item2.map {
            val codProducto = "P%03d".format(it.producto.idProducto)
            val cantidad = 1.0
            val mtoValorUnitario = it.producto.precioProducto!! / 1.18
            val mtoValorVenta = mtoValorUnitario
            val mtoBaseIgv = mtoValorUnitario
            val porcentajeIgv = 18.0
            val igv = it.producto.precioProducto!! - mtoValorUnitario
            val mtoPrecioUnitario = it.producto.precioProducto!!

            Detail(
                codProducto = codProducto,
                unidad = "ZZ",
                descripcion = it.producto.nombreProducto!!,
                cantidad = cantidad,
                mtoValorUnitario = mtoValorUnitario,
                mtoValorVenta = mtoValorVenta,
                mtoBaseIgv = mtoBaseIgv,
                porcentajeIgv = porcentajeIgv,
                igv = igv,
                tipAfeIgv = 10,
                totalImpuestos = igv,
                mtoPrecioUnitario = mtoPrecioUnitario
            )
        }

        val legends = listOf(
            Legend(
                code = "1000",
                value = montoEnLetras(totalPedido)
            )
        )

        return comprobanteBody(
            ublVersion = "2.1",
            tipoOperacion = "0101",
            tipoDoc = "03",
            serie = "B001",
            correlativo = correlativo,
            fechaEmision = fechaEmision,
            formaPago = formaPago,
            tipoMoneda = "PEN",
            client = client,
            company = company,
            mtoOperGravadas = mtoOperGravadas,
            mtoIGV = mtoIGV,
            valorVenta = mtoOperGravadas,
            totalImpuestos = mtoIGV,
            subTotal = subTotal,
            mtoImpVenta = mtoImpVenta,
            details = detalles,
            legends = legends
        )
    }
    private fun enviarBoleta(idMedio:Int){
        val boleta = crearBoleta()
        val venta = venta()
        var ventaResponse:ventaResponse
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch{
            val call2 = getRetrofit().create(APIService::class.java).reservarCorrelativo(venta,"venta/nuevoVenta")
            if(call2.isSuccessful){
                val correlativoFormateado = String.format(Locale.US,"%08d", call2.body()!!.idVenta)
                ventaResponse = ventaResponse(call2.body()!!.idVenta)
                boleta.correlativo = correlativoFormateado
            }
            else{
                runOnUiThread {
                    Toast.makeText(this@CarritoActivity, "No se pudo generar la boleta", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            val call = getRetrofit4().create(APIService::class.java).generarComprobante(boleta,"invoice/send")
            if (call.isSuccessful){
                ventaResponse.pedidoResponse = pedidoResponse(idPedido = idPedido)
                ventaResponse.tipoPago = tipoPago(idMedio + 1)
                ventaResponse.tipoComprobante = tipoComprobante(1)
                val call = getRetrofit().create(APIService::class.java).generarVenta(ventaResponse,"venta/actualizarVenta")
                runOnUiThread {
                    if(call.isSuccessful){
                        Toast.makeText(this@CarritoActivity, "Venta generada", Toast.LENGTH_SHORT).show()
                        intent = Intent(this@CarritoActivity, HomeActivity::class.java)
                        intent.putExtra("trabajador", trabajador)
                        intent.putExtra("token", token)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@CarritoActivity, "No se pudo generar la boleta", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                runOnUiThread {
                    Toast.makeText(this@CarritoActivity, "No se pudo generar la boleta", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
        }
    }
    private fun crearFactura(): comprobanteBody {
        var rznSocial = ""
        rznSocial = binding.tvCliente.text.toString()
        val correlativo = "00000001" // Temporal, se sobrescribe después
        val zone = ZoneId.of("America/Lima")
        val now = ZonedDateTime.now(zone)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        val fechaEmision = now.format(formatter)

        val formaPago = FormaPago(moneda = "PEN", tipo = "Contado")

        val addressCliente = Address(
            direccion = "Direccion cliente",
            provincia = "TRUJILLO",
            departamento = "LA LIBERTAD",
            distrito = "TRUJILLO",
            ubigueo = "130101"
        )

        val numDoc = binding.etDocumento.text.toString().toLong()

        val client = Client(
            tipoDoc = "6",
            numDoc = numDoc,
            rznSocial = rznSocial,
            address = addressCliente
        )

        val addressCompany = Address(
            direccion = "Los Diamantes 486",
            provincia = "TRUJILLO",
            departamento = "LA LIBERTAD",
            distrito = "TRUJILLO",
            ubigueo = "130101"
        )

        val company = Company(
            ruc = 10295835465L,
            razonSocial = "El Limon y su Causa",
            nombreComercial = "El Limon y su Causa",
            address = addressCompany
        )

        val mtoOperGravadas = totalPedido / 1.18
        val mtoIGV = totalPedido - mtoOperGravadas
        val subTotal = totalPedido
        val mtoImpVenta = totalPedido

        val detalles = item2.map {
            val codProducto = "P%03d".format(it.producto.idProducto)
            val cantidad = 1.0
            val mtoValorUnitario = it.producto.precioProducto!! / 1.18
            val mtoValorVenta = mtoValorUnitario
            val mtoBaseIgv = mtoValorUnitario
            val porcentajeIgv = 18.0
            val igv = it.producto.precioProducto!! - mtoValorUnitario
            val mtoPrecioUnitario = it.producto.precioProducto!!

            Detail(
                codProducto = codProducto,
                unidad = "ZZ",
                descripcion = it.producto.nombreProducto!!,
                cantidad = cantidad,
                mtoValorUnitario = mtoValorUnitario,
                mtoValorVenta = mtoValorVenta,
                mtoBaseIgv = mtoBaseIgv,
                porcentajeIgv = porcentajeIgv,
                igv = igv,
                tipAfeIgv = 10,
                totalImpuestos = igv,
                mtoPrecioUnitario = mtoPrecioUnitario
            )
        }

        val legends = listOf(
            Legend(
                code = "1000",
                value = montoEnLetras(totalPedido)
            )
        )

        return comprobanteBody(
            ublVersion = "2.1",
            tipoOperacion = "0101",
            tipoDoc = "01",
            serie = "F001",
            correlativo = correlativo,
            fechaEmision = fechaEmision,
            formaPago = formaPago,
            tipoMoneda = "PEN",
            client = client,
            company = company,
            mtoOperGravadas = mtoOperGravadas,
            mtoIGV = mtoIGV,
            valorVenta = mtoOperGravadas,
            totalImpuestos = mtoIGV,
            subTotal = subTotal,
            mtoImpVenta = mtoImpVenta,
            details = detalles,
            legends = legends
        )
    }
    private fun enviarFactura(idMedio: Int){
        val factura = crearFactura()
        val venta = venta()
        var ventaResponse:ventaResponse
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch{
            val call2 = getRetrofit().create(APIService::class.java).reservarCorrelativo(venta,"venta/nuevoVenta")
            if(call2.isSuccessful){
                val correlativoFormateado = String.format(Locale.US,"%08d", call2.body()!!.idVenta)
                ventaResponse = ventaResponse(call2.body()!!.idVenta)
                factura.correlativo = correlativoFormateado
            }
            else{
                runOnUiThread {
                    Toast.makeText(this@CarritoActivity, "No se pudo generar la factura", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            val call = getRetrofit4().create(APIService::class.java).generarComprobante(factura,"invoice/send")
            if (call.isSuccessful){
                ventaResponse.pedidoResponse = pedidoResponse(idPedido = idPedido)
                ventaResponse.tipoPago = tipoPago(idMedio + 1)
                ventaResponse.tipoComprobante = tipoComprobante(3)
                val call = getRetrofit().create(APIService::class.java).generarVenta(ventaResponse,"venta/actualizarVenta")
                runOnUiThread {
                    if(call.isSuccessful){
                        Toast.makeText(this@CarritoActivity, "Venta generada", Toast.LENGTH_SHORT).show()
                        intent = Intent(this@CarritoActivity, HomeActivity::class.java)
                        intent.putExtra("trabajador", trabajador)
                        intent.putExtra("token", token)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@CarritoActivity, "No se pudo generar la factura", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                runOnUiThread {
                    Toast.makeText(this@CarritoActivity, "No se pudo generar la factura", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
        }
    }

}













