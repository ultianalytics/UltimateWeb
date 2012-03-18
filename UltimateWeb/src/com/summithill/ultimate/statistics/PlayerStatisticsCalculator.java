package com.summithill.ultimate.statistics;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.summithill.ultimate.model.Game;
import com.summithill.ultimate.model.Team;
import com.summithill.ultimate.model.lightweights.Event;
import com.summithill.ultimate.model.lightweights.Point;
import com.summithill.ultimate.service.MobileService;

public class PlayerStatisticsCalculator {
	private MobileService service;
	private Map<String, PlayerStats> stats;
	
	public PlayerStatisticsCalculator(MobileService service) {
		super();
		this.service = service;
	}
	
	public Collection<PlayerStats> calculateStats(Team team, List<String> gameIds) {
		stats = new HashMap<String, PlayerStats>();
		
		for (String gameId : gameIds) {
			Game game = getGame(team, gameId);
			updateStats(game.getPoints());
		}
		
		return stats.values();
	}
	
	private void updateStats(List<Point> points) {
		Collections.reverse(points);
		for (Point point : points) {
			updatePointsPlayedStats(point);
			List<Event> events = point.getEvents();
			Collections.reverse(events);
			for (Event event : events) {
				if (event.getAction().equals("Catch")) {
					getStats(event.getPasser()).incPasses();
					getStats(event.getReceiver()).incCatches();
				} else if (event.getAction().equals("Drop")) {
					getStats(event.getPasser()).incPasses();
					getStats(event.getReceiver()).incDrops();
				} else if (event.getAction().equals("Throwaway")) {
					getStats(event.getPasser()).incThrowaways();
				} else if (event.getAction().equals("Pull")) {
					getStats(event.getDefender()).incPulls();					
				} else if (event.getAction().equals("D")) {
					getStats(event.getDefender()).incDs();
				} else if (event.getAction().equals("Goal") && event.getType().equals("Offense")) {
					getStats(event.getPasser()).incAssists();
					getStats(event.getReceiver()).incGoals();
				} 
			}
		}
	}

	private void updatePointsPlayedStats(Point point) {
		for (String name : point.getLine()) {
			PlayerStats playerStats = getStats(name);
			playerStats.incPointsPlayed();
			if ("O".equals(point.getSummary().getLineType())) {
				playerStats.incOPointsPlayed();
			} else {
				playerStats.incDPointsPlayed();
			}
		}
	}

	private PlayerStats getStats(String playerName) {
		PlayerStats playerStats = stats.get(playerName) ;
		if (playerStats == null) {
			playerStats = new PlayerStats(playerName);
			stats.put(playerName, playerStats);
		}
		return playerStats;
	}
	
	private Game getGame(Team team, String gameId) {
		return service.getGame(team, gameId);
	}
}
