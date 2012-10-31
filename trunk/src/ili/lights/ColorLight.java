package ili.lights;

import processing.core.PApplet;
import processing.xml.XMLElement;

/**
 * The ColorLight is a subclass of the Light object and contains a light color, that can be set
 * using RGB and HSB parameters
 */
public class ColorLight extends Light
{
	public static final int RGB_MODE  =  1;
	public static final int HSB_MODE  =  2;
	
	private int _hue		=	127;
	private int _saturation	=	0;
	private int _brightness	=	255;
	
	private int _red		=	255;
	private int	_green		=	255;
	private int	_blue		=	255;

  /**
   * Empty constructor, creates standard Colored light.
   */
	public ColorLight() { super(255); }
  /**
   * Creates a colored light with the specified intensity and the specified R, G, and B values.
   * @param intensity
   * @param R
   * @param G
   * @param B
   */
	public ColorLight( int intensity, int r, int g, int b )
	{
	  super(intensity);
	  this.setRed( r, false );
	  this.setGreen( g, false );
	  this.setBlue( b, false );
	}
  /**
   * Creates a colored light with the specified intensity and the specified R, G, and B values
   * or H, S, and B values, depending on the colormode (RGB_MODE, HSB_MODE)
   * @param intensity
   * @param R/H
   * @param G/S
   * @param B
   * @param colorMode
   */
  ColorLight( int intensity, int one, int two, int three, int colorMode )
  {
    super(intensity);
    if( colorMode == ColorLight.HSB_MODE )
    {
    	this.setHue( one, false );
    	this.setSaturation( two, false );
    	this.setBrightness( three, false );
    }
    else
    {
    	this.setRed( one, false );
    	this.setGreen( two, false );
    	this.setBlue( three, false );
    }
  }
  /**
   * Creates a new ColorLight object with the specified intensity and the specified color
   * @param intensity
   * @param colour
   */
  ColorLight( int intensity, int colour )
  {
    super(intensity);
    this.setColor( colour, false );
  }
  /**
   * Returns this light source representation as an XMLElement in the format
   * <Light type=String lightid=int state=boolean intensity=int min_intensity=int max_intensity=int
   * color=int />
   * @return XMLElement
   */
  public XMLElement getAsXML()
  {
    XMLElement lightXML  =  new XMLElement("Light");
    lightXML.setString("type", "rgb");
    lightXML.setInt( "lightid", this.getLightID() );
    lightXML.setBoolean( "state", this.getState() );
    lightXML.setInt( "intensity", this.getIntensity() );
    lightXML.setInt( "min_intensity", this.getMinIntensity() );
    lightXML.setInt( "max_intensity", this.getMaxIntensity() );
    lightXML.setInt( "color", this.getColor() );
    return lightXML;
  }
  /**
   * Sets the colour of this light source to the specified colour. 
   * Returns true if the action has been successful. Automatically throws a LightEvent.
   * @param lightColor
   * @return boolean
   */
  public boolean setColor( int lightColor )
  {
	  return this.setColor(lightColor, true);
  }
  /**
   * Sets the colour of this light source to the specified colour. 
   * Returns true if the action has been succesful
   * @param lightColor
   * @param fireEvent
   * @return boolean
   */
  public boolean setColor( int lightColor, boolean fireEvent )
  {
    boolean updated  =  false;
    if ( this.getColor() != lightColor )
    {
    	this.setRed( lightColor >> 16 & 0xFF, false );
    	this.setGreen( lightColor >> 8 & 0xFF, false );
    	this.setBlue( lightColor & 0xFF, false );
    	updated  =  true;
    	if( fireEvent )
    	{
    		this.fireLightEvent( LightEvent.COLOR );
    	}
    }
    return updated;
  }

