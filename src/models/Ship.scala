package models

import models.enums._
import java.awt.Point
import java.awt.Graphics2D
import ui.elements.Grid

class Ship(
    var x: Int, var y: Int, 
    val shipType: ShipType.Value,
    var shield: ShieldType.Value,
    var weapon: WeaponType.Value,
    var engineType: EngineType.Value,
    private var hitPoints: Int
) extends Entity {
  
  override var entityType: EntityType.Value = EntityType.SHIP
  var cargoHold: List[CargoEquipment.Value] = List[CargoEquipment.Value]()
  var equippedCargo: List[CargoEquipment.Value] = List[CargoEquipment.Value]()
  
  override def printInfo(){
    println(toString)
  }
  override def toString(): String = {
    s"""${x},${y}
      ${hitPoints}HP
      ShipType: ${shipType}
      Weapon: ${weapon} FP: ${firePower}
      Shield: ${shield}
      Engine: ${engineType}"""
  }
  def getHp(): Int= {
    hitPoints
  }
  /** Take Damage */
  def takeDamage(attacker: Ship, distance: Double){
    val attackerFP = attacker.firePower
    val defenceBonus = getDefenceBonus(attacker)
    val dissipatedDamage = WeaponType.getRange(weapon).dissipatedDamage(distance)
    val damageTaken = Math.max((attackerFP - (attackerFP * dissipatedDamage)) - defenceBonus, 0)
    if (Math.random() > defenceBonus)
      hitPoints = hitPoints - damageTaken.toInt
  }
  /** Calculates FP based this Ship's Equips and Weapons*/
  def firePower(): Double = {
    WeaponType.firePower(weapon)
  }
  /** Calculates chance of enemy attack missing */
  def getDefenceBonus(attacker: Ship): Double = {
    val defenceBonus = ShieldType.getDefenceBonus(this, attacker)
    defenceBonus
  }
  def moveTo(loc: Point){
    x = loc.x
    y = loc.y
  }
  def getMovementRange() : Int = {
    ShipType.baseMoveDistance(shipType) + EngineType.moveDistanceBonus(engineType)
  }
  def draw(g: Graphics2D, pos: Point){
    g.drawString(EntityType.code(entityType), pos.x, pos.y+5)
  }
}

object Ship{
  import Ship._
  def apply(x: Int, y:Int, shipType:ShipType.Value, entityType: EntityType.Value): Ship = {
    val shieldType = ShieldType.random()
    val hp = ShipType.baseHP(shipType) + ShieldType.bonusHP(ShipType.baseHP(shipType), shieldType)
    val ship = new Ship(x, y, shipType, shieldType, WeaponType.random(), EngineType.HYPERDRIVE_1, hp)
    ship.entityType = entityType
    ship
  }
}