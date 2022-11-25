import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class ToDos {
  // Application Variables Here

  public static void renderFrame(JFrame frame) {
    frame.setSize(300, 400);
    frame.setTitle("An empty Frame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public static JFrame createMainFrame() {
    JFrame frame = new JFrame();
    return frame;
  }

  public static JButton createButton() {
    class ClickListener implements ActionListener {

      // Button Variables Here
      public void actionPerformed(ActionEvent event) {
        System.out.println("You clicked me!");
      }
    }
    JButton button = new JButton("Click Me!");
    ActionListener listener = new ClickListener();
  
    button.addActionListener(listener);
    return button;
  }

  public static void main(String[] args) {
    System.out.println("Launching ToDo Application...");

    JFrame mainFrame = createMainFrame();

    mainFrame.add(createButton());

    renderFrame(mainFrame);
    System.out.println("ToDo Application Opened!");
  }
}

