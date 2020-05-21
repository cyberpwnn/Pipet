package ninja.bytecode.pipet;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

import lombok.Data;

@Data
public class PipetSystem
{
	private final GpioController gpio = GpioFactory.getInstance();

	public PipetSystem()
	{
		
	}
	
	public void unregisterAll()
	{
		gpio.unexportAll();
	}

	public void unregister(Pin... pins)
	{
		gpio.unexport(pins);
	}

	public GpioPinDigitalOutput registerOutput(Pin pin)
	{
		return registerOutput(pin, PinState.LOW);
	}

	public GpioPinDigitalOutput registerOutput(Pin pin, PinState initialState)
	{
		return registerOutput(pin, pin.getName(), initialState);
	}

	public GpioPinDigitalOutput registerOutput(Pin pin, String name)
	{
		return registerOutput(pin, name, PinState.LOW);
	}

	public GpioPinDigitalInput registerInput(Pin pin)
	{
		return registerInput(pin, pin.getName());
	}

	public GpioPinDigitalInput registerInput(Pin pin, PinPullResistance resistance)
	{
		return registerInput(pin, pin.getName(), resistance);
	}

	public GpioPinDigitalInput registerInput(Pin pin, String name)
	{
		return registerInput(pin, name, PinPullResistance.PULL_DOWN);
	}

	public GpioPinDigitalInput registerInput(Pin pin, String name, PinPullResistance resistance)
	{
		GpioPinDigitalInput g = gpio.provisionDigitalInputPin(pin, name, resistance);
		return g;
	}

	public GpioPinDigitalOutput registerOutput(Pin pin, String name, PinState initialState)
	{
		GpioPinDigitalOutput g = gpio.provisionDigitalOutputPin(pin, name, initialState);

		return g;
	}

}
