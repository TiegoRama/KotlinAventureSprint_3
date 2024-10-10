package dao
import coBDD
import jdbc.BDD
import model.item.Armure
import qualiteRepository
import typeArmureRepository

class ArmureDAO(val bdd: BDD =coBDD ) { fun findAll(): MutableMap<String, Armure> {
    val result = mutableMapOf<String, Armure>()

    val sql = "SELECT * FROM Armure"
    val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
    val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
    if (resultatRequete != null) {
        while (resultatRequete.next()) {
            val  id = resultatRequete.getInt("id_Armure")
            val nom = resultatRequete.getString("nom")
            val idQualite = resultatRequete.getInt("fk_id_qualite")
            val description = resultatRequete.getString("description")
            val idTypeArmure = resultatRequete.getInt("fk_id_typeArmure")
            val laQualite= qualiteRepository.findById(idQualite)
            val leTypeArmure = typeArmureRepository.findById(idTypeArmure)

            result.set(nom.lowercase(), Armure(id,nom,description,leTypeArmure!!,laQualite!!)) 
        }
    }


    requetePreparer.close()
    return result
}
}