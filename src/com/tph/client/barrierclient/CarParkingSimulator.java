package com.tph.client.barrierclient;

import com.tph.parking.model.ExitRequest;
import com.tph.parking.service.client.ParkingService;

import java.util.Random;

public class CarParkingSimulator extends Thread {
    ParkingService parkingService;
    private volatile boolean running = false;

    public boolean isRunning() { return running; }

    public CarParkingSimulator(ParkingService parkingService) {
        this.parkingService = parkingService;
    }


    @Override
    public void run() {
        running = true;
        try {
            Random r = new Random();
            while (running) {

                // determine if a car should be enter or leave, in favor of entering
                int oneToThree = r.nextInt((3 - 1) + 1) + 1;
                if (oneToThree < 3) {
                    parkingService.requestAccess();
                }
                else if(parkingService.getParking().getLotsUsed() > 0) {
                    int receiptId = 0;
                    int randomCar = r.nextInt(parkingService.getParking().getLotsUsed());
                    int i = 0;
                    for(int id : parkingService.getParking().getCarIds()) {
                        if(i == randomCar) {
                            receiptId = id;
                        }
                        i++;
                    }
                    parkingService.requestExit(new ExitRequest(receiptId));
                }

                Thread.sleep((r.nextInt((4 - 1) + 1) + 1) * 600);
            }
        }
        catch (InterruptedException e) {
            running = false;
        }
    }

    public void startSimulation() {
        this.start();
    }

    public void stopSimulation() {
        running = false;
        this.interrupt();
    }
}
