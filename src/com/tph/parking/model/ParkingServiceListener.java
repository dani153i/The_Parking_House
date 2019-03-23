package com.tph.parking.model;

import java.net.Socket;

public interface ParkingServiceListener {
    /*void OnServiceStarted(ParkingService parkingService);
    void OnServiceStopped(ParkingService parkingService);*/
    void OnEnter(EnterRequest request, EnterAnswer answer, Socket socket);
    void OnExit(ExitRequest request, ExitAnswer answer, Socket socket);
}
