package utilidades

import entidades.detalle
import entidades.detalleResponse
import entidades.dtoLogin
import entidades.loginResponse
import entidades.pedidoBody
import entidades.pedidoResponse
import entidades.producto
import entidades.venta
import entidades.ventaResponse
import entidadesPago.comprobanteBody
import entidadesPago.comprobanteSuccessResponse
import entidadesPago.generarQRBody
import entidadesPago.login
import entidadesPago.loginBoletaResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import kotlin.collections.ArrayList

interface APIService {
    @POST
    suspend fun login(@Body dtoLogin: dtoLogin, @Url url:String):Response<loginResponse>
    @GET
    suspend fun productos(@Url url:String):Response<ArrayList<producto>>
    @GET
    suspend fun ultimoPedido(@Url url:String):Response<pedidoResponse?>
    @POST
    suspend fun crearPedido(@Body pedidoBody: pedidoBody, @Url url: String):Response<pedidoResponse?>
    @POST
    suspend fun crearDetalle(@Body detalle: detalle, @Url url: String):Response<Unit>
    @GET
    suspend fun pedidosActivos(@Url url: String):Response<ArrayList<pedidoResponse>>
    @GET
    suspend fun detallesPorPedido(@Url url: String):Response<ArrayList<detalleResponse>>
    @GET
    suspend fun detallesPorMesa(@Url url: String):Response<ArrayList<detalleResponse>>
    @POST
    suspend fun actualizarEstado(@Url url: String):Response<Unit>
    @POST
    suspend fun eliminarDetale(@Url url: String):Response<Int>
    @GET
    suspend fun consultarDocumento(@Url url: String):Response<ResponseBody>
    @POST
    suspend fun loginVenta(@Body login: login, @Url url:String):Response<loginBoletaResponse>
    @POST
    suspend fun reservarCorrelativo(@Body venta: venta, @Url url:String):Response<ventaResponse>
    @POST
    suspend fun generarComprobante(@Body comprobanteBody: comprobanteBody, @Url url:String):Response<comprobanteSuccessResponse>
    @POST
    suspend fun generarVenta(@Body venta: ventaResponse, @Url url:String):Response<Unit>
    @POST
    suspend fun actualizarVarios(@Body detalles: List<detalleResponse>, @Url url:String):Response<Unit>
    @POST
    suspend fun generarQR(@Body body: generarQRBody, @Url url: String):Response<ResponseBody>
}