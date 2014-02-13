package com.fckawe.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * The key input handler.
 * 
 * @author fckawe
 */
public class InputHandler implements KeyListener {

	private final Map<Integer, KeyState> keyMap;

	/**
	 * Constructor to create a new input handler. This registers the default
	 * keys automatically.
	 */
	public InputHandler() {
		keyMap = new HashMap<Integer, KeyState>();
		registerDefaultKeys();
	}

	/**
	 * Registers the default keys.
	 */
	public void registerDefaultKeys() {
		keyMap.clear();
		keyMap.put(KeyEvent.VK_UP, newKeyState());
		keyMap.put(KeyEvent.VK_DOWN, newKeyState());
		keyMap.put(KeyEvent.VK_LEFT, newKeyState());
		keyMap.put(KeyEvent.VK_RIGHT, newKeyState());
		keyMap.put(KeyEvent.VK_ENTER, newKeyState());
		keyMap.put(KeyEvent.VK_ESCAPE, newKeyState());
	}

	/**
	 * Creates a new key state object.
	 * 
	 * @return A newly created key state object.
	 */
	protected KeyState newKeyState() {
		return new KeyState();
	}

	/**
	 * Performs a tick on the input handler and therefore on all registered
	 * keys.
	 */
	public void tick() {
		for (KeyState key : keyMap.values()) {
			key.tick();
		}
	}

	@Override
	public void keyPressed(final KeyEvent event) {
		toggle(event, true);
	}

	@Override
	public void keyReleased(final KeyEvent event) {
		toggle(event, false);
	}

	@Override
	public void keyTyped(final KeyEvent event) {
		// nothing to do
	}

	private void toggle(final KeyEvent event, final boolean pressed) {
		KeyState key = keyMap.get(event.getKeyCode());
		if (key != null) {
			key.setNextState(pressed, event.isShiftDown(),
					event.isControlDown(), event.isAltDown(),
					event.isAltGraphDown(), event.isMetaDown());
		}

	}

	/**
	 * Returns the key state object for the specified key code.
	 * 
	 * @param key
	 *            The key code (one of the KeyEvent constants).
	 * @return The key state object.
	 */
	public KeyState getKeyState(final int key) {
		if (keyMap.containsKey(key)) {
			return keyMap.get(key);
		}
		return null;
	}

	/**
	 * Returns true, if the specified key was pressed. Convenience method to
	 * easily access the key's state.
	 * 
	 * @param key
	 *            The key code (one of the KeyEvent constants).
	 * @return True, if the key was pressed.
	 */
	public boolean isPressed(final int key) {
		KeyState state = getKeyState(key);
		if (state != null) {
			return state.isPressed();
		}
		return false;
	}

	/**
	 * Returns true, if the specified key was pressed while holding the
	 * specified held keys down. Convenience method to easily access the key's
	 * state.
	 * 
	 * @param key
	 *            The key code (one of the KeyEvent constants).
	 * @param heldKey
	 *            The bit mask specifying the held keys that should be held down
	 *            while pressing the key.
	 * @return True, if the key was pressed while holding the specified held
	 *         keys.
	 */
	public boolean isPressedWith(final int key, final short heldKey) {
		KeyState state = getKeyState(key);
		if (state != null) {
			return state.isPressedWith(heldKey);
		}
		return false;
	}

	/**
	 * Consumes and resets the key state for the key with the specified code,
	 * which means that the key event will not get evaluated twice. Convenience
	 * method to easily access the key's state.
	 * 
	 * @param key
	 */
	public void consume(final int key) {
		KeyState state = getKeyState(key);
		if (state != null) {
			state.consume();
		}
	}

}