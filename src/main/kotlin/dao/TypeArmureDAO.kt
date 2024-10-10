import jdbc.BDD
import model.item.TypeArmure

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
                result.set(nom.lowercase(), TypeArmure(id ,nom, bonusType))
            }
        }
        requetePreparer.close()
        return result
    }
}