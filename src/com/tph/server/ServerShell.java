package com.tph.server;

import com.tph.parking.model.*;
import com.tph.parking.service.server.ParkingService;

import java.net.Socket;
import java.util.Scanner;

public class ServerShell implements ParkingServiceListener {
    private Scanner scanner = new Scanner(System.in);
    private boolean run = false;
    private ParkingService parkingService;

    public ServerShell() {
        //
        Scanner scanner = new Scanner(System.in);

        //
        parkingService = new ParkingService(1234, this);
        //parkingService.startService();

        //
        run = true;
        do
            runUI();
        while(run);
    }

    @Override
    public void OnEnter(EnterRequest request, EnterAnswer answer, Socket socket) {
        if (answer.isAccess()) {
            System.out.println("Car " + answer.getReceiptId() + " entered Parking House");
        } else {
            System.out.println("Parking house is "+ (answer.isFull() ? "full" : "closed"));
            System.out.println("Car " + answer.getReceiptId() + " was denied access to Parking House");
        }
    }

    @Override
    public void OnExit(ExitRequest request, ExitAnswer answer, Socket socket) {
        if (answer.isAccess()) {
            System.out.println("Car " + answer.getReceiptId() + " left Parking House");
        } else {
            System.out.println("Car " + answer.getReceiptId() + " cannot leave Parking house, it is not parked at Parking House.");
        }

    }

    private void runUI() {
        System.out.println("~~~~ SERVICE MENU ~~~~");
        System.out.println("1) "+ (parkingService.isRunning() ? "Stop" : "Start") +" Parking Service");

        String input = "";
        while(!(input.equals("0") || input.equals("1"))) {
            input = scanner.nextLine();
        }

        handleServiceMenuInput(input);
    }

    private void handleServiceMenuInput(String input) {
        if(input.equals("0"))
            run = false;
        if(input.equals("1"))
            if(parkingService.isRunning()) {
                parkingService.stopService();
                while (parkingService.isRunning()) { }
            }
            else {
                parkingService.startService();
                while (!parkingService.isRunning()) { }
            }

    }

    public static void main(String[] args) {
        new ServerShell();
    }
}