package models

import models.enums.EntityType
import java.awt.Graphics2D
import java.awt.Point

trait Entity {
  var x: Int
  var y: Int
  var entityType: EntityType.Value
  def printInfo() {
    print(s"${EntityType.code(entityType)} - ${x},${y}")
  }
  def draw(g: Graphics2D, p: Point) : Unit
}