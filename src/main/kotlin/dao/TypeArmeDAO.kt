package dao

import coBDD

import jdbc.BDD
import model.item.TypeArme
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement
class TypeArmeDAO (val bdd: BDD =coBDD ) {


    fun findAll(): MutableMap<String, TypeArme> {
        val result = mutableMapOf<String, TypeArme>()

        val sql = "SELECT * FROM TypeArme"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val nombreDe = resultatRequete.getInt("nombreDe")
                val valeurDemax = resultatRequete.getInt("valeurDemax")
                val multiplacteurCritique = resultatRequete.getInt("multiplacteurCritique")
                val activationCritique = resultatRequete.getInt("activationCritique")
                result.set(nom.lowercase(), TypeArme(id,nom,nombreDe , valeurDemax, multiplacteurCritique, activationCritique))
            }
        }
        requetePreparer.close()
        return result
    }
}