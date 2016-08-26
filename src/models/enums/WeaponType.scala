package models.enums

import utils.WeaponRange

object WeaponType extends Enumeration {
  val LIGHT_LASER,
      QUAD_LASER,
      PULSE_LASER,
      BEAM_LASER,
      TURBO_LASER,
      NEUTRON_GUN,
      IMPULSE_GUN,
      PHOTON_BLASTER,
      MEZON_GUN,
      PARALYSER,
      ANTI_MATTER_RAY,
      TORPEDO,
      ADVANCED_TORPEDO, HEAVY_TORPEDO = Value
  def isEnergyWeapon(weaponType: WeaponType.Value): Boolean ={
    weaponType match {
      case LIGHT_LASER => true
      case QUAD_LASER => true
      case PULSE_LASER => true
      case BEAM_LASER => true
      case TURBO_LASER => true
      case _ => false
    }
  }
  def firePower(weaponType: WeaponType.Value): Int = {
    weaponType match {
      case LIGHT_LASER => 10
      case QUAD_LASER => 15
      case PULSE_LASER => 25
      case BEAM_LASER => 40
      case TURBO_LASER => 65
      case NEUTRON_GUN => 20
      case IMPULSE_GUN => 40
      case PHOTON_BLASTER => 80
      case MEZON_GUN => 150
      case PARALYSER => 50 // EXTRA EFFECT - X% CHANCE TO DISABLE ENEMY NEXT TURN
      case ANTI_MATTER_RAY => 600 // EXTRA EFFECT - AREA DAMAGE + DELAY OF X TURNS
      case TORPEDO => 20 // EXTRA EFFECT - CAN FIRE UP TO X PER TURN (3?), X% CHANCE OF MISSING TARGET
      case ADVANCED_TORPEDO => 40 // EXTRA EFFECT - CAN FIRE UP TO X PER TURN (4?), X% CHANCE OF MISSING TARGET
      case HEAVY_TORPEDO => 60 // EXTRA EFFECT - CAN FIRE UP TO X PER TURN (4?), X% CHANCE OF MISSING TARGET
      case _ => 0
    }
  }
  def random(): WeaponType.Value = {
    val weapons = Seq(LIGHT_LASER,
        QUAD_LASER,
        PULSE_LASER,
        BEAM_LASER,
        TURBO_LASER,
        NEUTRON_GUN,
        IMPULSE_GUN,
        PHOTON_BLASTER,
        MEZON_GUN,
        PARALYSER,
        ANTI_MATTER_RAY,
        TORPEDO,
        ADVANCED_TORPEDO, HEAVY_TORPEDO)
    weapons(util.Random.nextInt(weapons.length))
  }
  def getRange(weaponType: WeaponType.Value): WeaponRange = {
    weaponType match {
      case LIGHT_LASER => WeaponRange(4,7)
      case QUAD_LASER => WeaponRange(4,8)
      case PULSE_LASER => WeaponRange(4,9)
      case BEAM_LASER => WeaponRange(4,10)
      case TURBO_LASER => WeaponRange(4,11)
      case NEUTRON_GUN => WeaponRange(7,9)
      case IMPULSE_GUN => WeaponRange(7,10)
      case PHOTON_BLASTER => WeaponRange(7,11)
      case MEZON_GUN => WeaponRange(7,12)
      case PARALYSER => WeaponRange(7,12)
      case ANTI_MATTER_RAY => WeaponRange(9,13)
      case TORPEDO => WeaponRange(5,7)
      case ADVANCED_TORPEDO => WeaponRange(5,8)
      case HEAVY_TORPEDO => WeaponRange(5,9)
      case _ => WeaponRange(0,0)
    }
  }
}