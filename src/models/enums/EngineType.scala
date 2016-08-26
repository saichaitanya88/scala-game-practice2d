package models.enums

object EngineType extends Enumeration{
  val HYPERDRIVE_1,  
      HYPERDRIVE_2,
      HYPERDRIVE_3,
      HYPERDRIVE_4,
      HYPERDRIVE_5 = Value
  def moveDistanceBonus(engineType: EngineType.Value) : Int = {
    engineType match {
      case HYPERDRIVE_2 => 1
      case HYPERDRIVE_3 => 2
      case HYPERDRIVE_4 => 3
      case HYPERDRIVE_5 => 4
      case _ => 0
    }
  }
}