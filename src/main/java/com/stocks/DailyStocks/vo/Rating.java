package com.stocks.DailyStocks.vo;

import java.io.Serializable;

public  class Rating implements Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 2711355812615280247L;
	private int userId;
	  private int movieId;
	  private float rating;
	  private long timestamp;
 
//	  public Rating(int userId, int movieId, float rating, long timestamp) {
//	    this.userId = userId;
//	    this.movieId = movieId;
//	    this.rating = rating;
//	    this.timestamp = timestamp;
//	  }

	  public int getUserId() {
	    return userId;
	  }

	  public int getMovieId() {
	    return movieId;
	  }

	  public float getRating() {
	    return rating;
	  }

	  public long getTimestamp() {
	    return timestamp;
	  }
	  public  void parseRating(String str) {
		    String[] fields = str.split("::");
		    if (fields.length != 4) {
		      throw new IllegalArgumentException("Each line must contain 4 fields");
		    }
		    userId = Integer.parseInt(fields[0]);
		     movieId = Integer.parseInt(fields[1]);
		     rating = Float.parseFloat(fields[2]);
		     timestamp = Long.parseLong(fields[3]);
		    //return new Rating(userId, movieId, rating, timestamp);
		  }
	 public String toString() {
		 StringBuilder x = new StringBuilder();
		 x.append(userId).append(" ").append(movieId).append(" ").append(rating).append(" ").append(timestamp).append("\n");
		 return x.toString();
	 } 
}

