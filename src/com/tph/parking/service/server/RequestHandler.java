package com.tph.parking.service.server;

import com.tph.parking.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class RequestHandler extends Thread {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private ParkingServiceListener parkingServiceListener;

	public void setParkingServiceListener(ParkingServiceListener listener) { this.parkingServiceListener = listener; }

	/**
	 * 
	 * @param socket
	 */
	RequestHandler( Socket socket )
	{
		this.socket = socket;
	}

	public void run() {
		try
		{
			System.out.println( "Received a connection" );

			// Get input and output streams
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			// Write out our header to the TCPClient
			out.writeObject( "Parking Server 1.0" );
			out.flush();

			while(socket.isConnected()) {
				Object o = in.readObject();
				onPacketReceived(o);
				//Class<?> clazz = o.getClass();
                /*if (o instanceof ParkingRequest) {
                    out.writeObject(new ParkingRequestAnswer(false, true));
                }*/
			}

			// Close our connection
			in.close();
			out.close();
			socket.close();

			System.out.println( "Connection closed" );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void sendPacket(Object o) {
		try {
			out.writeObject(o);
		}
		catch (IOException e) { System.out.println(e); }
	}

	private void onPacketReceived(Object o) {
		System.out.println("packet received");
		if(parkingServiceListener == null)
			return;

		if(o instanceof EnterRequest) {
			parkingServiceListener.OnEnter((EnterRequest) o, new EnterAnswer(0, true, false), socket);
		}

		if(o instanceof ExitRequest) {
			parkingServiceListener.OnExit((ExitRequest) o, new ExitAnswer(0, false), socket);
		}
	}
}