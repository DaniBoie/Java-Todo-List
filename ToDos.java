import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridLayout;
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
    // can return custom JFrame subclass
    JFrame frame = new JFrame();
    frame.setLayout(new GridLayout(1, 2));
    // BORDER LAYOUT DEFAULT FOR JFRAMES BUT NOT JPANELS
    // panel.setLayout(new BorderLayout());
    // panel.add(component, ex. BorderLayout.NORTH);

    // FLOW LAYOUT DEFAULT FOR JPANELS

    // GRID LAYOUT
    // panel.setLayout(new GridLayout(row, col));
    // panel.add(component); placed in reading order
    return frame;
  }

  public static JPanel createLeftPanel() {

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    return panel;
  }

  public static JPanel createRightPanel() {

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(5, 1));
    
    return panel;
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
    JPanel leftPanel = createLeftPanel();
    JPanel rightPanel = createRightPanel();

    mainFrame.add(leftPanel);
    mainFrame.add(rightPanel);

    leftPanel.add(createButton(), BorderLayout.CENTER);
    leftPanel.add(createButton(), BorderLayout.EAST);
    
    rightPanel.add(createButton());
    rightPanel.add(createButton());
    rightPanel.add(createButton());
    rightPanel.add(createButton());
    rightPanel.add(createButton());


    renderFrame(mainFrame);
    System.out.println("ToDo Application Opened!");
  }
}

