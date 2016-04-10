package ohtu.data_access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ohtu.domain.User;

/**
 *
 * @author Riku
 */
public class FileUserDAO implements UserDao {

    private List<User> users;
    private Scanner scanner;
    private FileWriter writer;
    private File file;

    public FileUserDAO(String filename) {
        this.users = new ArrayList<User>();
        this.file = new File(filename);
        if (scannerForFile()) {
            readFile();
        }

    }

    private boolean scannerForFile() {
        try {
            this.scanner = new Scanner(file);
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        }
    }

    private void readFile() {
        while (this.scanner.hasNextLine()) {
            readLine(this.scanner.nextLine());
        }
    }

    private void readLine(String line) {
        String[] components = line.split(";", 2);
        this.users.add(new User(components[0], components[1]));
    }

    @Override
    public List<User> listAll() {
        return this.users;
    }

    @Override
    public User findByName(String name) {
        for (User user : this.users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    private boolean writerForFile() {
        if (this.writer == null) {
            try {
                this.writer = new FileWriter(this.file, true);
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void add(User user) {
        if (writerForFile()) {
            this.users.add(user);
            try {
                this.writer.write(user.getUsername() + ";" + user.getPassword() + System.lineSeparator());
                this.writer.flush();
            } catch (IOException ex) {
                Logger.getLogger(FileUserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
