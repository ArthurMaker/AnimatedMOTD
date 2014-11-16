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
package me.bigteddy98.animatedmotd.bungee;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import me.bigteddy98.animatedmotd.bungee.ping.DefaultPingManager;
import me.bigteddy98.animatedmotd.bungee.ping.PingManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.netty.PipelineUtils;

public class Main extends Plugin {

	@Override
	public void onEnable() {
		try {
			this.setStaticFinalValue(PipelineUtils.class.getDeclaredField("SERVER_CHILD"), new ConnectionReplacement(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PingManager.setPingManager(DefaultPingManager.class);
	}

	private void setStaticFinalValue(Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}
}
