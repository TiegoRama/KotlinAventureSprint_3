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


class ArmeDAO (val bdd: BDD =coBDD ) { fun findAll(): MutableMap<String, Arme> {
    val result = mutableMapOf<String, Arme>()

    val sql = "SELECT * FROM arme"
    val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
    val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
    if (resultatRequete != null) {
        while (resultatRequete.next()) {
            val id = resultatRequete.getInt("id")
            val nom = resultatRequete.getString("nom")
            val type = resultatRequete.getInt("fk_ty")
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

}