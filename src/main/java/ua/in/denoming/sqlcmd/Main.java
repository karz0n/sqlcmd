package ua.in.denoming.sqlcmd;

import ua.in.denoming.sqlcmd.controller.App;

public class Main {
    public static void main(String[] args) {
        try (App app = new App()) {
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
