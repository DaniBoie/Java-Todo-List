import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

class ToDoItem {
  public String name;
  public int priority;
  public LocalDate deadline;
  public String notes;
  public Boolean done;

  ToDoItem(String name, int priority, LocalDate deadline, String notes) {
    this.name = name;
    this.priority = priority;
    this.deadline = deadline;
    this.notes = notes;
    done = false;
  }
}

class ToDoFrame extends JFrame {
  private DefaultListModel<String> listData;
  private HashMap<String, ToDoItem> listItems;


  class LeftPanel extends JPanel {
    private JList<String> list;

    class ItemClick implements MouseListener {
      // Button Variables Here
      public void mouseReleased(MouseEvent event) {

        System.out.print("You clicked: ");
        System.out.println(list.getSelectedValue());

      }
      public void mouseClicked(MouseEvent event) {}
      public void mouseExited(MouseEvent event) {}
      public void mouseEntered(MouseEvent event) {}
      public void mousePressed(MouseEvent event) {}
    }

    class DoneListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        String selected = list.getSelectedValue();
        if (selected != null) {
          System.out.println("You completed: " + list.getSelectedValue());
          listItems.get(selected).done = true;
          // strikethrough
        }
      }
    }

    class DeleteListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        String selection = list.getSelectedValue();
        if (selection != null) {
          listData.removeElement(selection);
          listItems.remove(selection);
          System.out.println("Deleted: " + selection);
        }
      }
    }

    LeftPanel() {
      setLayout(new BorderLayout());
      add(createList(), BorderLayout.CENTER);
      add(createButtons(), BorderLayout.EAST);
    }

    private JList<String> createList() {

      listData = new DefaultListModel<>();

      list = new JList<String>(listData);
      list.addMouseListener(new ItemClick());
      return list;
    }

    private JPanel createButtons() {

      // Button Panel
      JPanel buttonPanel = new JPanel(new GridLayout(2, 1));

      // Done button
      JPanel topButtonPanel = new JPanel(new BorderLayout());
      JButton topButton = new JButton("Toggle Done");
      topButton.addActionListener(new DoneListener());
      topButtonPanel.add(topButton, BorderLayout.SOUTH);
      buttonPanel.add(topButtonPanel);

      // Delete button
      JPanel bottomButtonPanel = new JPanel(new BorderLayout());
      JButton bottomButton = new JButton("Delete");
      bottomButton.addActionListener(new DeleteListener());
      bottomButtonPanel.add(bottomButton, BorderLayout.NORTH);
      buttonPanel.add(bottomButtonPanel);
      
      return buttonPanel;
    }

  }

  class RightPanel extends JPanel {
    JTextField itemText;
    JTextField priorityText;
    JComboBox<String> monthDropdown;
    JComboBox<String> dayDropdown;
    JComboBox<String> yearDropdown;
    ArrayList<String> months30;

    class SaveListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        // get values saved in name label, priority label, and notes label
        // If no value for name INVALID
        // If no value for priority INVALID, must be > 0
        // Notes is optional
        // INVALID values, warning dialog "Invalid input!", else dialog "Item saved!"

        // create ToDoItem associtaed with values

        // add ToDoItem to HashMap

        // add item name to ToDoList

        // clear all inputs
        System.out.println("Item Name: " + itemText.getText());
        System.out.println("Item Priority: " + priorityText.getText());
        itemText.setText("");
        priorityText.setText("");
      }
    }

    class MonthListener implements ActionListener {      
      public void actionPerformed(ActionEvent event) {
        String selected = (String) monthDropdown.getSelectedItem();
        System.out.println("Month Selected: " + selected);
        
        int daysInMonth;
        if (months30.contains(selected)) {
          daysInMonth = 30;
        } else {
          if (selected == "Feburary") {
            daysInMonth = 28;
            if (Integer.parseInt((String) yearDropdown.getSelectedItem()) % 4 == 0) {
              daysInMonth = 29;
            }
          } else {
            daysInMonth = 31;
          }
        }

        dayDropdown.removeAllItems();
        for (int i = 1; i <= daysInMonth; i++) {
          dayDropdown.addItem(String.valueOf(i));
        }
      }
    }

    class YearListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        boolean isFeb = false;
        if (monthDropdown.getSelectedItem() == "Feburary") {
          isFeb = true;
        }

        if (isFeb) {
          int daysInMonth;

          if (Integer.parseInt((String) yearDropdown.getSelectedItem()) % 4 == 0) {
            daysInMonth = 29;
          } else {
           daysInMonth = 28;
          }

          dayDropdown.removeAllItems();
          for (int i = 1; i <= daysInMonth; i++) {
            dayDropdown.addItem(String.valueOf(i));
          }
        }
      }
    }

    private JPanel createItemPanel() {
      JPanel itemPanel = new JPanel();

      JLabel itemLabel = new JLabel("Item: ");
      itemText = new JTextField(10);


      itemPanel.add(itemLabel);
      itemPanel.add(itemText);

      return itemPanel;
    }

    private JPanel createPriorityPanel() {
      JPanel priorityPanel = new JPanel();

      JLabel priorityLabel = new JLabel("Priority: ");
      priorityText = new JTextField(5);

      priorityPanel.add(priorityLabel);
      priorityPanel.add(priorityText);

      return priorityPanel;
    }

    private JPanel createDeadlinePanel() {
      JPanel deadlinePanel = new JPanel();
      String[] months = {"January", "Feburary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

      JRadioButton deadlineButton = new JRadioButton("Deadline");

      monthDropdown = new JComboBox<String>();
      for (String month : months) {
        monthDropdown.addItem(month);
      }
      monthDropdown.addActionListener(new MonthListener());


      dayDropdown = new JComboBox<String>();
      for (int i = 1; i <= 31; i ++) {
        dayDropdown.addItem(String.valueOf(i));
      }

      yearDropdown = new JComboBox<String>();
      for (int i = 2022; i <= 2031; i++) {
        yearDropdown.addItem(String.valueOf(i));
      }
      yearDropdown.addActionListener(new YearListener());


      deadlinePanel.add(deadlineButton);

      deadlinePanel.add(new JLabel("Month: "));
      deadlinePanel.add(monthDropdown);

      deadlinePanel.add(new JLabel("Day: "));
      deadlinePanel.add(dayDropdown);

      deadlinePanel.add(new JLabel("Year: "));
      deadlinePanel.add(yearDropdown);

      return deadlinePanel;
    }

    private JPanel createNotesPanel() {
      JPanel notesPanel = new JPanel();

      JTextArea textarea = new JTextArea(3, 25);
      JScrollPane notesPane = new JScrollPane(textarea);
      
      notesPanel.add(new JLabel("Notes: "));
      notesPanel.add(notesPane);

      return notesPanel;
    }

    private JPanel createButtons() {
      JPanel buttonPanel = new JPanel();

      JButton saveButton = new JButton("Save Item");
      saveButton.addActionListener(new SaveListener());

      JButton newButton = new JButton("New Item");
      newButton.addActionListener(new SaveListener());

      buttonPanel.add(saveButton);
      buttonPanel.add(newButton);

      return buttonPanel;
    }

    RightPanel() {

      months30 = new ArrayList<String>();
      String[] months30Static = {"April", "June", "September", "November"};
      months30.addAll(Arrays.asList(months30Static));

      setLayout(new GridLayout(5, 1));
      add(createItemPanel());
      add(createPriorityPanel());
      add(createDeadlinePanel());
      add(createNotesPanel());
      add(createButtons());
    }
  }


  ToDoFrame() {

    listItems = new HashMap<>();
    
    setLayout(new GridLayout(1, 2));

    add(new LeftPanel());
    add(new RightPanel());
    

  }
}

class ToDos {

  public static void renderFrame(JFrame frame) {
    frame.setSize(1000, 700);
    frame.setTitle("To Do Frame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    // Instantiating Application Frame
    System.out.println("Launching ToDo Application...");
    JFrame mainFrame = new ToDoFrame();

    // Rendering Application Frame
    renderFrame(mainFrame);
    System.out.println("ToDo Application Opened!");
  }
}

