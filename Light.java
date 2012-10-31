/**
 * ILI_Lights
 * This library contains a collection of light objects.
 * http://www.tue.ili.nl
 *
 * Copyright (C) 2012 Remco Magielse http://www.remcomagielse.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      Remco Magielse http://www.remcomagielse.com
 * @modified    10/31/2012
 * @version     0.1.1 (1)
 */

package ili.lights;


import java.util.ArrayList;
import java.util.Iterator;

import processing.core.*;
import processing.xml.XMLElement;

/**
 * This is a general light object which contains a light state (on/off) and 
 * an intensity, and intensity range and can be used to create representations of light sources 
 */

public class Light
{
  int _maxIntensity         =  255;            //Contains the maximum possible intensity
  int _minIntensity         =  0;              //Contains the minimum possible intensity
  int    _intensity         =  _maxIntensity;  //Sets the current intensity to the maximum intensity
  int _lightID              =  0;              //Sets the ID of this light source (in some cases channel) to 0 (standard)
  boolean _lightOn          =  true;
  boolean _useLightID       =  false;

  ArrayList<LightListener> _lightListeners =  new ArrayList<LightListener>();

  /**
   * Creates a standard light object with default settings intensity at 255, range from 0-255.
   */
  public Light() { }
  /**
   * Creates a new light object with the intensity set to the specified value.
   * @param intensity
   */
  public Light( int intensity )
  {
    this.setIntensity( intensity );
  }
  
  
  /**
   * Returns this light source representation as an XMLElement in the format
   * <Light type=String lightid=int state=boolean intensity=int min_intensity=int max_intensity=int />
   * @return XMLElement
   */
  public XMLElement getAsXML()
  {
    XMLElement lightXML  =  new XMLElement("Light");
    lightXML.setString("type", "light");
    lightXML.setInt( "lightid", this.getLightID() );
    lightXML.setBoolean( "state", this.getState() );
    lightXML.setInt( "intensity", this.getIntensity() );
    lightXML.setInt( "min_intensity", this.getMinIntensity() );
    lightXML.setInt( "max_intensity", this.getMaxIntensity() );
    return lightXML;
  }

  /**
   * Sets the identifier of this light source to the specified value. Automatically sets this light
   * source to also use the light id, when requested.
   * @param lightID
   */
  public void setLightID( int lightID )
  {
    this._lightID  		=  PApplet.max( 0, lightID );
    this._useLightID	=  true;
  }
  /**
   * Returns the light identifier of this light source
   * @return int
   */
  public int getLightID( )
  {
    return this._lightID;
  }
  /**
   * Enable the use of the light identifier. Can be useful when you have a single object with
   * multiple light sources. This allows you to distinguish among light sources in one object.
   */
  public void enableLightID()
  {
    this._useLightID  =  true;
  }
  /**
   * Disable the use of the light identifier.
   */
  public void disableLightID()
  {
    this._useLightID  =  false;
  }

//  /**  Sets the position of the light source using a PVector object  */
//  public boolean setPosition( PVector position )
//  {
//    this._position  =  position;
//    return true;
//  }
//  /**  Sets the position of the light source using an x and y coordinate object  */
//  public boolean setPosition( float x, float y )
//  {
//    return this.setPosition( new PVector( x, y ) );
//  }
//  /*  Returns the position of this light source as PVector  */
//  public PVector getPosition( )
//  {
//    return this._position;
//  }
//  /*  Returns the x-coordinate of this light source as a float  */
//  public float getX()
//  {
//    return this._position.x;
//  }
//  /*  Returns the y-coordinate of this light source as a float  */
//  public float getY()
//  {
//    return this._position.y;
//  }

  /**
   * Sets the intensity to the specified value if it is between the minimum and maximum range.
   * Automatically casts the specified value to an integer value.
   * @param intensity
   * @return boolean
   */
  public boolean setIntensity( float intensity )
  {
    return this.setIntensity( (int) intensity, true );
  }
  
  /**
   * Sets the intensity to the specified value if it is between the minimum and maximum range.
   * Light source automatically throws an event to all listeners.  
   * @param intensity (int)
   * @return boolean
   */
  public boolean setIntensity( int intensity )
  {
	  return this.setIntensity( intensity, true );
  }
  
  /**
   * Sets the intensity to the specified value if it is between the minimum and maximum range.
   * Specify whether you would like the light source to throw an event.  
   * @param intensity (int)
   * @param fireEvent (boolean)
   * @return boolean
   */
  public boolean setIntensity( int intensity, boolean fireEvent )
  {
	  
	  
    boolean success  =  false;
    if ( intensity >= this.getMinIntensity() &&
         intensity <= this.getMaxIntensity() && 
         intensity != this._intensity )
    {
      this._intensity  =  PApplet.constrain(intensity, this.getMinIntensity(), this.getMaxIntensity());
      success  =  true;
      
      if( fireEvent )
      {
    	  this.fireLightEvent( LightEvent.INTENSITY );
      }
    }
    return success;
  }
  
