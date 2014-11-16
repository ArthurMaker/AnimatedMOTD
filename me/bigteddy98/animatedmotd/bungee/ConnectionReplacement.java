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

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import java.net.InetSocketAddress;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.KickStringWriter;
import net.md_5.bungee.protocol.LegacyDecoder;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;

public final class ConnectionReplacement extends ChannelInitializer<Channel> {

	private final Main plugin;

	public ConnectionReplacement(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initChannel(Channel ch) throws Exception {
		if (BungeeCord.getInstance().getConnectionThrottle().throttle(((InetSocketAddress) ch.remoteAddress()).getAddress())) {
			// TODO: Better throttle - we can't throttle this way if we want to
			// maintain 1.7 compat!
			// ch.close();
			// return;
		}
		PipelineUtils.BASE.initChannel(ch);
		ch.pipeline().addBefore(PipelineUtils.FRAME_DECODER, PipelineUtils.LEGACY_DECODER, new LegacyDecoder());
		ch.pipeline().addAfter(PipelineUtils.FRAME_DECODER, PipelineUtils.PACKET_DECODER, new MinecraftDecoder(Protocol.HANDSHAKE, true, ProxyServer.getInstance().getProtocolVersion()));
		ch.pipeline().addAfter(PipelineUtils.FRAME_PREPENDER, PipelineUtils.PACKET_ENCODER, new MinecraftEncoder(Protocol.HANDSHAKE, true, ProxyServer.getInstance().getProtocolVersion()));
		
		ch.pipeline().addAfter(PipelineUtils.PACKET_DECODER, "packet-interception", new PacketInterceptionDecoder(this.plugin));
		
		ch.pipeline().addBefore(PipelineUtils.FRAME_PREPENDER, PipelineUtils.LEGACY_KICKER, new KickStringWriter());
		ch.pipeline().get(HandlerBoss.class).setHandler(new InitialHandler(ProxyServer.getInstance(), ch.attr(PipelineUtils.LISTENER).get()));
	}
}