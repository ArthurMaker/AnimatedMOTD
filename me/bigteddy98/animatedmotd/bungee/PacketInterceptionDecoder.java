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

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.bigteddy98.animatedmotd.bungee.ping.PingManager;
import me.bigteddy98.animatedmotd.bungee.ping.ServerData;
import me.bigteddy98.animatedmotd.bungee.ping.StatusListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.StatusResponse;

import com.google.gson.JsonObject;

public class PacketInterceptionDecoder extends MessageToMessageDecoder<PacketWrapper> {

	private final Main plugin;
	private StatusListener statusListener;
	private long previousTime = System.currentTimeMillis();
	private ChannelHandlerContext ctx;
	private ScheduledTask task;
	private boolean isPing = false;

	public PacketInterceptionDecoder(Main plugin) {
		this.plugin = plugin;
		try {
			this.statusListener = PingManager.getPingManager().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		task = ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable() {

			@Override
			public void run() {
				if ((System.currentTimeMillis() - previousTime) > 15000) {
					if (isPing) {
						ctx.close();
					}
					task.cancel();
					System.out.println("Animated Ping handler disconnected.");
				}
			}
		}, 1L, 1L, TimeUnit.SECONDS);
	}

	@Override
	protected void decode(final ChannelHandlerContext ctx, PacketWrapper packet, List<Object> out) throws Exception {
		this.previousTime = System.currentTimeMillis();
		this.ctx = ctx;
		if (packet.packet.getClass().getSimpleName().equals("PingPacket")) {
			isPing = true;
			final ServerData data = statusListener.update();
			ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable() {

				@Override
				public void run() {

					// respond with a response packet
					JsonObject version = new JsonObject();
					version.addProperty("name", data.getFormat().replace("%COUNT%", ProxyServer.getInstance().getOnlineCount() + "").replace("%MAX%", getMaxCount() + ""));
					version.addProperty("protocol", 10000);

					JsonObject countData = new JsonObject();
					countData.addProperty("max", 0);
					countData.addProperty("online", 0);

					JsonObject jsonObject = new JsonObject();
					jsonObject.add("version", version);
					jsonObject.add("players", countData);
					jsonObject.addProperty("description", data.getMotd());

					if (data.getFavicon() != null) {
						jsonObject.addProperty("favicon", data.getFavicon());
					}

					ctx.pipeline().writeAndFlush(new StatusResponse(jsonObject.toString()));
				}
			}, data.getSleepMillis(), TimeUnit.MILLISECONDS);
		} else {
			out.add(packet);
		}
	}

	@SuppressWarnings("deprecation")
	private int getMaxCount() {
		for (ListenerInfo listener : ProxyServer.getInstance().getConfig().getListeners()) {
			return listener.getMaxPlayers();
		}
		return -1;
	}
}
