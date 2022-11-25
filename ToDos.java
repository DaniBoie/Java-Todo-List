import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

class ToDoItem {
  public String name;
  public int priority;
  public LocalDate deadline;
  public String notes;

  ToDoItem(String name, int priority, LocalDate deadline, String notes) {
    this.name = name;
    this.priority = priority;
    this.deadline = deadline;
    this.notes = notes;
  }
}

class ToDoFrame extends JFrame {
  private ArrayList<String> listData;
  private HashMap<String, ToDoItem> listItems;


  class LeftPanel extends JPanel {
    private JList<String> list;

    class Click implements MouseListener {
      // Button Variables Here
      public void mouseReleased(MouseEvent event) {

        System.out.println("You clicked me!");
        System.out.println(list.getSelectedValue());

      }
      public void mouseClicked(MouseEvent event) {}
      public void mouseExited(MouseEvent event) {}
      public void mouseEntered(MouseEvent event) {}
      public void mousePressed(MouseEvent event) {}
    }

    LeftPanel() {
      setLayout(new BorderLayout());
      add(createList(), BorderLayout.CENTER);


      add(createButtons(), BorderLayout.EAST);
    }

    private JList<String> createList() {
      String[] typeControl = {};
      list = new JList<String>(listData.toArray(typeControl));
      list.addMouseListener(new Click());
      return list;
    }

    private JPanel createButtons() {

      JPanel buttonPanel = new JPanel(new GridLayout(2, 1));

      JPanel topButton = new JPanel(new BorderLayout());
      // Done button
      topButton.add(new JButton("Click Me"), BorderLayout.SOUTH);
      buttonPanel.add(topButton);

      // Delete button
      JPanel bottomButton = new JPanel(new BorderLayout());
      bottomButton.add(new JButton("Click Me"), BorderLayout.NORTH);
      buttonPanel.add(bottomButton);
      
      return buttonPanel;
    }

  }


  ToDoFrame() {
    listData = new ArrayList<String>();
    listData.add("EXAMPLE TEXT");
    listData.add("SOME MORE TEXT");
    
    setLayout(new GridLayout(1, 2));

    add(new LeftPanel());

    

  }
}

class ToDos {

  public static void renderFrame(JFrame frame) {
    frame.setSize(300, 400);
    frame.setTitle("To Do Frame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
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

    // Instantiating Main Layout Components

    JFrame mainFrame = new ToDoFrame();
    JPanel rightPanel = createRightPanel();
    
    // Adding Item Interface to Right Panel
    rightPanel.add(createButton());
    rightPanel.add(createButton());
    rightPanel.add(createButton());
    rightPanel.add(createButton());
    rightPanel.add(createButton());

    // Mounting Finished Components
    mainFrame.add(rightPanel);

    // Rendering Finished Application
    renderFrame(mainFrame);
    System.out.println("ToDo Application Opened!");
  }
}

