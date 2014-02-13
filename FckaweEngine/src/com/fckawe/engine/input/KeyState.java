package com.fckawe.engine.input;

/**
 * Represents the state of a key. The state contains information if the key was
 * pressed and if so, if it was pressed holding another key (like shift, alt,
 * control and so on) down.
 * 
 * @author fckawe
 */
public class KeyState {

	/**
	 * Bit value for the shift hold key state.
	 */
	public static final short HELD_SHIFT = 1;

	/**
	 * Bit value for the control hold key state.
	 */
	public static final short HELD_CONTROL = 2;

	/**
	 * Bit value for the alt hold key state.
	 */
	public static final short HELD_ALT = 4;

	/**
	 * Bit value for the altgr hold key state.
	 */
	public static final short HELD_ALT_GR = 8;

	/**
	 * Bit value for the meta hold key state.
	 */
	public static final short HELD_META = 16;

	private boolean pressed;
	private short heldKey;
	private boolean nextPressed;
	private short nextHeldKey;

	/**
	 * Sets the next key state that will get activated with the next tick.
	 * 
	 * @param pressed
	 *            True, if the key was pressed, false if it was released.
	 * @param withShift
	 *            True, if the key was pressed while holding the shift key down.
	 * @param withControl
	 *            True, if the key was pressed while holding the control key
	 *            down.
	 * @param withAlt
	 *            True, if the key was pressed while holding the alt key down.
	 * @param withAltGr
	 *            True, if the key was pressed while holding the altgr key down.
	 * @param withMeta
	 *            True, if the key was pressed while holding the meta key down.
	 */
	public void setNextState(final boolean pressed, final boolean withShift,
			final boolean withControl, final boolean withAlt,
			final boolean withAltGr, final boolean withMeta) {
		nextPressed = pressed;

		nextHeldKey = 0;
		if (withShift) {
			nextHeldKey |= HELD_SHIFT;
		}
		if (withControl) {
			nextHeldKey |= HELD_CONTROL;
		}
		if (withAlt) {
			nextHeldKey |= HELD_ALT;
		}
		if (withAltGr) {
			nextHeldKey |= HELD_ALT_GR;
		}
		if (withMeta) {
			nextHeldKey |= HELD_META;
		}
	}

	/**
	 * Performs a tick on this key state object, which means that the next key
	 * state gets activated.
	 */
	public void tick() {
		pressed = nextPressed;
		heldKey = nextHeldKey;
	}

	/**
	 * Consumes and resets the key state, which means that the key event will
	 * not get evaluated twice.
	 */
	public void consume() {
		pressed = false;
		heldKey = 0;
		nextPressed = false;
		nextHeldKey = 0;
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
	 * @param heldKey
	 *            The bit mask specifying the held keys that should be held down
	 *            while pressing the key.
	 * @return True, if the key was pressed while holding the specified held
	 *         keys.
	 */
	public boolean isPressedWith(final short heldKey) {
		if (pressed) {
			if (this.heldKey == 0 && heldKey == 0) {
				return true;
			} else if ((this.heldKey - heldKey) == 0) {
				return true;
			}
		}
		return false;
	}

}
