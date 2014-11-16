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

public class PingManager {

	private static Class<? extends StatusListener> pingExecutor = DefaultPingManager.class;

	public static Class<? extends StatusListener> getPingManager() {
		return PingManager.pingExecutor;
	}

	public static void setPingManager(Class<? extends StatusListener> pingManager) {
		PingManager.pingExecutor = pingManager;
	}
}
