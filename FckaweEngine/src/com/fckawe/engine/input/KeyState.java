package com.fckawe.engine.input;

/**
 * Represents the state of a key. The state contains information if the key was
 * pressed and if so, if it was pressed holding another key (like shift, alt,
 * control and so on) down.
 * 
 * @author fckawe
 */
public class KeyState {

	private boolean pressed;
	private int modifiers;
	private boolean nextPressed;
	private int nextModifiers;

	/**
	 * Sets the next key state that will get activated with the next tick.
	 * 
	 * @param pressed
	 *            True, if the key was pressed, false if it was released.
	 * @param modifiers
	 *            The modifiers bit mask.
	 */
	public void setNextState(final boolean pressed, final int modifiers) {
		nextPressed = pressed;
		nextModifiers = modifiers;
	}

	/**
	 * Performs a tick on this key state object, which means that the next key
	 * state gets activated.
	 */
	public void tick() {
		pressed = nextPressed;
		modifiers = nextModifiers;
	}

	/**
	 * Consumes and resets the key state, which means that the key event will
	 * not get evaluated twice.
	 */
	public void consume() {
		pressed = false;
		modifiers = 0;
	}

	/**
	 * Returns true, if the key was pressed without any held key.
	 * 
	 * @return True, if the key was pressed without any held key.
	 */
	public boolean isPressed() {
		return isPressedWith((short) 0);
	}

	/**
	 * Returns true, if the key was pressed while holding the keys down, which
	 * are specified with the heldKey bits.
	 * 
	 * @param modifiers
	 *            Modifiers bit mask (see KeyEvent constants).
	 * @return True, if the key was pressed while holding the specified held
	 *         keys.
	 */
	public boolean isPressedWith(final int modifiers) {
		if (pressed) {
			if (this.modifiers == 0 && modifiers == 0) {
				return true;
			}
			return this.modifiers == modifiers;
		}
		return false;
	}

}
