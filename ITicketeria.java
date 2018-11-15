package rmi.test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITicketeria extends Remote {
    String getAppIntro() throws RemoteException;

    String bookTicket(int local, String date, int hour) throws RemoteException;

    String getBookOptionsText() throws RemoteException;

    String validateTicketOptions(int local, String date, int hour) throws RemoteException;

    String getGoodbyeMsg() throws RemoteException;
}