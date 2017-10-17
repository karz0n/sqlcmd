package ua.in.denoming.sqlcmd.controller;

public class Main {
    public static void main(String[] args) {
        try (App app = App.getInstance()) {
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
