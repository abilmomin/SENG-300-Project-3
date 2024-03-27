package com.jjjwelectronics.keyboard;

import java.util.List;

/**
 * Represents a physical keyboard. The individual keys have labels but no
 * physical location. This keyboard is in the US style, with a "QWERTY"
 * arrangement.
 * 
 * @author JJJW Electronics LLP
 */
public class USKeyboardQWERTY extends AbstractKeyboard {
	/**
	 * This is an unmodifiable map, usable as a configuration for the keyboard. It
	 * is based on the keyboard I am using to write this.
	 */
	public static final List<String> WINDOWS_QWERTY;

	static {
		String[] windowsQwertyLabels = new String[] { /* Row 1 */ "FnLock Esc", "F1", "F2", "F3", "F4", "F5", "F6",
			"F7", "F8", "F9", "F10", "F11", "F12", "Home", "End", "Insert", "Delete", /* Row 2 */ "` ~", "1 !", "2 @",
			"3 #", "4 $", "5 %", "6 ^", "7 &", "8 *", "9 (", "0 )", "- _", "= +", "Backspace", /* Row 3 */ "Tab", "Q",
			"W", "E", "R", "T", "Y", "U", "I", "O", "P", "[ {", "] }", "\\ |", /* Row 4 */ "CapsLock", "A", "S", "D",
			"F", "G", "H", "J", "K", "L", "; :", "' \"", "Enter", /* Row 5 */ "Shift (Left)", "Z", "X", "C", "V", "B",
			"N", "M", ", <", ". >", "/ ?", "Shift (Right)", /* Row 6 */ "Fn", "Ctrl (Left)", "Windows", "Alt (Left)",
			"Spacebar", "Alt (Right)", "PrtSc", "Ctrl (Right)", "PgUp", "Up Arrow", "PgDn", "Left Arrow", "Down Arrow",
			"Right Arrow" };

		WINDOWS_QWERTY = List.of(windowsQwertyLabels);
	}

	/**
	 * Constructs a US keyboard with a "QWERTY" arrangement.
	 */
	public USKeyboardQWERTY() {
		super(WINDOWS_QWERTY);
	}
}
