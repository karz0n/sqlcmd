package ua.in.denoming.sqlcmd.view;

import java.util.Scanner;

public class Console implements View {
    private Scanner scanner;

    public Console() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public View throwing(Throwable throwable) {
        throwable.printStackTrace(System.out);
        return this;
    }

    @Override
    public View write(String message) {
        System.out.print(message);
        return this;
    }

    @Override
    public View writeFormat(String format, Object ... objects) {
        System.out.printf(format, objects);
        return this;
    }

    @Override
    public View line() {
        System.out.println();
        return this;
    }

    @Override
    public View indent(int count) {
        while (count-- > 0) {
            System.out.print("\t");
        }
        return this;
    }

    @Override
    public String readLine() {
        return this.scanner.nextLine();
    }
}
