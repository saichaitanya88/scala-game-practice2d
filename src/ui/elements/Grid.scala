package ui.elements

import java.awt.Graphics2D
import java.awt.Color
import java.awt.Point
import java.awt.Stroke
import java.awt.geom.Rectangle2D

class Grid(val width: Int, val height: Int) extends UIElement {
  val gridCount = 16
  def maxLineSize = Math.min(width, height)
  val gridSize = maxLineSize / gridCount
  var highlightedGridLoc: Point = null
  println(s"gridSize: ${gridSize}")
  def draw(g: Graphics2D){
    g.setColor(Color.green)
    drawLines(g)
    drawSelectedGridLoc(g)
  }
  def drawLines(g: Graphics2D){
    List.range(0, gridCount+1).foreach(x => {
      g.drawLine(0, gridSize * x, maxLineSize, gridSize * x)
      g.drawLine(gridSize * x, 0, gridSize * x, maxLineSize)
    })
  }
  def drawSelectedGridLoc(g: Graphics2D){
    if (highlightedGridLoc != null){
      g.fill(new Rectangle2D.Double(highlightedGridLoc.x, highlightedGridLoc.y, gridSize, gridSize))
      //println(s"hgl: ${highlightedGridLoc}")
    }
  }
  def setSelectedGridLoc(p: Point){
    val x = p.x - (p.x % gridSize)
    val y = p.y - (p.y % gridSize)
    highlightedGridLoc = new Point(x,y)
  }
}