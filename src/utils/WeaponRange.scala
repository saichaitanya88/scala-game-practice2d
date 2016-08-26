package utils

class WeaponRange(val min: Int, val max: Int){
  def dissipatedDamage(distance: Double): Double = {
    if (distance <= min.toDouble){
      0
    }
    else if (distance > max.toDouble){
      1
    }
    else {
      Math.max(1 - (distance-min.toDouble) / (max-min).toDouble, 0)
    }
  }
}

object WeaponRange {
  def apply(min: Int, max: Int): WeaponRange = {
    new WeaponRange(min, max)
  }
}