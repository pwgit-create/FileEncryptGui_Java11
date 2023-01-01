module pwdev.mongoose.fileencryptgui_u1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;
    requires org.apache.commons.io;
    requires zip4j;



    opens pwdev.mongoose.fileencryptgui_u1 to javafx.fxml , javafx.graphics;

    exports pwdev.mongoose.fileencryptgui_u1;
}