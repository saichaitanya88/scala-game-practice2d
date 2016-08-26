import javax.swing.JFrame
import ui.BasicBoard


class Application extends JFrame{
  initUI()
  def initUI(){
    setSize(1280,720)
    add(new BasicBoard())
    setTitle("Practice2d")
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setLocationRelativeTo(null)
    setResizable(false)
  }
}