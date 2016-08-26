package ui.elements

import java.awt.Graphics2D

trait UIElement {
  def draw(g: Graphics2D)
}