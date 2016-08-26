package models.enums

import models.Ship

object ShieldType extends Enumeration{
  val 
      /** Increases HP by 15% at BattleStart & Laser Damage -10%*/
      ENERGY_SHIELD,
      /** Increases HP by 30% at BattleStart & Laser Damage -20%*/
      KINETIC_SHIELD,
      /** Increases HP by 40% at BattleStart & Laser Damage -30% */
      INERTIA_SHIELD,
      /** Increases HP by 60% at BattleStart & Laser Damage -45% & Neutron,Photon -12% */
      MAGNETIC_SHIELD,
      /** Increases HP by 80% at BattleStart & Laser Damage -75% & Neutron,Photon -36% */
      GRAVITY_SHIELD,
      /** Increases HP by 100% at BattleStart & Laser Damage -95% & Neutron,Photon -72% */
      DEFLECTOR_SHIELD = Value
  def random(): ShieldType.Value = {
    val shields = Seq(ENERGY_SHIELD,
      KINETIC_SHIELD,
      INERTIA_SHIELD,
      MAGNETIC_SHIELD,
      GRAVITY_SHIELD,
      DEFLECTOR_SHIELD)
      shields(util.Random.nextInt(shields.length))
  }
  def bonusHP(baseHp: Int, shieldType: ShieldType.Value): Int = {
    shieldType match {
      case ENERGY_SHIELD => (baseHp * 0.15).toInt
      case KINETIC_SHIELD => (baseHp * 0.30).toInt
      case INERTIA_SHIELD => (baseHp * 0.40).toInt
      case MAGNETIC_SHIELD => (baseHp * 0.60).toInt
      case GRAVITY_SHIELD => (baseHp * 0.80).toInt
      case DEFLECTOR_SHIELD => (baseHp * 0.100).toInt
      case _ => 0
    }
  }
  def getDefenceBonus(targetShip: Ship, attacker: Ship): Double = {
    targetShip.shield match {
      case ShieldType.ENERGY_SHIELD => {
        if (WeaponType.isEnergyWeapon(attacker.weapon)) 0.1
        else 0
      }
      case ShieldType.KINETIC_SHIELD => { 
        if (WeaponType.isEnergyWeapon(attacker.weapon)) 0.2
        else 0
      }
      case ShieldType.INERTIA_SHIELD => { 
        if (WeaponType.isEnergyWeapon(attacker.weapon)) 0.3
        else 0
      }
      case ShieldType.MAGNETIC_SHIELD => { 
        if (WeaponType.isEnergyWeapon(attacker.weapon)) 0.45
        // Non Energy Weapons - 
        else 0.12
      }
      case ShieldType.GRAVITY_SHIELD => { 
        if (WeaponType.isEnergyWeapon(attacker.weapon)) 0.75
        // Non Energy Weapons - 
        else 0.36
      }
      case ShieldType.DEFLECTOR_SHIELD => { 
        if (WeaponType.isEnergyWeapon(attacker.weapon)) 0.95
        //Non Energy Weapons - 
        else 0.6
      }
      case _ => 0
    }
  }
}
