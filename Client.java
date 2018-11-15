package rmi.test;

import java.rmi.RMISecurityManager; 
import java.rmi.Naming;

import java.util.Scanner;

public class Client {
    private Client() {}

    public static void clearConsole() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    public static void main(String[] args) {
        System.setProperty("java.security.policy","file:./ticket.policy");
        System.setSecurityManager(new RMISecurityManager());

        boolean exit = false;

        try{
            // Obtém o stub para o objeto remoto do registro:
            ITicketeria stub = (ITicketeria) Naming.lookup("//192.168.0.1/Ticketeria");

            String intro = stub.getAppIntro();
            System.out.println(intro);

            Scanner sc = new Scanner(System.in);
            while(!exit) {
                String optionsText = stub.getBookOptionsText();
                System.out.println(optionsText);

                System.out.println("Digite o código do local desejado:");
                System.out.print(">> ");
                int local = sc.nextInt();

                System.out.println("\nDigite a data desejada:");
                System.out.print("(dd/mm/aa) >> ");
                String date = sc.next();

                System.out.println("\nDigite a hora de saída desejada:");
                System.out.print(">> ");
                int hour = sc.nextInt();

                String validateOptions = stub.validateTicketOptions(local, date, hour);
                if(validateOptions.length() != 0) {
                    System.out.println(validateOptions);
                } else {
                    String ticket = stub.bookTicket(local, date, hour);
                    System.out.println(ticket);
                }

                System.out.println("\nVocê deseja reservar outro ticket?");
                System.out.print("Sim(s) ou Não(n) >> ");
                char response = sc.next().charAt(0);

                if(response == 'n') {
                    exit = true;
                }

                clearConsole();
            }
            
            String goodbyeMsg = stub.getGoodbyeMsg();
            System.out.println(goodbyeMsg);

        } catch (Exception e) {
            System.err.println("Exceção no cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}