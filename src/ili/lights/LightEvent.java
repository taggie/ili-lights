package ili.lights;

import java.util.EventObject;

/**
 * The LightEvent is a subclass of EventObject and is thrown by light sources when any of their parameters
 * change. If you want to capture events, be sure to implement LightListener and create a function 
 * public void lightEventReceived( event ); in your own class.
 * Then register your class to the light source of choice by calling addLightListener( yourObject ) of 
 * that specific object. 	
 */
public class LightEvent extends EventObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5091271756916309731L;
	
	/** Event is thrown when the state of the light source changes **/
	public final static int STATE          =	01;//This is an event for the state (on/off) of the light
	/** Event is thrown when the light source is turned on (state and intensity) **/
	public final static int TURN_ON		=	02;
	/** Even it thrown when the light source is turned off (state)	**/
	public final static int TURN_OFF		=	03;
  
  public final static int INTENSITY      =  20;  //This is an event for the intensity
  public final static int MIN_INTENSITY  =  21;  //Make sure the intensity is minimally this value
  public final static int MAX_INTENSITY  =  22;  //Make sure the intensity is maximum this value
  
  public final static int CCT            =  30;  //This is an event for the correlated colour temperature to the value
  public final static int MIN_CCT        =  31;  //Make sure the correlated colour temperature is minimally this value
  public final static int MAX_CCT        =  32;  //Make sure the correlated colour temperature is maximum this value
  
  public final static int COLOR		  =  40;  //This is a general color type event
  public final static int RGB            =  41;  //This is an event for the RGB colour settings
  public final static int MIN_RGB        =  42;  //Make sure the RGB is minimally this value
  public final static int MAX_RGB        =  43;  //Make sure the RGB is maximum this value
  public final static int HSB            =  44;  //This is an event for the HSB colour settings
  public final static int MIN_HSB        =  45;  //Make sure the HSB is minimally this value
  public final static int MAX_HSB        =  45;  //Make sure the HSB is maximum this value
  public final static int RED			=	51;	//Color RED changed
  public final static int GREEN			=	52;	//Color RED changed
  public final static int BLUE			=	53;	//Color RED changed
  public final static int HUE			=	54;	//Color RED changed
  public final static int SATURATION	=	55;	//Color RED changed
  public final static int BRIGHTNESS	=	56;	//Color RED changed
  
  public final static int ALL_PARAMETERS 	 =  60;  //This indicates all parameters have changed
  public final static int ALL_PARAMETERS_CCT =  61;  //This indicates all parameters have changed
  public final static int ALL_PARAMETERS_RGB =  62;  //This indicates all parameters have changed
  public final static int ALL_PARAMETERS_HSB =  63;  //This indicates all parameters have changed
  
  private int   _eventType        	=  0;
  
  /**
   * Creates a new LightEvent. Pass the object that throws this event as a source.
   * @param source
   */
  public LightEvent( Object source )
  {
    super( source );
  }
  /**
   * Creates a new LightEvent. Pass the object that throws this event and the event type.
   * @param source
   * @param eventType
   */
  public LightEvent( Object source, int eventType )
  {
    super( source );
    this._eventType  =  eventType;
  }
 
  /**
   * Returns the state of the light source. Call this when you received a STATE (01) event
   * @return boolean
   */
  public boolean getState()
  {
    return ((Light) this.getSource()).getState();
  }
  /**
   * Returns the light identifier of the light source that passed this event
   * @return int
   */
  public int getLightID()
  {
    return ( (Light) this.getSource()).getLightID();
  }
  /**
   * Returns the intensity of the light source that passed this event
   * @return int
   */
  public int getIntensity()
  {
    return ((Light) this.getSource()).getIntensity();
  }
  /**
   * Returns the colour temperature (CCT) of the light source
   * @return int
   */
  public int getCCT()
  {
    int cct  =  128;
    if( this.getType() == LightEvent.CCT )
    {
      CCTLight source  =  (CCTLight) this.getSource();
      cct              =  source.getCCT();
    }
    return cct;
  }
  /**
   * Returns the color of the light source that passed this event
   * @return int (Processing color type)
   */
  public int getColor()
  {
    int lightColour  =  0xFFFFFF;
    if( this.getType() >= LightEvent.RGB && this.getType() < LightEvent.ALL_PARAMETERS )
    {
      ColorLight source  =  (ColorLight) this.getSource();
      lightColour      =  source.getColor();
    }
    return lightColour;
  }
  /**
   * Returns the type of event that was thrown.
   * @return int
   */
  public int getType()
  {
    return this._eventType;
  }
  /**
   * Returns the light source that passed this event
   * @return Light
   */
  public Light getLight()
  {
	  return (Light) this.getSource();
  }
  
  /**
   * Returns the source that passed this event. This needs to be converted to a Light, CCTLight or ColorLight object.
   */
  public Object getSource()
  {
	  return this.getSource();
  }
}