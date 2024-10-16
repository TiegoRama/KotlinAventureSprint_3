import jdbc.BDD
import model.item.Armure
import model.item.TypeArmure
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

class TypeArmureDAO(val bdd: BDD =coBDD ) {

    fun findAll(): MutableMap<String, TypeArmure> {
        val result = mutableMapOf<String, TypeArmure>()

        val sql = "SELECT * FROM typeArmure"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("type_Armure_id")
                val nom = resultatRequete.getString("nom")
                val bonusType = resultatRequete.getInt("bonusRarete")
                result.set(nom.lowercase(), TypeArmure(id, nom, bonusType))
            }
        }
        requetePreparer.close()
        return result
    }

    fun findByNom(nomRechecher: String): MutableMap<String, TypeArmure> {
        val result = mutableMapOf<String, TypeArmure>()

        val sql = "SELECT * FROM typeArmure WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("type_Armure_id")
                val nom = resultatRequete.getString("nom")
                val bonusType = resultatRequete.getInt("bonusRarete")
                result.set(nom.lowercase(), TypeArmure(id, nom, bonusType))
            }
        }
        requetePreparer.close()
        return result
    }

    fun findById(id: Int): TypeArmure? {
        var result: TypeArmure? = null
        val sql = "SELECT * FROM typeArmure WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("type_Armure_id")
                val nom = resultatRequete.getString("nom")
                val bonusType = resultatRequete.getInt("bonusRarete")
                result = TypeArmure(id, nom, bonusType)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }

    fun save(uneTypeArmure: TypeArmure): TypeArmure? {

        val requetePreparer: PreparedStatement

        if (uneTypeArmure.id == null) {
            val sql =
                "Insert Into typeArmure (nom,bonusType) values (?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, uneTypeArmure.nom)
            requetePreparer?.setInt(2, uneTypeArmure.bonusType)
        } else {
            var sql = ""
            sql =
                "Update  TypeArmure set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            requetePreparer?.setString(1, uneTypeArmure.nom)
            requetePreparer?.setInt(2, uneTypeArmure.bonusType)
            requetePreparer?.setInt(4, uneTypeArmure.id!!)
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
                uneTypeArmure.id = id // Mettez à jour l'ID de l'objet TypeArmure avec la valeur générée
                return uneTypeArmure
            }
        }

        requetePreparer.close()

        return null
    }
    fun saveAll(lesTypeArmures: Collection<TypeArmure>): MutableMap<String, TypeArmure> {
        var result = mutableMapOf<String, TypeArmure>()
        for (uneTypeArmure in lesTypeArmures) {
            val TypeArmureSauvegarde = this.save(uneTypeArmure)
            if (TypeArmureSauvegarde != null) {
                result.set(TypeArmureSauvegarde.nom.lowercase(), TypeArmureSauvegarde)
            }
        }
        return result
    }

    fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM TypeArmure WHERE id = ?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setInt(1, id)
        try {
            val nbLigneMaj = requetePreparer?.executeUpdate()
            requetePreparer.close()
            if (nbLigneMaj != null && nbLigneMaj > 0) {
                return true
            } else {
                return false
            }
        } catch (erreur: SQLException) {
            println("Une erreur est survenue lors de la suppression de la qualité : ${erreur.message}")
            return false
        }
    }
}
