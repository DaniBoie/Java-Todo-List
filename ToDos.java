import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

class Deadline {
  public String month;
  public int day;
  public int year;

  Deadline(String month, int day, int year) {
    this.month = month;
    this.day = day;
    this.year = year;
  }
}

class ToDoItem {
  public String name;
  public int priority;
  public Deadline deadline;
  public String notes;
  public Boolean done;
  public Boolean hasDeadline;

  ToDoItem(String name, int priority, Deadline deadline, String notes, Boolean hasDeadline) {
    this.name = name;
    this.priority = priority;
    this.deadline = deadline;
    this.notes = notes;
    this.hasDeadline = hasDeadline;
    done = false;
  }
}

class ToDoFrame extends JFrame {
  private DefaultListModel<String> listData;
  private HashMap<String, ToDoItem> listItems;
  private LeftPanel leftPanel;
  private RightPanel rightPanel;
  private boolean isNew = true;


  class LeftPanel extends JPanel {
    private JList<String> list;

    class ItemClick implements MouseListener {
      // Button Variables Here
      public void mouseReleased(MouseEvent event) {
        String pattern = "^<html><strike>(.*)</strike></html>$";
        Pattern strikeoutPattern = Pattern.compile(pattern);

        String selectedText = list.getSelectedValue();
        System.out.print("You clicked: " + selectedText);

        Matcher m = strikeoutPattern.matcher(selectedText);

        if (m.find()) {
          selectedText = m.group(1);
        }

        ToDoItem selectedItem = listItems.get(selectedText);
        
        isNew = false;
        rightPanel.populate(selectedItem);
      }
      public void mouseClicked(MouseEvent event) {}
      public void mouseExited(MouseEvent event) {}
      public void mouseEntered(MouseEvent event) {}
      public void mousePressed(MouseEvent event) {}
    }

    class DoneListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {

        String selectedText = list.getSelectedValue();

        if (selectedText != null) {

          selectedText = matchStrikeThrough(selectedText);
        
          System.out.println("You completed: " + selectedText);

          ToDoItem item = listItems.get(selectedText);

          if (item.done) {
            item.done = false;
          } else {
            item.done = true;
          }

          reorganize();
        }
      }
    }

