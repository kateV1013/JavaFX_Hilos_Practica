module ni.edu.uam.nicadata_solutions {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.nicadata_solutions to javafx.fxml;
    opens ni.edu.uam.practicahilos to javafx.fxml;
    exports ni.edu.uam.nicadata_solutions;
    exports ni.edu.uam.practicahilos;
}