  /**
   * Returns the current colour this light source is set to
   * @return int
   */
  public int getColor()
  {
    return (this.getRed()<<16) + (this.getGreen()<<8) + this.getBlue() ;
  }
  /**
   * Returns the current hue value of this light colour (range: 0-255)
   * @return int
   */
  public int getHue()
  {
    return this._hue;
  }
  /**
   * Sets the new hue value to the specified value. Returns the new color value.
   * Automatically throws an event.
   * @param int
   * @return int
   */
  public int setHue( int hue )
  {
	  return this.setHue( hue, true );
  }
  /**
   * Sets the new hue value to the specified value. Returns the new color value. Automatically throws an event.
   * @param float
   * @return int
   */
  public int setHue( float hue )
  {
	  return this.setHue( (int) hue, true );
  }
  /**
   * Sets the new hue value to the specified value. 
   * Returns the new color value. 
   * Throws a LightEvent.RED type of event if fireEvent set to true.
   * @param hue
   * @param fireEvent
   * @return int
   */
  public int setHue( int hue, boolean fireEvent )
  {
	  this._hue	=	hue;
	  if( fireEvent )
	  {
		  this.fireLightEvent(LightEvent.HUE);
	  }
	  this.updateRGB();
	  return this.getColor();
  }
  /**
   * Returns the current saturation value of the light source.
   * @return int
   */
  public int getSaturation()
  {
    return this._saturation;
  }
  /**
   * Updates the saturation value to the specified value. 
   * Returns the new color value.
   * Automatically throws a LightEvent.SATURATION type of event.
   * @param saturation
   * @return int
   */
  public int setSaturation( int saturation )
  {
	  return this.setSaturation(saturation, true);
  }
  /**
   * Updates the saturation value to the specified value. 
   * Returns the new color value.
   * Throws a LightEvent.SATURATION if fireEvent is set to true.
   * @param saturation
   * @param fireEvent
   * @return int
   */
  public int setSaturation( int saturation, boolean fireEvent )
  {
	  this._saturation	=	saturation;
	  if( fireEvent )
	  {
		  this.fireLightEvent(LightEvent.SATURATION);
	  }
	  this.updateRGB();
	  return this.getColor();
  }
  /**
   * Returns the current brightness value of this colour
   * @return int
   */
  public int getBrightness()
  {
    return this._brightness;
  }
  
  /**
   * Updates the brightness to the specified value. 
   * Returns the new color value.
   * Automatically throws a LightEvent.BRIGHTNESS type of event.
   * @param brightness
   * @return int
   */
  public int setBrightness( int brightness )
  {
	  return this.setBrightness( brightness, true );
  }

  /**
   * Updates the brightness to the specified value. Returns the new color value.
   * @param brightness
   * @param fireEvent
   * @return int
   */
  public int setBrightness( int brightness, boolean fireEvent )
  {
	  this._brightness	=	brightness;
	  if( fireEvent )
	  {
		  this.fireLightEvent(LightEvent.BRIGHTNESS);
	  }
	  this.updateRGB();
	  return this.getColor();
  }
  
  /**
   * Returns the red value of the current color of the light source
   * @return int
   */
  public int getRed()
  {
	  return this._red;
  }
  
  /**
   * Updates the red value of the color to the new value. 
   * Returns the new color value.
   * Automatically throws a LightEvent.RED type of event.
   * @param red
   * @return int
   */
  public int setRed( int red )
  {
	  return this.setRed(red, true);
  }
  /**
   * Updates the red value of the color to the new value. Returns the new color value.
   * @param red
   * @return int
   */
  public int setRed( int red, boolean fireEvent )
  {
	  this._red	=	red;
	  if( fireEvent )
	  {
		  this.fireLightEvent(LightEvent.RED);
	  }
	  this.updateHSB();
	  
	  return this.getColor();
  }
  /**
   * Returns the green value of the current color of this light source
   * @return int
   */
  public int getGreen()
  {
	  return this._green;
  }
  /**
   * Updates the green value of the color to the new value. Returns the new color value.
   * @param red
   * @return int
   */
  public int setGreen( int green )
  {
	  return this.setGreen( green, true );
  }
  /**
   * Updates the green value of the color to the new value. Returns the new color value.
   * @param red
   * @param fireEvent
   * @return int
   */
  public int setGreen( int green, boolean fireEvent )
  {
	  this._green	=	green;
	  if( fireEvent )
	  {
		  this.fireLightEvent(LightEvent.GREEN);
	  }
	  this.updateHSB();
	  
	  return this.getColor();
  }
  /**
   * Returns the current blue value of the current color of this light source
   * @return int
   */
  public int getBlue()
  {
	  return this._blue;
  }
  
  /**
   * Updates the blue value of the color to the new value. 
   * Returns the new color value.
   * Automatically throws a LightEvent.BLUE type of event.
   * @param red
   * @param fireEvent
   * @return int
   */
  public int setBlue( int blue )
  {
	  return this.setBlue(blue, true);
  }
  /**
   * Updates the blue value of the color to the new value. 
   * Returns the new color value.
   * Throws a LightEvent.BLUE type of event if fireEvent set to true
   * @param red
   * @param fireEvent
   * @return int
   */
  public int setBlue( int blue, boolean fireEvent )
  {
	  this._blue	=	blue;
	  if( fireEvent )
	  {
	   	this.fireLightEvent(LightEvent.BLUE);
	  }
	  this.updateHSB();
	  
	  return this.getColor();
  }
  
