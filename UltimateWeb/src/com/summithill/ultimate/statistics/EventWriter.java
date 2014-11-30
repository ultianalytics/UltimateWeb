package com.summithill.ultimate.statistics;

import java.io.IOException;
import java.io.Writer;

import com.summithill.ultimate.model.Game;
import com.summithill.ultimate.model.lightweights.Event;
import com.summithill.ultimate.model.lightweights.Point;
import com.summithill.ultimate.model.lightweights.PointSummary;

public class EventWriter {
	private static String DELIMITER = ",";
	private static String DELIMITER_REPLACEMENT = "-";
	private int MAX_PLAYERS_IN_POINT = 28;
	private Writer writer;
	private Anonymizer anonymizer;
	private String teamId;
	
	public EventWriter(Writer writer) {
		this(writer, null, null);
	}
	
	public EventWriter(Writer writer, Anonymizer anonymizer, String teamId) {
		super();
		this.writer = writer;
		this.anonymizer = anonymizer;
		this.teamId = teamId;
		this.writeHeader();
	}

	public void writeEvent(Event event, Game game, Point point) {
		PointSummary pointSummary = point.getSummary();
		try {
			if (this.teamId != null) {
				this.writeWithDelimiterAfter(teamDisplayName(this.teamId));
			}
			this.writeWithDelimiterAfter(displayableTimestamp(game.getTimestamp()));
			this.writeWithDelimiterAfter(this.replaceDelims(tournamentDisplayName(game.getTournamentName())));
			this.writeWithDelimiterAfter(this.replaceDelims(opponentDisplayName(game.getOpponentName())));
			this.writeWithDelimiterAfter(asString(pointSummary.getElapsedTime()));
			this.writeWithDelimiterAfter(pointSummary.getLineType());
			this.writeWithDelimiterAfter(asString(pointSummary.getScore().getOurs()));
			this.writeWithDelimiterAfter(asString(pointSummary.getScore().getTheirs()));
			this.writeWithDelimiterAfter(event.getType());
			this.writeWithDelimiterAfter(event.getAction());
			this.writeWithDelimiterAfter(replaceDelims(playerDisplayName(event.getPasser())));
			this.writeWithDelimiterAfter(replaceDelims(playerDisplayName(event.getReceiver())));
			this.writeWithoutDelimiter(replaceDelims(playerDisplayName(event.getDefender())));
			String hangTime = "";
			if (event.getDetails() != null && event.getDetails().getHangtime() > 0) {
				float hangTimeSeconds = (float)event.getDetails().getHangtime()  / 1000f;
				hangTime = this.asString(hangTimeSeconds);
			} 
			this.writeWithDelimiterBefore(hangTime);
			if (point.getLine() != null) {
				int i = 0;
				for (String playerName : point.playersInPoint()) {
					i++;
					if (i < MAX_PLAYERS_IN_POINT) {
						this.writeWithDelimiterBefore(replaceDelims(playerDisplayName(playerName)));
					}
				}
				while (i < MAX_PLAYERS_IN_POINT) {
					this.writeWithDelimiterBefore("");
					i++;
				}
			}
			writer.write("\n");
		} catch (IOException e) {
			throw new RuntimeException("Error writing export", e);
		}
	}

	private void writeHeader() {
		try {
			if (teamId != null) {
				writer.write("Team");
				writer.write(DELIMITER);
			}
			writer.write("Date/Time");
			writer.write(DELIMITER);
			writer.write("Tournamemnt");
			writer.write(DELIMITER);			
			writer.write("Opponent");
			writer.write(DELIMITER);
			writer.write("Point Elapsed Seconds");
			writer.write(DELIMITER);
			writer.write("Line");		
			writer.write(DELIMITER);
			writer.write("Our Score - End of Point");	
			writer.write(DELIMITER);
			writer.write("Their Score - End of Point");
			writer.write(DELIMITER);
			writer.write("Event Type");
			writer.write(DELIMITER);
			writer.write("Action");
			writer.write(DELIMITER);
			writer.write("Passer");
			writer.write(DELIMITER);
			writer.write("Receiver");
			writer.write(DELIMITER);
			writer.write("Defender");
			writer.write(DELIMITER);
			writer.write("Hang Time (secs)");
			for (int i = 0; i < MAX_PLAYERS_IN_POINT; i++) {
				writer.write(DELIMITER);
				writer.write("Player " + Integer.toString(i));	
			}
			writer.write("\n");
		} catch (IOException e) {
			throw new RuntimeException("Error writing export", e);
		}
	}
	
	private void writeWithDelimiterBefore(String field) throws IOException {
		writer.write(DELIMITER);
		writer.write(field == null ? "" : field); 
	}
	
	private void writeWithDelimiterAfter(String field) throws IOException {
		writer.write(field == null ? "" : field); 
		writer.write(DELIMITER);
	}
	
	private void writeWithoutDelimiter(String field) throws IOException {
		writer.write(field == null ? "" : field); 
	}
	
	private String replaceDelims(String s) {
		if (s == null) {
			return "";
		}
		return s.replace(DELIMITER, DELIMITER_REPLACEMENT);
	}
	
	private String asString(long i) {
		return Long.toString(i);
	}
	
	private String asString(float f) {
		return Float.toString(f);
	}
	
	private String teamDisplayName(String teamId) {
		return anonymizer == null ? teamId : anonymizer.anonymizeTeamName(teamId);
	}
	
	private String playerDisplayName(String nickname) {
		return anonymizer == null ? nickname : anonymizer.anonymizeNickname(nickname);
	}
	
	private String opponentDisplayName(String opponentName) {
		return anonymizer == null ? opponentName : anonymizer.anonymizeOpponentName(opponentName);
	}
	
	private String tournamentDisplayName(String tournamentName) {
		return anonymizer == null ? tournamentName : anonymizer.anonymizeTournamentName(teamId, tournamentName);
	}
	
	private String displayableTimestamp(String timestamp) {
		return anonymizer == null ? timestamp : anonymizer.anonymizeTimestamp(timestamp);
	}

}