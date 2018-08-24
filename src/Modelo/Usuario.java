/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

/**
 *
 * @author Domingo Luna
 */
public class Usuario
{
    String idUsuario;
    String userName;
    String password;
    String nombre;
    String Tipo;
    
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
    
    /**
     * Este método sirve para comparar si un usuario es igual a otro.
     * Dos usuarios son iguales si el username y el password tienen
     * el mismo valor.
     * @param o
     * @return 
     */
    public boolean equals(Object o)
    {
        Usuario uTmp = null;
        
        if (o == null)
            return false;
        
        //Comparamos si el objeto o es de tipo Usuario
        if (o instanceof Usuario)
        {
            //Convertimos o en un tipo Usuario
            uTmp = (Usuario) o;
            
            if (uTmp.getUserName() == null || uTmp.getPassword() == null)
                return false;
            
            //Comparamos el userName de esta clase, con el del objeto uTmp y
            //Comparamos el password de esta clase, con el del objeto uTmp
            if (userName.equals(uTmp.getUserName()) && 
                password.equals(uTmp.getPassword()))
            {
                nombre=uTmp.getNombre();
                idUsuario=uTmp.getIdUsuario();
                Tipo=uTmp.getTipo();
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }
}
