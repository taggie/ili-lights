package ili.lights;

import processing.core.PApplet;
import processing.xml.XMLElement;

/**
 * The CCTLight is a subclass of the Light object and contains a range of warm-white to cool-white light.
 */
public class CCTLight extends Light
{
  public static final int WARMWHITE  =  0xF9E9B7;
  public static final int WHITE      =  0xF9F9ED;
  public static final int COOLWHITE  =  0x96C3E2;
  
  int _maxCCT  =  255;
  int _minCCT  =  0;
  int _cct     =  128;  //Set the CCT standard in the middle

  /**
   * Create a new CCTLight source with standard parameters.
   * The low end (e.g. 0) of the colour temperature relates to a warm white light colour, the high end
   * (e.g. 255) relates to a cool white light colour. 
   */
  public CCTLight() { super(255); }
  /**
   * Create a new CCTLight source with the specified intensity and cct (0-255) range standard.
   * The low end (e.g. 0) of the colour temperature relates to a warm white light colour, the high end
   * (e.g. 255) relates to a cool white light colour.
   * @param intensity
   * @param cct
   */
  public CCTLight( int intensity, int cct )
  {
    super(intensity);
  }
  
  /**
   * Returns this light source representation as an XMLElement in the format
   * <Light type=String lightid=int state=boolean intensity=int min_intensity=int max_intensity=int
   * cct=int min_cct=int max_cct=int />
   * @return XMLElement
   */
  public XMLElement getAsXML()
  {
    XMLElement lightXML  =  new XMLElement("Light");
    lightXML.setString("type", "cct");
    lightXML.setInt( "lightid", this.getLightID() );
    lightXML.setBoolean( "state", this.getState() );
    lightXML.setInt( "intensity", this.getIntensity() );
    lightXML.setInt( "min_intensity", this.getMinIntensity() );
    lightXML.setInt( "max_intensity", this.getMaxIntensity() );
    lightXML.setInt( "cct", this.getCCT() );
    lightXML.setInt( "min_cct", this.getMinCCT() );
    lightXML.setInt(" max_cct", this.getMaxCCT() );
    return lightXML;
  }
  
  /**
   * Sets the colour temperature of this light source to the specified value (range standard 0-255).
   * @param newCCT
   */
  public void setCCT( int newCCT )
  {
	  this.setCCT( newCCT, true );
  }
  /**
   * Sets the colour temperature of this light source to the specified value (range standard 0-255).
   * @param newCCT
   */
  public void setCCT( int newCCT, boolean fireEvent )
  {
    if ( newCCT != this._cct )
    {
      this._cct  =  PApplet.constrain( newCCT, this._minCCT, this._maxCCT );
      if( fireEvent )
      {
      	this.fireLightEvent(LightEvent.CCT);
      }
    }
  }
  /**
   * Sets the colour temperature of this light source to the specified value (range standard 0-255).
   * Automatically converts from float to int.
   * @param newCCT
   */
  public void setCCT( float newCCT )
  {
    this.setCCT( (int) newCCT );
  }
  /**
   * Sets the colour temperature of this light source to the specified value (range standard 0-255).
   * Automatically converts from float to int.
   * @param newCCT
   */
  public void setCCT( float newCCT, boolean fireEvent)
  {
    this.setCCT( (int) newCCT, fireEvent );
  }
  /**
   * Returns the current colour temperature of this light source
   * @return int
   */
  public int getCCT()
  {
    return this._cct;
  }
  /**
   * Set the minimum colour temperature of this light source (standard 0)
   * @param cct
   */
  public void setMinCCT( int cct )
  {
    this._minCCT  =  cct;
  }
  /**
   * Returns the current low end of the colour temperature range (standard 0)
   * @return int
   */
  public int getMinCCT( )
  {
    return this._minCCT;
  }
  /**
   * Sets the high end of the colour temperature range (standard 255)
   * @param cct
   */
  public void setMaxCCT( int cct )
  {
    this._maxCCT  =  cct;
  }
  /**
   * Returns the high end of the colour temperature range (standard 255)
   * @return int
   */
  public int getMaxCCT()
  {
    return this._maxCCT;
  }
  /**
   * Set the range in which the colour temperature should be (standard 0-255)
   * @param minCCT
   * @param maxCCT
   */
  public void setRangeCCT( int minCCT, int maxCCT )
  {
	//We also calculate the new cct value of the light source based on the new range
	this.setCCT( PApplet.map( this.getCCT(), this.getMinCCT(), this.getMaxCCT(), minCCT, maxCCT ), false );
	//Then we set the new range
    this.setMinCCT( minCCT );
    this.setMaxCCT( maxCCT );
  }
  /**
   * Returns a representation of the light colour based on the current colour temperature
   * @return
   */
  public int getLightColor()
  {
    int lightColour	=	WHITE;
    if ( this.getCCT() <= (this.getMaxCCT()/2) )
    {
    	float amount =  PApplet.map( this.getCCT(), 0, this.getMaxCCT()/2, (float)0.0, (float)1.0);
    	lightColour  =  PApplet.lerpColor( CCTLight.WARMWHITE, CCTLight.WHITE, amount, PApplet.RGB );
    }
    else
    {
    	float amount	=	PApplet.map( this.getCCT(), this.getMaxCCT()/2, this.getMaxCCT(), (float)0.0, (float)1.0);
    	lightColour  =  PApplet.lerpColor( CCTLight.WHITE, CCTLight.COOLWHITE, amount , PApplet.RGB );
    }
    return lightColour;
  }
  
  /**
   * Set all parameters of this light source at once
   * @param state
   * @param intensity
   * @param cct
   */
  public void setParameters( boolean state, int intensity, int cct )
  {
    this.setState( state, false );	//Do not throw event
    this.setIntensity( intensity, false );
    this.setCCT( cct, false );
    
    this.fireLightEvent( LightEvent.ALL_PARAMETERS_CCT );
  }
  
  /**
   * Returns the information of this light source as a String
   */
  public String toString()
  {
	  String lightInfo	=	"(type: CCTLight)";
	  lightInfo			+=	" [id:"+this.getLightID()+"] ";
	  if( this.isOn() )
	  {
		  lightInfo	+=	" [on] ";
	  }
	  else
	  {
		  lightInfo	+=	" [off] ";
	  }
	  lightInfo			+=	" [intensity: ("+this.getMinIntensity()+")-"+this.getIntensity()+"("+this.getMaxIntensity()+")]";
	  lightInfo			+=	" [cct: ("+this.getMinCCT()+")-"+this.getCCT()+"("+this.getMaxCCT()+")]";
	  
	  return lightInfo;
  }
  
}
