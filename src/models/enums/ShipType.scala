package models.enums

object ShipType extends Enumeration{
  val FIGHTER,
      ADVANCED_FIGHTER,
      INTERCEPTOR,
      HEAVY_FIGHTER,
      DESTROYER,
      HEAVY_DESTROYER,
      CORVETTE,
      HEAVY_CORVETTE, 
      BATTLESHIP,
      CRUISER, HEAVY_CRUISER,
      TRANSPORT = Value
  def random() : ShipType.Value = {
    val values: Seq[ShipType.Value] = Seq(FIGHTER,ADVANCED_FIGHTER,INTERCEPTOR,HEAVY_FIGHTER,DESTROYER,HEAVY_DESTROYER,
      CORVETTE,HEAVY_CORVETTE,BATTLESHIP,TRANSPORT, CRUISER, HEAVY_CRUISER)
    values(util.Random.nextInt(values.length))
  }
  def baseHP(shipType: ShipType.Value) : Int = {
    shipType match {
      case FIGHTER => 10
      case ADVANCED_FIGHTER => 20
      case INTERCEPTOR => 30
      case HEAVY_FIGHTER => 50
      case DESTROYER => 100
      case HEAVY_DESTROYER => 150
      case CORVETTE => 250
      case HEAVY_CORVETTE => 300
      case CRUISER => 350
      case HEAVY_CRUISER => 400 
      case BATTLESHIP => 500
      case TRANSPORT => 5
      case _ => 1
    }
  }
  def baseMoveDistance(shipType: ShipType.Value) : Int = {
    shipType match {
      case FIGHTER => 8
      case ADVANCED_FIGHTER => 8
      case INTERCEPTOR => 8
      case HEAVY_FIGHTER => 8
      case DESTROYER => 5
      case HEAVY_DESTROYER => 5
      case CORVETTE => 5
      case HEAVY_CORVETTE => 5
      case CRUISER => 4
      case HEAVY_CRUISER => 4
      case BATTLESHIP => 3
      case TRANSPORT => 5
      case _ => 1
    }
  }
}