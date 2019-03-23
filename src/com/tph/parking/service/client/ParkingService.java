package com.tph.parking.service.client;

import com.tph.model.Parking;
import com.tph.parking.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;

public class ParkingService extends Thread implements com.tph.parking.service.ParkingService {

	private Parking parking;
	private ParkingServiceListener listener;
	private final int PORT;
	private InetAddress host;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private volatile boolean running;

	public Parking getParking() { return parking; }

	public int getPort() {
		return this.PORT;
	}

	public InetAddress getHost() {
		return this.host;
	}

	public boolean isRunning() {
		return this.running;
	}

	public ParkingService(int port, InetAddress host, ParkingServiceListener listener) {
		this.parking = new Parking(1, 10, new HashSet<>());
		this.PORT = port;
		this.host = host;
		this.listener = listener;
	}

	public EnterAnswer requestAccess() {
		EnterRequest request = new EnterRequest();
		EnterAnswer answer = null;
		try {
			out.writeObject(request);
			Object o = in.readObject();
			if(o instanceof EnterAnswer) {
				answer = (EnterAnswer) o;
				if(answer.isAccess())
					parking.addCar(answer.getReceiptId());
				listener.OnEnter(request, answer, socket);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}

		return answer;
	}

	public ExitAnswer requestExit(ExitRequest request) {
		ExitAnswer answer = null;
		try {
			out.writeObject(request);
			Object o = in.readObject();
			if(o instanceof ExitAnswer) {
				answer = (ExitAnswer) o;

				if(answer.isAccess())
					parking.removeCar(answer.getReceiptId());

				listener.OnExit(request, answer, socket);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}

		return answer;
	}

	@Override
	public void run() {
		running = true;
		while (running) {

		}
	}

	public void startService() {
		try {
			socket = new Socket(host, PORT);
			// Get input and output streams

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			try {
				Object o = in.readObject();
				if(o instanceof String) {
					System.out.println((String) o);
					running = true;
					//this.start();
					return;
				}
			}
			catch (Exception e) {
				System.out.println(e);
			}

		}
		catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		running = false;
	}

	@Override
	public void stopService() {
		try {
			socket.close();
			running = false;
			this.interrupt();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}