  /**
   * Returns the current intensity of this light source.
   * @return int
   */
  public int getIntensity( )
  {
    return this._intensity;
  }
  /**
   * Sets the minimum value of this light source (standard 0)
   * @param intensity
   */
  public void setMinIntensity( int intensity )
  {
    this._minIntensity  =  intensity;
  }
  /**
   * Returns the minimum value of this light source's intensity (standard 0)
   * @return int
   */
  public int getMinIntensity( )
  {
    return this._minIntensity;
  }
  /**
   * Sets the maximum value of this light source (standard 255)
   * @param intensity
   */
  public void setMaxIntensity( int intensity )
  {
    this._maxIntensity  =  intensity;
  }
  /**
   * Returns the maximum value of this light source (standard 255)
   * @return
   */
  public int getMaxIntensity()
  {
    return this._maxIntensity;
  }
  /**
   * Set the range for the intensity (standard 0-255)
   * @param minIntensity
   * @param maxIntensity
   */
  public void setRange( int minIntensity, int maxIntensity )
  {
    this.setMinIntensity( minIntensity );
    this.setMaxIntensity( maxIntensity );
  }
  /**
   * Set the state of this light source (on/off) to the specified value (true/false).
   * Fires an even to all listeners and returns the updated state.
   * Returns the new state of the light source.
   * @param state
   * @param fireEvent
   * @return boolean
   */
  public boolean setState( boolean state )
  {
    return this.setState(state, true);
  }
  /**
   * Set the state of this light source (on/off) to the specified value (true/false).
   * Fires an even to all listeners and returns the updated state if specified in the second argument
   * Returns the new state of the light source.
   * @param state
   * @param fireEvent
   * @return boolean
   */
  public boolean setState( boolean state, boolean fireEvent )
  {
	this._lightOn  =  state;
	if( fireEvent )
	{
		  this.fireLightEvent( LightEvent.STATE );
	}
    return this._lightOn;
  }
  /**
   * Returns the current state (on/off) of the light source as a boolean (on = true, off = false)
   * @return boolean
   */
  public boolean getState()
  {
    return this._lightOn;
  }
  /**
   * Turns the light source on and sets the intensity to the getMaxIntensity() and fires an event. 
   * Returns the new state of the light source (should be on).
   * @return boolean
   */
  public void turnOn()
  {
	  this.turnOn(true);
  }
  /**
   * Turns the light source on and determine if you fire an event (true = fire event) to all listeners. 
   * Returns the new state of the light source.
   * @return boolean
   */
  public void turnOn( boolean fireEvent )
  {
	  this.setIntensity( this.getMaxIntensity(), false );
	  this.setState(true, false);
	  if( fireEvent )
	  {
		  this.fireLightEvent(LightEvent.TURN_ON);
	  }
  }
  /**
   * Turns the light source off and fires an event. Returns the new state of the light source (should be off)
   * @return boolean
   */
  public boolean turnOff()
  {
	  return this.setState(false);
  }
  /**
   * Turns the light source off and fires an event if argument is set to true. 
   * Returns the new state of the light source (should be off)
   * @return boolean
   */
  public boolean turnOff( boolean fireEvent )
  {
    return this.setState(false, fireEvent);
  }
  /**
   * Indicates whether the light source is turned on. Returns 'true' when the light source is ON!
   * (This is the reverse of the function isOff())
   * @return boolean
   */
  public boolean isOn()
  {
    return this._lightOn;
  }
  /**
   * Indicates whether the light source is turned off. Returns 'true' when the light source is OFF!
   * (This is the reverse of the function isOn())
   * @return
   */
  public boolean isOff()
  {
    return !this._lightOn;
  }
  
  /**
   * Update both the state and the intensity in one function. Throws an all parameters change event.
   * @param state
   * @param intensity
   */
  public void setParameters( boolean state, int intensity )
  {
    this.setParameters( state, intensity, true);
  }
  /**
   * Update both the state and the intensity in one function. Throws an all parameters change event.
   * @param state
   * @param intensity
   */
  public void setParameters( boolean state, int intensity, boolean fireEvent )
  {
    this.setState( state, false );
    this.setIntensity( intensity, false );
    if( fireEvent )
    {
    	this.fireLightEvent(LightEvent.ALL_PARAMETERS);
    }
  }
  
  

  /**  Adds an object to listen to this light  **/
  public synchronized void addLightListener( LightListener l ) 
  {
    //    this.traceln("ADDLIGHTLISTENER( LIGHTLISTENER ): Adding LightListener", 1);
    _lightListeners.add( l );
  }
  /**  Removes an object to listen to this light  **/
  public synchronized void removeLightListener( LightListener l ) 
  {
    //    this.traceln("ADDLIGHTLISTENER( LIGHTLISTENER ): Removing LightListener from "+this.getName(), 1);
    _lightListeners.remove( l );
  }
  /**  Fires an event from this light source  **/
  protected synchronized void fireLightEvent( int eventType ) 
  {
    //    this.traceln("FIREVENT( ): Firing an event [on: "+this.isOn()+", intensity: "+this.getIntensity()+"]");
    //Create a new LithneEvent, with the specific details
    LightEvent event = new LightEvent( this, eventType );

    Iterator<LightListener> listeners = _lightListeners.iterator();
    while ( listeners.hasNext () ) 
    {
      ( (LightListener) listeners.next() ).lightEventReceived( event );
    }
  }
  
  
  public String toString()
  {
	  String lightInfo	=	"(type: Light)";
	  lightInfo			+=	" [id:"+this.getLightID()+"] ";
	  if( this.isOn() )
	  {
		  lightInfo	+=	" [on] ";
	  }
	  else
	  {
		  lightInfo	+=	" [off] ";
	  }
	  lightInfo		+=	" [intensity: ("+this.getMinIntensity()+")-"+this.getIntensity()+"("+this.getMaxIntensity()+")]";
	  
	  return lightInfo;
  }
  
}


