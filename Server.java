package rmi.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ITicketeria {
    private ArrayList<Ticket> ticketsList = new ArrayList<Ticket>();
    private String[] availablePlaces = {"Atibaia", "Bragança Paulista", "Campinas", "Itatiba", "São Paulo"};
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd/MM/yyyy HH:mm");

    public Server() throws RemoteException {}

    private String getLocalNameByCode(int code) {
        String name;
        if(code > 0 && code <= availablePlaces.length) {
            name = availablePlaces[code-1];
        } else {
            name = "Local Inválido";
        }
        return name;
    }

    private boolean checkTicketAvailability(Ticket newTicket) {
        boolean isAvailable = true;

        for(Ticket ticket: ticketsList) {
            if(ticket.getLocal() == newTicket.getLocal() &&
               ticket.getDate().compareTo(newTicket.getDate()) == 0) {
                   isAvailable = false;
                   break;
               }
        }

        return isAvailable;

    }

    public String getAppIntro() {
        return "\nOlá! Seja bem-vindo à Ticketeria!\n"
                + "Evite filas reservando seu ticket conosco de forma fácil, simples e rápida.";
    }

    public String getGoodbyeMsg() {
        return "\nObrigado por usar a Ticketeria!\n"
                + "Sentiremos a sua falta! Volte sempre!\n";
    }

    public String getBookOptionsText() {
        String text;
        String now = dateFormatter.format(new Date());

        text = "\n\n***** Reserva de Ticket *****\n\n"
            + "Por favor, insira o código do local, a data e a hora desejada para a reserva do ticket.\n\n"
            + "Data e hora atual: " + now + "\n\n"
            + "Lista de destinos disponíveis:\n";

        for(int i = 0; i < availablePlaces.length; i++) {
            text += String.format("%d - %s\n", i+1, availablePlaces[i]);
        }

        return text;
    }

    public String validateTicketOptions(int local, String date, int hour) {
        String response = "";

        try {
            SimpleDateFormat dateFormatted = new SimpleDateFormat("dd/MM/yy HH:mm");
            Date ticketDate = dateFormatted.parse(date + String.format(" %d:00", hour));
            String reason = "";

            if(local <= 0 || local > availablePlaces.length) {
                reason = "O código do local inserido não existe!";
            } else if(ticketDate.compareTo(new Date()) < 0) {
                reason = "A data inserida é anterior a data atual!";
            }

            if(reason.length() != 0) {
                response = "\nSentimos muito, mas não foi possível reservar este Ticket!\n"
                        + "Motivo: " + reason + "\n"
                        + "Verifique se você digitou corretamente as opções e tente novamente.\n";
            }
            
        } catch(Exception e) {
            System.out.println("Exception: " + e);
        }

        return response;
    }

    public String bookTicket(int local, String data, int hour) {
        String response;
        Ticket ticket = new Ticket(local, data, hour);

        if(checkTicketAvailability(ticket)) {
            ticketsList.add(ticket);

            Date ticketDate = ticket.getDate();
            String ticketDateFormatted = dateFormatter.format(ticketDate);

            response = "\nTicket reservado com sucesso!"
                    + "\nData: " + ticketDateFormatted
                    + "\nLocal: " + getLocalNameByCode(ticket.getLocal())
                    + "\n";
        } else {
            response = "\nSentimos muito, mas não foi possível reservar este Ticket!\n"
                    + "O local e horário desejados já foram reservados. Favor escolher outra opção.\n";
        }

        return response;
    }

    public static void main (String args[]) {
        try {
            System.setProperty("java.security.policy","file:./ticket.policy");
            System.setProperty("java.rmi.server.hostname", "192.168.0.1");

            //Criação e exportação do Objeto Remoto:
            Server server = new Server();
            Naming.rebind("Ticketeria", server);

            System.err.println("Servidor pronto...");
        } catch (Exception e) {
            System.err.println("Exceção no servidor:  " + e.toString());
            e.printStackTrace();
        }
    }
}