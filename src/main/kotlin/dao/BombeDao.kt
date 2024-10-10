package dao
import jdbc.BDD
import coBDD
import model.item.Bombe
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

/**
 * Cette classe représente un repository pour les objets Bombe, permettant d'effectuer des opérations de
 * recherche et de sauvegarde dans la base de données.
 *
 * @param bdd L'objet de base de données à utiliser pour les opérations de base de données.
 */
class BombeDao(val bdd: BDD=coBDD ) {

    /**
     * Recherche et retourne toutes les qualités de la base de données.
     *
     * @return Une liste de toutes les qualités trouvées.
     */
    fun findAll(): MutableMap<String, Bombe> {
        val result = mutableMapOf<String, Bombe>()

        val sql = "SELECT * FROM Bombe"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id_bombe")
                val nom = resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val MaxDe = resultatRequete.getInt("maxDe")
                val nombreDeDes = resultatRequete.getInt("nombreDeDes")
                result.set(nom.lowercase(), Bombe(id, nom, description, MaxDe, nombreDeDes))
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Recherche et retourne une bombe par nom (retourne la première correspondance).
     *
     * @param nomRechecher Le nom à rechercher.
     * @return La première bombe correspondant au nom donné, ou null si aucune n'est trouvée.
     */
    fun findByNom(nomRechecher: String): MutableMap<String, Bombe> {
        val result = mutableMapOf<String, Bombe>()

        val sql = "SELECT * FROM Bombe WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id_bombe")
                val nom = resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val nombreDeDes = resultatRequete.getInt("nombreDeDes")
                val MaxDe = resultatRequete.getInt("maxDe")
                result.set(nom.lowercase(), Bombe(id, nom, description, MaxDe, nombreDeDes))
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Recherche et retourne une qualité par nom (retourne la première correspondance).
     *
     * @param Int L'id à rechercher.
     * @return La première qualité correspondant au nom donné, ou null si aucune n'est trouvée.
     */
    fun findById(id: Int): Bombe? {
        var result: Bombe? = null
        val sql = "SELECT * FROM Bombe WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id_bombe")
                val nom = resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val MaxDe = resultatRequete.getInt("maxDe")
                val nombreDeDes = resultatRequete.getInt("nombreDeDes")
                result = Bombe(id, nom, description, MaxDe, nombreDeDes)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Sauvegarde une qualité dans la base de données.
     *
     * @param uneBombe L'objet Bombe à sauvegarder.
     * @return L'objet Bombe sauvegardé, y compris son ID généré, ou null en cas d'échec.
     */
    fun save(uneBombe: Bombe): Bombe? {

        val requetePreparer: PreparedStatement

        if (uneBombe.id == null) {
            val sql =
                "Insert Into Bombe (nom,description,MaxDe,nombreDe) values (?,?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, uneBombe.nom)
            requetePreparer?.setString(2, uneBombe.description)
            requetePreparer?.setInt(3, uneBombe.maxDe)
            requetePreparer?.setInt(4, uneBombe.nombreDeDes)
        } else {
            var sql = ""
            sql =
                "Update  Bombe set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            requetePreparer?.setString(1, uneBombe.nom)
            requetePreparer?.setString(2, uneBombe.description)
            requetePreparer?.setInt(3, uneBombe.maxDe)
            requetePreparer?.setInt(4, uneBombe.nombreDeDes)
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
                uneBombe.id = id // Mettez à jour l'ID de l'objet Bombe avec la valeur générée
                return uneBombe
            }
        }
        requetePreparer.close()

        return null
    }

    /**
     * Sauvegarde toutes les qualités dans la liste dans la base de données.
     *
     * @param lesBombes La liste des objets Bombe à sauvegarder.
     * @return Une liste des objets Bombe sauvegardés, y compris leurs ID générés, ou null en cas d'échec.
     */
    fun saveAll(lesBombes: Collection<Bombe>): MutableMap<String, Bombe> {
        var result = mutableMapOf<String, Bombe>()
        for (uneBombe in lesBombes) {
            val BombeSauvegarde = this.save(uneBombe)
            if (BombeSauvegarde != null) {
                result.set(BombeSauvegarde.nom.lowercase(), BombeSauvegarde)
            }
        }
        return result
    }

    /**
     * Supprime une qualité de la base de données en fonction de son ID.
     *
     * @param id L'ID de la qualité à supprimer.
     * @return `true` si la qualité a été supprimée avec succès, sinon `false`.
     */
    fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM Bombe WHERE id = ?"
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