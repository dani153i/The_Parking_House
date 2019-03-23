package com.tph.parking.service.server;

import com.tph.model.Parking;
import com.tph.parking.model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ParkingService extends Thread implements com.tph.parking.service.ParkingService, ParkingServiceListener {

	int receiptCount = 0;
	private Parking parking;

	private ParkingServiceListener listener;
	private int port;
	private ServerSocket serverSocket;
	private Map<Socket, RequestHandler> clients = new HashMap<>();
	private volatile boolean running;


	public int getPort() {
		return this.port;
	}
	public boolean isRunning() {
		return this.running;
	}

	public ParkingService(int port, ParkingServiceListener listener )
	{
		parking = new Parking(1, 10, new HashSet<>());
		this.port = port;
		this.listener = listener;
	}

	public void startService() {
		try
		{
			serverSocket = new ServerSocket( port );
			serverSocket.setReuseAddress(true);
			this.start();

		}
		catch (IOException e) { System.out.println(e); }
	}

	public void stopService() {
		this.interrupt();
		running = false;
	}

	@Override
	public void run() {
		System.out.println( "Listening for a connection" );
		running = true;
		while( running )
		{
			try
			{
				// Call accept() to receive the next connection
				Socket socket = serverSocket.accept();

				// Pass the socket to the RequestHandler thread for processing
				RequestHandler requestHandler = new RequestHandler( socket );
				requestHandler.setParkingServiceListener(this);
				clients.put(socket, requestHandler);
				requestHandler.start();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void OnEnter(EnterRequest request, EnterAnswer answer, Socket socket) {
		int recieptId = ++receiptCount;


		if(parking.getLotsUsed() < parking.getLotCount()
		&& parking.addCar(recieptId)) {
			answer.setAccess(true);
			answer.setFull(false);
		} else {
			answer.setAccess(false);
			answer.setFull(true);

		}

		answer.setReceiptId(recieptId);


		clients.get(socket).sendPacket(answer);

		listener.OnEnter(request, answer, socket);
	}

	@Override
	public void OnExit(ExitRequest request, ExitAnswer answer, Socket socket) {
		answer.setReceiptId(request.getReceiptId());


		answer.setAccess(parking.removeCar(request.getReceiptId()));


		clients.get(socket).sendPacket(answer);

		listener.OnExit(request, answer, socket);
	}
}