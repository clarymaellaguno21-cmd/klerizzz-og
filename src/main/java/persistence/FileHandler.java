package persistence;

import model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;

public class FileHandler {
    private String fileName;

    public FileHandler(String fileName) {
        this.fileName = fileName;
    }

    public ObservableList<Contact> load() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        File file = new File(fileName);
        if (!file.exists()) return contacts;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 5) {
                    contacts.add(new Contact(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return contacts;
    }

    public void save(ObservableList<Contact> contacts) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (Contact c : contacts) {
                pw.println(c.getFirstName() + "," + c.getMiddleName() + "," + c.getLastName() + "," + c.getEmail() + "," + c.getPhone());
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}