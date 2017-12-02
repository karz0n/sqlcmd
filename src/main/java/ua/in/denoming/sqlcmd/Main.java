package ua.in.denoming.sqlcmd;

import ua.in.denoming.sqlcmd.controller.App;

class Pair<T> {
    private T first;
    private T second;
}

public class Main {
    public static void main(String[] args) {
        @SuppressWarnings("unchecked")
        Pair<String>[] table = (Pair<String>[]) new Pair<?>[10];

        try (App app = new App()) {
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
