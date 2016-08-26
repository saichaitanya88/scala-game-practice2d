package listeners

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class GridMouseListener extends MouseAdapter{
  var mouseClickedEvts: List[(String, (MouseEvent) => Unit)] = List[(String, (MouseEvent) => Unit)]()
  
  def addMouseClickEvent(name: String, fn: (MouseEvent) => Unit){
    mouseClickedEvts = (name, fn) :: mouseClickedEvts
  }
  
  override def mouseClicked(e: MouseEvent){
    mouseClickedEvts.foreach(m => m._2(e))
  }
}