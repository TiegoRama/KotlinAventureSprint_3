package dao
import coBDD
import jdbc.BDD
import model.item.Arme
import dao.TypeArmeDAO
import model.item.TypeArme
import qualiteRepository
import qualites
import typearmeRepository
import typearmes
import java.sql.PreparedStatement
import java.sql.Statement


class ArmeDAO (val bdd: BDD =coBDD ) { fun findAll(): MutableMap<String, Arme> {
    val result = mutableMapOf<String, Arme>()

    val sql = "SELECT * FROM arme"
    val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
    val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
    if (resultatRequete != null) {
        while (resultatRequete.next()) {
            val id = resultatRequete.getInt("id")
            val nom = resultatRequete.getString("nom")
            val type = resultatRequete.getInt("fk_id_type")
            val description = resultatRequete.getString("couleur")
            val qualite = resultatRequete.getInt("fk_id_qualite")
            val laQualite= qualiteRepository.findById(qualite)
            val leTypeArme = typearmeRepository.findById(type)


            result.set(nom.lowercase(), Arme(id, nom, description, leTypeArme!! , laQualite!!))
        }
    }
    requetePreparer.close()
    return result
}
    fun findByNom(nomRechecher:String): MutableMap<String, Arme> {
        val result = mutableMapOf<String, Arme>()

        val sql = "SELECT * FROM Arme WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val fk_id_type_arme = resultatRequete.getInt("fk_id_type")
                val typearmes = typearmeRepository.findById(fk_id_type_arme)!!
                val fk_id_qualite = resultatRequete.getInt("fk_id_qualite")
                val qualite = qualiteRepository.findById(fk_id_qualite)!!
                result.set(nom.lowercase(), Arme(id,nom,description , typearmes, qualite))
            }
        }
        requetePreparer.close()
        return result
    }
    fun findById(id:Int): Arme? {
        var result :Arme?=null
        val sql = "SELECT * FROM Arme WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val fk_id_type_arme = resultatRequete.getInt("fk_id_type")
                val typearmes = typearmeRepository.findById(fk_id_type_arme)!!
                val fk_id_qualite = resultatRequete.getInt("fk_id_qualite")
                val qualite = qualiteRepository.findById(fk_id_qualite)!!
                result=Arme(id,nom,description , typearmes, qualite)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }
    fun save(uneArme: Arme): Arme? {

        val requetePreparer: PreparedStatement


        if (uneArme.id == null) {
            val sql =
                "Insert Into Arme (nom,description,fk_id_type,fk_id_qualite) values (?,?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, uneArme.nom)
            requetePreparer?.setString(2, uneArme.description)
            requetePreparer?.setInt(3,uneArme.type)
            requetePreparer?.setInt(4,uneArme.f)

        } else {
            var sql = ""
            sql =
                "Update  TypeArme set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, Arme.nom)
            requetePreparer?.setInt(2, Arme.nombreDes)
            requetePreparer?.setInt(3, Arme.valeurDeMax)
            requetePreparer?.setInt(4,Arme.multiplicateurCritique)
            requetePreparer?.setInt(5,Arme.activationCritique)

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
                Arme
.id = id // Mettez à jour l'ID de l'objet Qualite avec la valeur générée
                return Arme

            }
        }
        requetePreparer.close()

        return null
    }

}