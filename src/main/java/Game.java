import java.util.*;

public class Game {
    public static void main(String[] args) {
        Sorter sorter = new Sorter();


       printBigHello();
       sorter.init();

    }

    public static void printBigHello() {
        String[] helloArt = {
                "██████╗  ██████╗   ██╗  ██╗   ██╗  ███████╗  ████████╗",
                "██╔══██╗ ██╔══██╗  ██║  ██║   ██║  ██╔════╝  ╚══██╔══╝",
                "██████╔╝ ██████╔╝  ██║  ██║   ██║  █████╗       ██║   ",
                "██╔═══╝  ██╔══██╗  ██║  ╚██╗ ██╔╝  ██╔══╝       ██║   ",
                "██║      ██║  ██║  ██║   ╚████╔╝   ███████╗     ██║   ",
                "╚═╝      ╚═╝  ╚═╝  ╚═╝    ╚═══╝    ╚══════╝     ╚═╝   ",
                "\n"};

        for (String line : helloArt) {
            System.out.println(line);
        }
    }


}


