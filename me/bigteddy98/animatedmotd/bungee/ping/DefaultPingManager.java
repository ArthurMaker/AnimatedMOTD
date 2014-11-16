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

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.bigteddy98.animatedmotd.bungee.Scroller;
import net.md_5.bungee.api.ChatColor;

public class DefaultPingManager implements StatusListener {

	private final Scroller scroller1 = new Scroller(ChatColor.GOLD + "This is a scrolling MOTD", 60, 15, '&');
	private final Scroller scroller2 = new Scroller(ChatColor.GOLD + "This is another scrolling MOTD", 60, 15, '&');
	private int i = 1;

	@Override
	public ServerData update() {
		if (i == 4) {
			i = 1;
		}
		try {
			return new ServerData(this.scroller1.next() + "     " + this.scroller2.next(), ImageIO.read(new File("C:\\Users\\Sander\\Desktop\\sword" + i++ + ".png")), 300, ChatColor.WHITE + "%COUNT%" + ChatColor.GOLD + "/" + ChatColor.WHITE + "%MAX% " + ChatColor.WHITE + "Players Online");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