    class DeleteListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        String selection = list.getSelectedValue();
        if (selection != null) {
          listData.removeElement(selection);
          listItems.remove(matchStrikeThrough(selection));
          rightPanel.clearFields();
          System.out.println("Deleted: " + selection);
        }
      }
    }

    LeftPanel() {
      setLayout(new BorderLayout());
      add(createList(), BorderLayout.CENTER);
      add(createButtons(), BorderLayout.EAST);
    }

    private void reorganize() {
      DefaultListModel<String> organizedData = new DefaultListModel<>();
      TreeMap<Integer, ArrayList<String>> dataMap = new TreeMap<>();

      ListModel<String> oldData = list.getModel();
      ArrayList<String> done = new ArrayList<>();

      for (int i = 0; i < oldData.getSize(); i++) {
        String itemName = oldData.getElementAt(i);
        itemName = matchStrikeThrough(itemName);
        ToDoItem item = listItems.get(itemName);

        if (item.done) {
          done.add(itemName);
        } else {

          ArrayList<String> priorityArray = dataMap.get(item.priority);

          if (priorityArray != null) {
            priorityArray.add(itemName);
          } else {
            priorityArray = new ArrayList<>();
            priorityArray.add(itemName);
            dataMap.put(item.priority, priorityArray);
          }

        }
      }

      for (ArrayList<String> items : dataMap.values()) {
        System.out.println("Priority List -> " + items);

        Collections.sort(items);

        organizedData.addAll(items);

        // for (String item : items) {
        // // if string matches done REGEX string replace with regular text.
        // // item = matchStrikeThrough(item);
        // organizedData.addElement(item);
        // }
      }
      for (String itemName : done) {
        organizedData.addElement("<html><strike>" + itemName + "</strike></html>");
      }

      listData = organizedData;
      list.setModel(organizedData);
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
    JTextArea noteText;
    ArrayList<String> months30;
    String oldName;
    JRadioButton deadlineButton;

    class SaveListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        // get values saved in name label, priority label, and notes label
        String itemName = itemText.getText();
        // if blank, throw error
        if (itemName.equals("")) {
          JOptionPane.showMessageDialog(rightPanel, "Invalid input!", "Error", 0);
          return;
        }

        String priority = priorityText.getText();

        // if blank, throw error
        if (priority.equals("")) {
          JOptionPane.showMessageDialog(rightPanel, "Invalid input!", "Error", 0);
          return;
        }

        // if <= 0 or not int, throw error
        int itemPriority = 0;
        try {
          itemPriority = Integer.parseInt(priority);
          if (itemPriority <= 0) {
            JOptionPane.showMessageDialog(rightPanel, "Invalid input!", "Error", 0);
            return;
          }
        } catch (Exception e) {
          JOptionPane.showMessageDialog(rightPanel, "Invalid input!", "Error", 0);
          return;
        }
        
        
        String itemNote = noteText.getText();

        String itemMonth = (String) monthDropdown.getSelectedItem();
        int itemDay = Integer.parseInt((String) dayDropdown.getSelectedItem());
        int itemYear = Integer.parseInt((String) yearDropdown.getSelectedItem());

        
        Deadline itemDeadline = new Deadline(itemMonth, itemDay, itemYear);

        ToDoItem newItem = new ToDoItem(itemName, itemPriority, itemDeadline, itemNote, deadlineButton.isSelected());

        if (isNew) {
          if (listData.contains(itemName)) {
            // THROW ERROR CANNOT CREATE WITH THIS NAME
            JOptionPane.showMessageDialog(rightPanel, "Invalid input!", "Error", 0);
            return;
          } else {
            listItems.put(itemName, newItem);
            listData.addElement(itemName);
            // THROW SUCCESS
            JOptionPane.showMessageDialog(rightPanel, "Item Saved!", "Success", 1);
          }
        } else {
          if (!oldName.equals(itemName) && listData.contains(itemName)) {
            // THROW ERROR, not allowed to change newName to name already in list.
            JOptionPane.showMessageDialog(rightPanel, "Invalid input!", "Error", 0);
            return;
          } else {
            if (oldName.equals(itemName)) {
              listItems.put(itemName, newItem);
            } else {
              listItems.remove(oldName);
              listData.removeElement(oldName);
              listItems.put(itemName, newItem);
              listData.addElement(itemName);
            }
            // THROW SUCCESS
            // JOptionPane success = new JOptionPane("Item saved!", 1, 0);
            JOptionPane.showMessageDialog(rightPanel, "Item Saved!", "Success", 1);
          } 
        }

        populate(newItem);
        leftPanel.reorganize();
        leftPanel.list.setSelectedValue(itemName, true);

      }
    }

    class ClearListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        clearFields();
      }
    }

    class DeadlineListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        if (deadlineButton.isSelected()) {
          monthDropdown.setEnabled(true);
          yearDropdown.setEnabled(true);
          dayDropdown.setEnabled(true);
        } else {
          monthDropdown.setEnabled(false);
          yearDropdown.setEnabled(false);
          dayDropdown.setEnabled(false);
        }
      }
    }

    class MonthListener implements ActionListener {      
      public void actionPerformed(ActionEvent event) {
        String selected = (String) monthDropdown.getSelectedItem();
        
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

    RightPanel() {

      months30 = new ArrayList<String>();
      String[] months30Static = { "April", "June", "September", "November" };
      months30.addAll(Arrays.asList(months30Static));

      setLayout(new GridLayout(5, 1));
      add(createItemPanel());
      add(createPriorityPanel());
      add(createDeadlinePanel());
      add(createNotesPanel());
      add(createButtons());
    }

    private void clearFields() {
      itemText.setText("");
      priorityText.setText("");
      noteText.setText("");

      monthDropdown.setSelectedIndex(0);
      monthDropdown.setEnabled(false);
      dayDropdown.setSelectedIndex(0);
      dayDropdown.setEnabled(false);
      yearDropdown.setSelectedIndex(0);
      yearDropdown.setEnabled(false);

      deadlineButton.setSelected(false);

      isNew = true;
    }

    private void populate(ToDoItem item) {
      oldName = item.name;

      itemText.setText(item.name);
      priorityText.setText(String.valueOf(item.priority));

      if (item.hasDeadline) {
        monthDropdown.setEnabled(true);
        monthDropdown.setSelectedItem(item.deadline.month);
        dayDropdown.setEnabled(true);
        dayDropdown.setSelectedIndex(item.deadline.day - 1);
        yearDropdown.setEnabled(true);
        yearDropdown.setSelectedItem(String.valueOf(item.deadline.year));
        deadlineButton.setSelected(true);
      } else {
        monthDropdown.setSelectedIndex(0);
        monthDropdown.setEnabled(false);
        dayDropdown.setSelectedIndex(0);
        dayDropdown.setEnabled(false);
        yearDropdown.setSelectedIndex(0);
        yearDropdown.setEnabled(false);
        deadlineButton.setSelected(false);
      }
      
      noteText.setText(item.notes);

      isNew = false;
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

      deadlineButton = new JRadioButton("Deadline");
      deadlineButton.addActionListener(new DeadlineListener());


      monthDropdown = new JComboBox<String>();
      for (String month : months) {
        monthDropdown.addItem(month);
      }
      monthDropdown.addActionListener(new MonthListener());
      monthDropdown.setEnabled(false);

      dayDropdown = new JComboBox<String>();
      for (int i = 1; i <= 31; i ++) {
        dayDropdown.addItem(String.valueOf(i));
      }
      dayDropdown.setEnabled(false);

      yearDropdown = new JComboBox<String>();
      for (int i = 2022; i <= 2031; i++) {
        yearDropdown.addItem(String.valueOf(i));
      }
      yearDropdown.addActionListener(new YearListener());
      yearDropdown.setEnabled(false);

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

      noteText = new JTextArea(3, 25);
      JScrollPane notesPane = new JScrollPane(noteText);
      
      notesPanel.add(new JLabel("Notes: "));
      notesPanel.add(notesPane);

      return notesPanel;
    }

    private JPanel createButtons() {
      JPanel buttonPanel = new JPanel();

      JButton saveButton = new JButton("Save Item");
      saveButton.addActionListener(new SaveListener());

      JButton newButton = new JButton("New Item");
      newButton.addActionListener(new ClearListener());

      buttonPanel.add(saveButton);
      buttonPanel.add(newButton);

      return buttonPanel;
    }

 
  }

  private String matchStrikeThrough(String itemText) {
    String pattern = "^<html><strike>(.*)</strike></html>$";
    Pattern strikeoutPattern = Pattern.compile(pattern);
    Matcher m = strikeoutPattern.matcher(itemText);
    if (m.find()) {
      return m.group(1);
    } else {
      return itemText;
    }
  }

  ToDoFrame() {

    listItems = new HashMap<>();
    
    setLayout(new GridLayout(1, 2));
    leftPanel = new LeftPanel();
    rightPanel = new RightPanel();
    add(leftPanel);
    add(rightPanel);

  }
}

class ToDos {

  public static void renderFrame(JFrame frame) {
    frame.setSize(1000, 700);
    frame.setTitle("Daniel Ayala | To Do Application");
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

