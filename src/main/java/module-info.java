module com.rickturner.alexplayground {
    requires javafx.controls;
    requires javafx.fxml;
    requires redis.clients.jedis;
    requires Java.WebSocket;


    opens com.rickturner.alexplayground to javafx.fxml;
    exports com.rickturner.alexplayground;
}