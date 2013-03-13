package com.MA1.contextclient;

import java.rmi.RemoteException;

import dk.pervasive.jcaf.ContextEvent;
import dk.pervasive.jcaf.EntityListener;
import dk.pervasive.jcaf.impl.RemoteEntityListenerImpl;
import dk.pervasive.jcaf.util.AbstractContextClient;


public class ContextClient extends AbstractContextClient {

	private RemoteEntityListenerImpl listener;

	public ContextClient(String serviceUri) {
		super(serviceUri);
		try {
			listener = new RemoteEntityListenerImpl();
			listener.addEntityListener(new EntityListener() {
				@Override
				public void contextChanged(ContextEvent event) {
					System.out.println("Listener1: " + event.toString());
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {		
		Boolean added = false;
//		String location_id = "itu.zone0.zonedorsyd";
		String location_id = "itu.zone0.zoneaud1";
		try{
			System.out.println("Client for " + location_id + " -> Server info: \n   " + getContextService().getServerInfo());
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Client for "+location_id+" trying to add listener");
		while(!added){
			try {
				getContextService().addEntityListener(listener, location_id);
				added = true;
			} catch (Exception e) {
				try{
					Thread.currentThread();
					Thread.sleep(5000);
				}catch(Exception ex){
				}
			}		
		}
		System.out.println("Client for "+location_id+" --> LISTENING!");
	}

	public static void main(String[] args) {

		ContextClient cc = new ContextClient("rmi://192.168.1.3/info@itu");
		Thread t = new Thread(cc);
		t.run();
	}
}
