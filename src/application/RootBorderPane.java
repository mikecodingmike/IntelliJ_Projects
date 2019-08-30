package application;

import javafx.application.Platform;
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


    RootBorderPane(){

        initComponents();
        addComponents();
        disableComponents(true);
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
        menuMitarbeiter.getItems().addAll(miAendern, miLoeschenEinzeln, miLoeschenMulti);
        menuHilfe.getItems().add(miUeber);
        menuSortierenNach.getItems().addAll(miSortAlter, miSortGehalt, miSortNamen);
        menuHinzufuegen.getItems().addAll(miHinzuAngestellter, miHinzuManager, miHinzuFreelancer);

        menuBar.getMenus().addAll(menuDatei, menuMitarbeiter, menuHilfe);

        buttonPane.getChildren().addAll(btSortieren, rbSortierenNachAlter, rbSortierenNachGehalt, rbSortierenNachNamen);
        rbGroup.getToggles().addAll(rbSortierenNachAlter, rbSortierenNachGehalt, rbSortierenNachNamen);

        setCenter(mitarbeiterUebersicht);
        setBottom(buttonPane);
    }

    private void disableComponents(Boolean disabled){
        miSpeichern.setDisable(disabled);
        miExportieren.setDisable(disabled);
        miAendern.setDisable(disabled);
        menuSortierenNach.setDisable(disabled);
        miLoeschenEinzeln.setDisable(disabled);
        miLoeschenMulti.setDisable(disabled);

        mitarbeiterUebersicht.setVisible(!disabled);
        buttonPane.setVisible(!disabled);
    }
    //Händler

    private void addHaendler(){

        miLaden.setOnAction(event -> laden());
        miBeenden.setOnAction(event -> beenden());
        miUeber.setOnAction(event -> ueber());
        miLoeschenMulti.setOnAction(event -> loeschenMultiple());
        miLoeschenEinzeln.setOnAction(event -> loeschenEinzeln());
        miSpeichern.setOnAction(event -> speichern());

    }

    private void laden() {

        FileChooser fc = new FileChooser();
        File initDirectory = new File("c:\\scratch");
        if(initDirectory != null){
            fc.setInitialDirectory(initDirectory);
        }
        else{
            Main.showAlert(Alert.AlertType.ERROR, "initDirectory not found",ButtonType.OK);
        }
        File selected = fc.showOpenDialog(null);
        if(selected != null){
            try{
                personalbuero.loadMitarbeiter(selected.getAbsolutePath());
                mitarbeiterUebersicht.updateAndShow(personalbuero.getMitarbeiter());
            }
            catch(PersonalException e){
                Main.showAlert(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
            }
        }
        else{
            Main.showAlert(Alert.AlertType.ERROR, "something went wrong with the selected file",ButtonType.OK);
        }
    }

    private void speichern(){
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory( new File("C:\\scratch"));
        File selected = fc.showSaveDialog(null);
        if(selected != null){
            try{
                personalbuero.saveMitarbeiter(selected.getAbsolutePath());
            }
            catch(PersonalException e){
                Main.showAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        }
        else{
            Main.showAlert(Alert.AlertType.ERROR, "No File selected", ButtonType.OK);
        }
    }

    void aendern(){
        List<Mitarbeiter> auswahl = mitarbeiterUebersicht.getSelection();
        if(auswahl.size() > 0){
            if(auswahl.size() ==1){
                Mitarbeiter m = auswahl.get(0);
                mitarbeiterDialog.updateAndShow(m);
                mitarbeiterUebersicht.updateAndShow(personalbuero.getMitarbeiter());

            }
            else{
                Main.showAlert(Alert.AlertType.ERROR,"Only single selection can be deleted", ButtonType.OK);
            }
        }
        else{
            Main.showAlert(Alert.AlertType.ERROR, "Nothing is selevted",ButtonType.OK);
        }
    }

    private void ueber(){
        Main.showAlert(Alert.AlertType.INFORMATION, "Personalbuero Version 1.0\n \n Copyright Mike Tangwena 2019", ButtonType.OK);
    }

    private void beenden(){
        Platform.exit();
    }

    protected void loeschenMultiple(){
        List<Mitarbeiter> multiSelect = mitarbeiterUebersicht.getSelection();
        if(multiSelect.size() > 0){
            try{
                personalbuero.remove(multiSelect);
                mitarbeiterUebersicht.updateAndShow(personalbuero.getMitarbeiter());
            }
            catch(PersonalException e){
                Main.showAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        }
        else{
            Main.showAlert(Alert.AlertType.ERROR, "Nothing is selected", ButtonType.OK);
        }
    }

    private void loeschenEinzeln(){
        List<Mitarbeiter> einzelSelect = mitarbeiterUebersicht.getSelection();
        if(einzelSelect.size() == 1){
            try{
                personalbuero.remove(einzelSelect);
                mitarbeiterUebersicht.updateAndShow(personalbuero.getMitarbeiter());
            }
            catch(PersonalException e){
                Main.showAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        }
        else{
            Main.showAlert(Alert.AlertType.ERROR, "Selected is not 1", ButtonType.OK);
        }
    }

}
