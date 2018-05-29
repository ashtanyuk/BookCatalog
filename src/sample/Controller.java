package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class Controller {

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfAuthor;
    @FXML
    private TextField tfYear;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnOpen;
    @FXML
    private Button btnSelect;

    @FXML
    private TableView<Book> tvData;
    @FXML
    private TableColumn<Book,Integer> tcId;
    @FXML
    private TableColumn<Book,String> tcTitle;
    @FXML
    private TableColumn<Book,String> tcAuthor;
    @FXML
    private TableColumn<Book,Integer> tcYear;

    private ObservableList<Book> bookData=
            FXCollections.observableArrayList();
    private FilteredList<Book> filteredData;

    @FXML
    public void initialize() {
        tcId.setCellValueFactory(new PropertyValueFactory<Book,Integer>("id"));
        tcTitle.setCellValueFactory(new PropertyValueFactory<Book,String>("title"));
        tcAuthor.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        tcYear.setCellValueFactory(new PropertyValueFactory<Book,Integer>("year"));
        tvData.setItems(bookData);
    }
    @FXML
    public void onClickAdd() {
        bookData.add(new Book(Integer.parseInt(tfId.getText()),
                              tfTitle.getText(),
                              tfAuthor.getText(),
                              Integer.parseInt(tfYear.getText())));
    }
    @FXML
    public void onClickOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().
         add(new FileChooser.ExtensionFilter("JSON","*.json"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        importJSON(selectedFile.getName());
    }

    public void importJSON(String filename) {
        int id=1;
        JSONParser parser = new JSONParser();
        try
        {
            Object obj=parser.parse(new FileReader(filename));
            JSONArray books=(JSONArray)obj;
            Iterator bookIterator=books.iterator();
            while(bookIterator.hasNext()) {
                JSONObject book=(JSONObject)bookIterator.next();
                String title=book.get("title").toString();
                String author=book.get("author").toString();
                int year=Integer.parseInt(book.get("year").toString());
                bookData.add(new Book(id++,title,author,year));
            }
        }
        catch(FileNotFoundException ex){}
        catch(IOException ex) {}
        catch (ParseException ex) {}
    }

    @FXML
    public void onClickSelect() {
        filteredData=new FilteredList<Book>(bookData,
                book -> {if(book.getYear()>1800)
                           return true; else return false;});
        tvData.setItems(filteredData);
    }
}
