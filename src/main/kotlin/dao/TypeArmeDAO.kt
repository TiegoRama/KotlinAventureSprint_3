package dao

import coBDD

import jdbc.BDD
import model.item.Qualite
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
    fun findByNom(nomRechecher:String): MutableMap<String, TypeArme> {
        val result = mutableMapOf<String, TypeArme>()

        val sql = "SELECT * FROM TypeArme WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
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
    fun findById(id:Int): TypeArme? {
        var result :TypeArme?=null
        val sql = "SELECT * FROM TypeArme WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val nombreDe = resultatRequete.getInt("nombreDe")
                val valeurDemax = resultatRequete.getInt("valeurDemax")
                val multiplacteurCritique = resultatRequete.getInt("multiplacteurCritique")
                val activationCritique = resultatRequete.getInt("activationCritique")
                result=TypeArme(id,nom,nombreDe , valeurDemax, multiplacteurCritique, activationCritique)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }

    fun save(unTypeArme: TypeArme): TypeArme? {

        val requetePreparer:PreparedStatement

        if (unTypeArme.id == null) {
            val sql =
                "Insert Into TypeArme (nom,nombreDe , valeurDemax, multiplacteurCritique, activationCritique) values (?,?,?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, unTypeArme.nom)
            requetePreparer?.setInt(2, unTypeArme.nombreDes)
            requetePreparer?.setInt(3, unTypeArme.valeurDeMax)
            requetePreparer?.setInt(4,unTypeArme.multiplicateurCritique)
            requetePreparer?.setInt(5,unTypeArme.activationCritique)

        } else {
            var sql = ""
            sql =
                "Update  TypeArme set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, unTypeArme.nom)
            requetePreparer?.setInt(2, unTypeArme.nombreDes)
            requetePreparer?.setInt(3, unTypeArme.valeurDeMax)
            requetePreparer?.setInt(4,unTypeArme.multiplicateurCritique)
            requetePreparer?.setInt(5,unTypeArme.activationCritique)

        }


        // Exécutez la requête d'insertion
        val nbLigneMaj = requetePreparer?.executeUpdate()
        // La méthode executeUpdate() retourne le nombre de lignes modifié par un insert, update ou delete sinon elle retourne 0 ou -1

        // Si l'insertion a réussi
        if (nbLigneMaj != null && nbLigneMaj > 0) {
            // Récupérez les clés générées (comme l'ID auto-incrémenté)
            val generatedKeys = requetePreparer.generatedKeys
            if (generatedKeys.next()) {
                val id = generatedKeys.getInt(1) // Supposons que l'ID est la première col
                unTypeArme.id = id // Mettez à jour l'ID de l'objet Qualite avec la valeur générée
                return unTypeArme
            }
        }
        requetePreparer.close()

        return null
    }
    fun saveAll(LesTypesArmes:Collection<TypeArme>):MutableMap<String,TypeArme>{
        var result= mutableMapOf<String,TypeArme>()
        for (unTypeArme in LesTypesArmes){
            val TypeArmeSauvegarde=this.save(unTypeArme)
            if (TypeArmeSauvegarde!=null){
                result.set(TypeArmeSauvegarde.nom.lowercase(),TypeArmeSauvegarde)
            }
        }
        return result
    }
}