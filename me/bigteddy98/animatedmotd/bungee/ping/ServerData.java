/* 
 * AnimatedMOTD BungeePlugin
 * Copyright (C) 2014 Sander Gielisse
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.bigteddy98.animatedmotd.bungee.ping;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

public class ServerData {

	private final String motd;
	private String favicon;
	private final int sleepTime;
	private final String format;

	public ServerData(String motd, BufferedImage pngIcon, int sleepTime, String format) {
		this.motd = motd;

		if (pngIcon != null) {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				if (pngIcon.getWidth() == 64 && pngIcon.getHeight() == 64) {
					ImageIO.write(pngIcon, "png", baos);
					baos.flush();
					this.favicon = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
				} else {
					throw new RuntimeException("Your server-icon.png needs to be 64*64!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.sleepTime = sleepTime;
		this.format = format;
	}

	public String getMotd() {
		return motd;
	}

	public String getFavicon() {
		return favicon;
	}

	public int getSleepMillis() {
		return sleepTime;
	}

	public String getFormat() {
		return format;
	}
}
