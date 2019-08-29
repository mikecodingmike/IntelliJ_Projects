package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import model.Mitarbeiter;
import model.PersonalException;
import model.Personalbuero;

import java.io.File;
import java.util.List;

public class RootBorderPane extends BorderPane {

    private Menu menu;
    private MenuBar menuBar;
    private Menu menuDatei, menuMitarbeiter, menuHilfe, menuHinzufuegen, menuSortierenNach;
    private MenuItem miLaden, miSpeichern, miImportieren, miExportieren, miBeenden,
            miLoeschenEinzeln, miLoeschenMulti, miAendern, miUeber,
            miSortAlter, miSortGehalt, miSortNamen,
            miHinzuAngestellter, miHinzuManager, miHinzuFreelancer;

    private FlowPane buttonPane;
    private Button btSortieren;
    private RadioButton rbSortierenNachAlter, rbSortierenNachGehalt,  rbSortierenNachNamen;

    private ToggleGroup rbGroup;

    private Personalbuero personalbuero;

    private MitarbeiterUebersicht mitarbeiterUebersicht;

    private MitarbeiterDialog mitarbeiterDialog;


    public RootBorderPane(){

        initComponents();
        addComponents();
        diableComponents(true);
        addHaendler();

    }

    private void initComponents(){

        menuBar = new MenuBar();
        menuDatei = new Menu("Datei");
        menuMitarbeiter = new Menu("Mitarbeiter");
        menuHinzufuegen = new Menu("Hinzuf�gen");
        menuHilfe = new Menu("Hilfe");
        menuSortierenNach = new Menu("Sortieren nach");

        miLaden = new MenuItem("Laden");
        miSpeichern = new MenuItem("Speichern");
        miImportieren = new MenuItem("Importieren");
        miExportieren = new MenuItem("Exportieren");
        miBeenden = new MenuItem("Beenden");

        miLoeschenEinzeln = new MenuItem("L�schen einzeln");
        miLoeschenMulti = new MenuItem("L�schen mehrere");
        miAendern = new MenuItem("�ndern");
        miUeber = new MenuItem("�ber");

        miSortAlter = new MenuItem("Alter");
        miSortGehalt = new MenuItem("Gehalt");
        miSortNamen = new MenuItem("Namen");

        miHinzuAngestellter = new MenuItem("Angestellter");
        miHinzuManager = new MenuItem("Manager");
        miHinzuFreelancer = new MenuItem("Freelancer");

        btSortieren = new Button("Sortieren");
        rbSortierenNachAlter = new RadioButton("nach Alter");
        rbSortierenNachGehalt = new RadioButton("nach Gehalt");
        rbSortierenNachNamen = new RadioButton("nach Namen");

        buttonPane = new FlowPane(10,10);
            buttonPane.setAlignment(Pos.CENTER);
            buttonPane.setPadding(new Insets(5));
        rbGroup = new ToggleGroup();

        personalbuero = new Personalbuero();

        mitarbeiterUebersicht = new MitarbeiterUebersicht(this);



    }

    private void addComponents(){
        setTop(menuBar);
        menuDatei.getItems().addAll(miLaden, miSpeichern, new SeparatorMenuItem(), miImportieren, miExportieren, new SeparatorMenuItem(), miBeenden);
        menuHilfe.getItems().add(miUeber);
        menuSortierenNach.getItems().addAll(miSortAlter, miSortGehalt, miSortNamen);
        menuHinzufuegen.getItems().addAll(miHinzuAngestellter, miHinzuManager, miHinzuFreelancer);

        menuBar.getMenus().addAll(menuDatei, menuMitarbeiter, menuHilfe);

        buttonPane.getChildren().addAll(btSortieren, rbSortierenNachAlter, rbSortierenNachGehalt, rbSortierenNachNamen);
        rbGroup.getToggles().addAll(rbSortierenNachAlter, rbSortierenNachGehalt, rbSortierenNachNamen);

        setCenter(mitarbeiterUebersicht);
        setBottom(buttonPane);
    }

    public void diableComponents(Boolean disabled){

    }
    //Händler

    public void addHaendler(){

        miLaden.setOnAction(event -> laden());
    }

    private void laden() {

        FileChooser fc = new FileChooser();
        File initDirectory = new File("c:\\scratch");
        if(initDirectory != null){
            fc.setInitialDirectory(initDirectory);
        }
        else{
            Main.showAlert(Alert.AlertType.ERROR, "initDirectory not found");
        }
        File selected = fc.showOpenDialog(null);
        if(selected != null){
            try{
                personalbuero.loadMitarbeiter(selected.getAbsolutePath());
                mitarbeiterUebersicht.updateAndShow(personalbuero.getMitarbeiter());
            }
            catch(PersonalException e){
                Main.showAlert(Alert.AlertType.ERROR,e.getMessage());
            }
        }
        else{
            Main.showAlert(Alert.AlertType.ERROR, "something went wrong with the selected file");
        }
    }

    public void aendern(){
        List<Mitarbeiter> auswahl = mitarbeiterUebersicht.getSelection();
        if(auswahl.size() > 0){
            if(auswahl.size() ==1){
                Mitarbeiter m = auswahl.get(0);
                mitarbeiterDialog.updateAndShow(m);
                mitarbeiterUebersicht.updateAndShow(personalbuero.getMitarbeiter());

            }
        }
    }

    public void loeschenMultiple(){

    }

}
