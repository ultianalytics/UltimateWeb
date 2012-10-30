package com.summithill.ultimate.statistics;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PlayerStats {
	private String playerName;
	private int plusMinusCount;
	private int gamesPlayed;
	private int pointsPlayed;
	private int oPointsPlayed;
	private int dPointsPlayed;
	private int goals;
	private int assists;
	private int passes;
	private int passSuccess;
	private int catches;
	private int catchSuccess;
	private int drops;
	private int throwaways;
	private int ds;
	private int pulls;
	private int pullsWithHangtime;
	private int pullsOB;
	private int pullsAvgHangtimeMillis;
	private int touches;
	private int secondsPlayed;
	@JsonIgnore
	private int pullsTotalHangtime = 0;
	
	public PlayerStats(String playerName) {
		super();
		this.playerName = playerName;
	}
	
	public void incGamesPlayed() {
		this.gamesPlayed++;
	}
	public void incPointsPlayed() {
		this.pointsPlayed++;
	}	
	public void incOPointsPlayed() {
		this.oPointsPlayed++;
	}
	public void incDPointsPlayed() {
		this.dPointsPlayed++;
	}
	public void incGoals() {
		this.goals++;
	}
	public void incAssists() {
		this.assists++;
	}
	public void incPasses() {
		this.passes++;
	}
	public void incCatches() {
		this.catches++;
	}	
	public void incDrops() {
		this.drops++;
	}
	public void incThrowaways() {
		this.throwaways++;
	}
	public void incDs() {
		this.ds++;
	}
	public void incPulls() {
		this.pulls++;
	}
	public void incPulls(int hangtimeMilliseconds) {
		this.pulls++;
		if (hangtimeMilliseconds > 0) {
			this.pullsWithHangtime++;
			this.pullsTotalHangtime += hangtimeMilliseconds;
			this.pullsAvgHangtimeMillis = this.pullsTotalHangtime / this.pullsWithHangtime;
		} else {
			this.incPulls();
		}
	}
	public void incPullOBs() {
		this.pullsOB++;
	}	
	public void incTouches() {
		this.touches++;
	}
	public void incPlusMinusCount() {
		this.plusMinusCount++;
	}
	public void decPlusMinusCount() {
		this.plusMinusCount--;
	}	
	public void addSecondsPlayed(long seconds) {
		this.secondsPlayed += seconds;
	}
	public int getPointsPlayed() {
		return pointsPlayed;
	}
	public void setPointsPlayed(int pointsPlayed) {
		this.pointsPlayed = pointsPlayed;
	}
	public int getOPointsPlayed() {
		return oPointsPlayed;
	}
	public void setOPointsPlayed(int oPointsPlayed) {
		this.oPointsPlayed = oPointsPlayed;
	}
	public int getDPointsPlayed() {
		return dPointsPlayed;
	}
	public void setDPointsPlayed(int dPointsPlayed) {
		this.dPointsPlayed = dPointsPlayed;
	}
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
	public int getAssists() {
		return assists;
	}
	public void setAssists(int assists) {
		this.assists = assists;
	}
	public int getPasses() {
		return passes;
	}
	public void setPasses(int passes) {
		this.passes = passes;
	}
	public int getDrops() {
		return drops;
	}
	public void setDrops(int drops) {
		this.drops = drops;
	}
	public int getThrowaways() {
		return throwaways;
	}
	public void setThrowaways(int throwaways) {
		this.throwaways = throwaways;
	}
	public int getDs() {
		return ds;
	}
	public void setDs(int ds) {
		this.ds = ds;
	}
	public int getPulls() {
		return pulls;
	}
	public void setPulls(int pulls) {
		this.pulls = pulls;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getCatches() {
		return catches;
	}

	public void setCatches(int catches) {
		this.catches = catches;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	public int getTouches() {
		return touches;
	}

	public void setTouches(int touches) {
		this.touches = touches;
	}

	public int getSecondsPlayed() {
		return secondsPlayed;
	}

	public void setSecondsPlayed(int secondsPlayed) {
		this.secondsPlayed = secondsPlayed;
	}

	public int getPlusMinusCount() {
		return plusMinusCount;
	}

	public void setPlusMinusCount(int plusMinusCount) {
		this.plusMinusCount = plusMinusCount;
	}

	public int getPullsOB() {
		return pullsOB;
	}

	public void setPullsOB(int pullsOB) {
		this.pullsOB = pullsOB;
	}

	public int getPullsAvgHangtimeMillis() {
		return pullsAvgHangtimeMillis;
	}

	public void setPullsAvgHangtimeMillis(int avgHangtime) {
		this.pullsAvgHangtimeMillis = avgHangtime;
	}

	public int getPullsWithHangtime() {
		return pullsWithHangtime;
	}

	public void setPullsWithHangtime(int pullsWithHangtime) {
		this.pullsWithHangtime = pullsWithHangtime;
	}

	public int getPassSuccess() {
		return passSuccess;
	}

	public void setPassSuccess(int passSuccess) {
		this.passSuccess = passSuccess;
	}

	public int getCatchSuccess() {
		return catchSuccess;
	}

	public void setCatchSuccess(int catchSuccess) {
		this.catchSuccess = catchSuccess;
	}
}
