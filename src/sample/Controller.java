package sample;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.skins.BarChartItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
    private Pane barChartPane;
    private Tile barChartTile;
    private BarChartItem barChartItemOld;
    private BarChartItem barChartItem18;
    private BarChartItem barChartItem19;
    private BarChartItem barChartItem20;

    @FXML
    public void initialize() {
        tcId.setCellValueFactory(new PropertyValueFactory<Book,Integer>("id"));
        tcTitle.setCellValueFactory(new PropertyValueFactory<Book,String>("title"));
        tcAuthor.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        tcYear.setCellValueFactory(new PropertyValueFactory<Book,Integer>("year"));
        tvData.setItems(bookData);


        barChartItemOld=new BarChartItem("До 18 в.",15,Tile.ORANGE);
        barChartItem18=new BarChartItem("18 в.",25,Tile.BLUE);
        barChartItem19=new BarChartItem("19 в.",35,Tile.GREEN);
        barChartItem20=new BarChartItem("20 в.",45,Tile.MAGENTA);



        barChartTile= TileBuilder.create()
                .prefSize(150,150)
                .skinType(Tile.SkinType.BAR_CHART)
                .title("Статистика")
                .text("")
                .textColor(Color.BLACK)
                .titleColor(Color.BLACK)
                .valueColor(Color.BLUEVIOLET)
                .backgroundColor(Color.rgb(244,244,244))
                .barChartItems(barChartItemOld,barChartItem18,barChartItem19,barChartItem20)
                .build();
        barChartPane.getChildren().add(barChartTile);

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
        //tvData.setItems(filteredData);


        barChartItemOld.setValue(bookData.stream().filter(book ->book.getYear()<1700).count());
        barChartItem18.setValue(bookData.stream().filter(book ->book.getYear()>=1700 && book.getYear()<1800).count());
        barChartItem19.setValue(bookData.stream().filter(book ->book.getYear()>=1800 && book.getYear()<1900).count());
        barChartItem20.setValue(bookData.stream().filter(book ->book.getYear()>=1900 && book.getYear()<2000).count());



    }
}