  /**
   * Updates all parameters and throws an event
   * @param state
   * @param intensity
   * @param colour
   */
  public void setParameters( boolean state, int intensity, int colour )
  {
	  this.setParameters(state, intensity, colour, true);
  }
  /**
   * Updates all parameters and throws an event of type LightEvent.ALL_PARAMETERS_RGB.
   * @param state
   * @param intensity
   * @param colour
   * @param fireEvent
   */
  public void setParameters( boolean state, int intensity, int colour, boolean fireEvent )
  {
    this.setState( state );
    this.setIntensity( intensity );
    this.setColor( colour );
    if( fireEvent )
    {
    	this.fireLightEvent(LightEvent.ALL_PARAMETERS_RGB);
    }
  }
  
  
  /**
   * This function should be called whenever one of the RGB values is changed. It will calculate the respective HSB values.
   */
  private void updateHSB()
  {
	  float rd 		= (float) this.getRed()/255;
      float gd 		= (float) this.getGreen()/255;
      float bd 		= (float) this.getBlue()/255;
      float maxVal	= PApplet.max(rd, PApplet.max(gd, bd));
      float minVal	= PApplet.min(rd, PApplet.min(gd, bd));
      float h 		= maxVal;
      float s 		= maxVal;
      float v 		= maxVal;
   
      float d = maxVal - minVal;
      s = maxVal == 0 ? 0 : d / maxVal;
   
      if (maxVal == minVal) 
      {
          h = 0; // achromatic
      } 
      else 
      {
          if (maxVal == rd) 
          {
              h = (gd - bd) / d + (gd < bd ? 6 : 0);
          } 
          else if (maxVal == gd) 
          {
              h = (bd - rd) / d + 2;
          } 
          else if (maxVal == bd) 
          {
              h = (rd - gd) / d + 4;
          }
          h /= 6;
      }
      h *= 255;
      s *= 255;
      v *= 255;
   
      this._hue			=	(int) h;
      this._saturation	=	(int) s;
      this._brightness	=	(int) v;
  }
  
  private void updateRGB()
  {
	  int r	= 255, g = 255, b = 255;
      int hue		= (int) PApplet.map(this.getHue(),0,255,0,359);
      int phase		= hue/60;
      int bottom	= (int) ( (255 - this.getSaturation() ) * ( this.getBrightness() / 255.0) );
      int top		= this.getBrightness();
      int rising	= ((top-bottom)  *(hue%60   )  )  /  60  +  bottom;
      int falling	= ((top-bottom)  *(60-hue%60)  )  /  60  +  bottom;
     
      switch(phase) 
      {
      case 0:
          r = top;
          g = rising;
          b = bottom;
          break;

      case 1:
          r = falling;
          g = top;
          b = bottom;
          break;

      case 2:
          r = bottom;
          g = top;
          b = rising;
          break;

      case 3:
          r = bottom;
          g = falling;
          b = top;
          break;

      case 4:
    	  r = rising;
    	  g = bottom;
    	  b = top;
    	  break;

      case 5:
    	  r = top;
    	  g = bottom;
    	  b = falling;
    	  break;
      }

	this._red	=	r;
	this._green =	g;
	this._blue	=	b;
  }
  
  /**
   * Returns the information of this light source as a String
   */
  public String toString()
  {
	  String lightInfo	=	"(type: ColorLight)";
	  if( this.isOn() )
	  {
		  lightInfo	+=	" [on] ";
	  }
	  else
	  {
		  lightInfo	+=	" [off] ";
	  }
	  lightInfo	+=	" [id:"+this.getLightID()+"] ";
	  lightInfo	+=	" [intensity: ("+this.getMinIntensity()+")-"+this.getIntensity()+"("+this.getMaxIntensity()+")]";
	  lightInfo	+=	" [r:"+this.getRed()+" g:"+this.getGreen()+" b:"+this.getBlue()+"]";
	  lightInfo	+=	" [h:"+this.getHue()+" s:"+this.getSaturation()+" b:"+this.getBrightness()+"]";
	  
	  return lightInfo;
  }
  
}