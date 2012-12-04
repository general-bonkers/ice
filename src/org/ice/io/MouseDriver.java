package org.ice.io;

import java.awt.GraphicsDevice;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import sun.java2d.ScreenUpdateManager;

/**
 * Subclass for providing simple (procedural) interface to mouse.
 * 
 * @author home
 *
 */
public class MouseDriver implements MouseMotionListener, MouseListener {

	private int mouseButton;
	private int mouseX;
	private int mouseY;
	private int oldMouseX;
	private int oldMouseY;
	private boolean mouseMoved;
	
	public MouseDriver() 
	{
		init();
	}
		
	public void init() 
	{
		// do nothing?
	}

	public int getMouseX()
	{
		return mouseX;
	}
	
	public int getMouseY()
	{
		return mouseY;
	}
	
	public int getMouseButton()
	{
		return mouseButton;
	}
	
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		mouseButton = mouseEvent.getButton();
	}

	@Override
	public void mouseEntered(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		oldMouseX = mouseX;
		oldMouseY = mouseY;
		mouseX = mouseEvent.getLocationOnScreen().x;
		mouseY = mouseEvent.getLocationOnScreen().y;		
	}
}
