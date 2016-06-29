/* Copyright (c) 2014 PixelScientists
 * 
 * The MIT License (MIT)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.mygdx.time.inventory;

/**
 * @author Daniel Holderbaum
 */
public enum Item {

	CRYSTAL_RED("redcrystal", "abc"),
	CRYSTAL_BLUE("bluecrystal", "dsaf"),
	CRYSTAL_GREEN("greencrystal", "dasfd"),
	CRYSTAL_YELLOW("yellowcrystal", "dasfdasd"),
	CRYSTAL_MAGENTA("magentacrystal", "???"),
	CRYSTAL_CYAN("cyancrystal", "dsaff"),
	CRYSTAL_ORANGE("orangecrystal", "dasfd"),
	CRYSTAL_VIOLET("violetcrystal", "dasfddasdsa"),
	TITANIUM("titanium", "ASdsdsa"),
	PALLADIUM("palladium", "d"),
	IRIDIUM("iridium", "ff"),
	RHODIUM("rhodium", "da"),
	HULL("hullbase", "asd"),
	CANNON("cannonbase", "asdf"),
	RAY("raybase", "dff"),
	LAUNCHER("launcherbase", "fdas"),
	DROID("droidbase", "asdf"),
	MINE("dropperbase", "fddsa"),
	BATTERY("batterybase", "fasdf");

	private String textureRegion, tooltip;

	private Item(String textureRegion, String tooltip) {
		this.textureRegion = textureRegion;
		this.tooltip = tooltip;
	}

	public String getTextureRegion() {
		return textureRegion;
	}
	
	public String getTooltip() {
		return tooltip;
	}

}
