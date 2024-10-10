package dao

import coBDD
import jdbc.BDD
import model.item.Arme
import model.item.Potion
import model.item.Qualite
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

class PotionDao (val bdd: BDD =coBDD){ 
    fun findAll(): MutableMap<String, Potion> {
        val result = mutableMapOf<String, Potion>()

        val sql = "SELECT * FROM Potion"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val description = resultatRequete.getString("couleur")
                val soins = resultatRequete.getInt("soin")



                result.set(nom.lowercase(), Potion(id, nom, description,soins))
            }
        }
        requetePreparer.close()
        return result
    }
    fun findByNom(nomRechecher:String): MutableMap<String, Potion> {
        val result = mutableMapOf<String, Potion>()

        val sql = "SELECT * FROM Potion WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description= resultatRequete.getString("description")
                val soins= resultatRequete.getInt("soin")
                result.set(nom.lowercase(), Potion(id,nom,description,soins))
            }
        }
        requetePreparer.close()
        return result
    }
    fun findById(id:Int): Potion? {
        var result : Potion?=null
        val sql = "SELECT * FROM Potion WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val soin= resultatRequete.getInt("soin")
                result= Potion(id,nom,description,soin)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }
    fun save(unPotion: Potion): Potion? {

        val requetePreparer:PreparedStatement

        if (unPotion.id == null) {
            val sql =
                "Insert Into Potion (nom,description ,soin) values (?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, unPotion.nom)
            requetePreparer?.setString(2, unPotion.description)
            requetePreparer?.setInt(3, unPotion.soin)

        } else {
            var sql = ""
            sql =
                "Update  Potion set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, unPotion.nom)
            requetePreparer?.setString(2, unPotion.description)
            requetePreparer?.setInt(3, unPotion.soin)

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
                unPotion.id = id // Mettez à jour l'ID de l'objet Qualite avec la valeur générée
                return unPotion
            }
        }
        requetePreparer.close()

        return null
    }
    fun saveAll(lesPotions:Collection<Potion>):MutableMap<String, Potion>{
        var result= mutableMapOf<String, Potion>()
        for (unePotion in lesPotions){
            val potionSauvegarde=this.save(unePotion)
            if (potionSauvegarde!=null){
                result.set(potionSauvegarde.nom.lowercase(),potionSauvegarde)
            }
        }
        return result
    }
    fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM Potion WHERE id = ?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setInt(1, id)
        try {
            val nbLigneMaj = requetePreparer?.executeUpdate()
            requetePreparer.close()
            if(nbLigneMaj!=null && nbLigneMaj>0){
                return true
            }else{
                return false
            }
        } catch (erreur: SQLException) {
            println("Une erreur est survenue lors de la suppression de la qualité : ${erreur.message}")
            return false
        }
    }

}

