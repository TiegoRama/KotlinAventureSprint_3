package dao
import coBDD
import jdbc.BDD
import model.item.Armure

class ArmureDAO(val bdd: BDD =coBDD ) { fun findAll(): MutableMap<String, Armure> {
    val result = mutableMapOf<String, Armure>()

    val sql = "SELECT * FROM Armure"
    val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
    val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
    if (resultatRequete != null) {
        while (resultatRequete.next()) {
            val  id = resultatRequete.getInt("id_Armure")
            val nom = resultatRequete.getString("nom")
            val qualite = resultatRequete.getInt("fk_id_qualite")
            val description = resultatRequete.getString("description")
            val typeArmure = resultatRequete.getInt("fk_id_typeArmure")
            result.set(nom.lowercase(), Armure(id,nom,description,qualite,typeArmure))
        }
    }
    requetePreparer.close()
    return result
}
}