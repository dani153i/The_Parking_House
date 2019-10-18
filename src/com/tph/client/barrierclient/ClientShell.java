package com.tph.client.barrierclient;

import com.tph.parking.model.*;
import com.tph.parking.service.client.CarParkingSimulator;
import com.tph.parking.service.client.ParkingService;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientShell implements ParkingServiceListener {
    private Scanner scanner = new Scanner(System.in);
    private ParkingService parkingService;
    private CarParkingSimulator simulator;


    private boolean run = false;


    public ClientShell() {
        try {
            parkingService = new ParkingService(1234, InetAddress.getLocalHost(), this);
            parkingService.startService();
            run = true;
        } catch (UnknownHostException e) {
            System.out.println(e);
        }

        simulator = new CarParkingSimulator(parkingService);

        while (run) {
            displayUI();
        }
    }



    @Override
    public void OnEnter(EnterRequest request, EnterAnswer answer, Socket socket) {
        if (answer.isAccess()) {
            System.out.println("Car " + answer.getReceiptId() + " entered Parking House.");
        } else {
            System.out.println("Parking house is "+ (answer.isFull() ? "full" : "closed."));
            System.out.println("Car " + answer.getReceiptId() + " was denied access to Parking House.");
        }
    }

    @Override
    public void OnExit(ExitRequest request, ExitAnswer answer, Socket socket) {
        if (answer.isAccess()) {
            System.out.println("Car " + answer.getReceiptId() + " left Parking House.");
        } else {
            System.out.println("Car " + answer.getReceiptId() + " cannot leave Parking house, it is not parked at Parking House.");
        }
    }


    public void displayUI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press [Enter] to " + (simulator.isRunning() ? "stop" : "start") + " parking simulator.");

        String input = scanner.nextLine();

        if(simulator.isRunning()) {
            simulator.stopSimulation();
            while (simulator.isRunning()) {}
            return;
        }

        simulator = new CarParkingSimulator(parkingService);
        simulator.startSimulation();
        while (!simulator.isRunning()) {}

    }

    public static void main(String[] args) {
        new ClientShell();
    }
}