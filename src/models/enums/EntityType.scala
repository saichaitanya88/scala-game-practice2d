package models.enums

object EntityType extends Enumeration {
  val SHIP, PLAYER_SHIP, ENEMY_SHIP = Value
  def code(et: EntityType.Value) : String ={
    if (et == SHIP) "S"
    else if (et == PLAYER_SHIP) "P"
    else if (et == ENEMY_SHIP) "E"
    else "."
  }
}