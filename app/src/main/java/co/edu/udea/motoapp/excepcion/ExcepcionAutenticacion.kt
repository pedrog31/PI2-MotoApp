package co.edu.udea.motoapp.excepcion

class ExcepcionAutenticacion(val mensaje: String) : Throwable() {

    constructor() : this("Usuario no inicio sesion en la aplicacion") {
    }
}
