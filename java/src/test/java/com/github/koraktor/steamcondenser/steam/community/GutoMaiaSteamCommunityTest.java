package com.github.koraktor.steamcondenser.steam.community;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.parsers.DOMParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;

import com.github.koraktor.steamcondenser.steam.community.tf2.TF2Stats;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DocumentBuilderFactory.class, DocumentBuilder.class})
public class GutoMaiaSteamCommunityTest {
		
	public Document loadXml(String file) throws Exception {
		try {
			DOMParser parser = new DOMParser();
			parser.parse("src/test/resources/"+file);
			return parser.getDocument();
		}catch (Exception e) {
			throw e;
		}
	}

	DocumentBuilder parser = mock(DocumentBuilder.class);
	DocumentBuilderFactory factory = mock(DocumentBuilderFactory.class);
	
	@Before
	public void init() throws Exception{
		mockStatic(DocumentBuilderFactory.class);
		when(DocumentBuilderFactory.newInstance()).thenReturn(factory);
		when(factory.newDocumentBuilder()).thenReturn(parser);
		
	}
	
	@Test
	public void getProfile() throws Exception{
		when(parser.parse("http://steamcommunity.com/id/gutomaia?xml=1")).thenReturn(loadXml("gutomaia.xml"));
				
		SteamId steamId = SteamId.create("gutomaia");
		
		verify(parser).parse("http://steamcommunity.com/id/gutomaia?xml=1");

		assertEquals(76561197985077150l, steamId.getSteamId64());
		assertEquals("gutomaia", steamId.getNickname());
		assertEquals("gutomaia",steamId.getCustomUrl());
		assertEquals("http://media.steampowered.com/steamcommunity/public/images/avatars/56/566f5c7e9126864777b7d9d3cfe9f8e62e27f706_full.jpg", steamId.getAvatarFullUrl());
		assertEquals("http://media.steampowered.com/steamcommunity/public/images/avatars/56/566f5c7e9126864777b7d9d3cfe9f8e62e27f706.jpg", steamId.getAvatarIconUrl());
		assertEquals("http://media.steampowered.com/steamcommunity/public/images/avatars/56/566f5c7e9126864777b7d9d3cfe9f8e62e27f706_medium.jpg", steamId.getAvatarMediumUrl());
		
		assertEquals(false, steamId.isBanned());
		assertEquals(false, steamId.isInGame());
		assertEquals(false, steamId.isOnline());
						
		assertEquals("Salvador, Bahia, Brazil",steamId.getLocation());
	}
	
	@Test
	public void getGames() throws Exception{
		when(parser.parse("http://steamcommunity.com/id/gutomaia?xml=1")).thenReturn(loadXml("gutomaia.xml"));		
		when(parser.parse("http://steamcommunity.com/id/gutomaia/games?xml=1")).thenReturn(loadXml("gutomaia-games.xml"));

		SteamId steamId = SteamId.create("gutomaia", true, false);
		HashMap<Integer, SteamGame> games = steamId.getGames();

		assertEquals(285, games.size());

		verify(parser).parse("http://steamcommunity.com/id/gutomaia/games?xml=1");
	}
	
	@Test
	public void getFriends() throws Exception{
		when(parser.parse("http://steamcommunity.com/id/gutomaia?xml=1")).thenReturn(loadXml("gutomaia.xml"));		
		when(parser.parse("http://steamcommunity.com/id/gutomaia/friends?xml=1")).thenReturn(loadXml("gutomaia-friends.xml"));
		
		SteamId steamId = SteamId.create("gutomaia", true, false);
		SteamId friends[] = steamId.getFriends();
		
		assertEquals(30, friends.length);
		
		//verify(parser).parse("http://steamcommunity.com/id/gutomaia?xml=1");
		verify(parser).parse("http://steamcommunity.com/id/gutomaia/friends?xml=1");
		
	}
	
	@Test
	public void getTF2Stats() throws Exception{
		when(parser.parse("http://steamcommunity.com/id/gutomaia?xml=1")).thenReturn(loadXml("gutomaia.xml"));
		when(parser.parse("http://steamcommunity.com/id/gutomaia/games?xml=1")).thenReturn(loadXml("gutomaia-friends.xml"));
		when(parser.parse("http://steamcommunity.com/id/gutomaia/stats/tf2?xml=all")).thenReturn(loadXml("gutomaia-tf2.xml"));

		SteamId steamId = SteamId.create("gutomaia", true, false);
		HashMap<Integer, SteamGame> games = steamId.getGames();

		TF2Stats tf2Stats = (TF2Stats) steamId.getGameStats("tf2");
		
		assertEquals("Team Fortress 2", tf2Stats.getGameName());
		assertEquals("TF2", tf2Stats.getGameFriendlyName());
		assertEquals(440, tf2Stats.getAppId());
	}
}
