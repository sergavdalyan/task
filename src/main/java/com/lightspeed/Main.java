package com.lightspeed;

import com.lightspeed.parser.Query;
import com.lightspeed.parser.SQLParser;

public class Main {
	public static void main(String[] args) {
		String sql = "SELECT author.name, count(book.id), sum(book.cost) " +
				"FROM author " +
				"LEFT JOIN book ON (author.id = book.author_id) " +
				"WHERE book.cost > 100 " +
				"GROUP BY author.name " +
				"HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 " +
				"ORDER BY author.name DESC " +
				"LIMIT 10 OFFSET 5;";

		SQLParser parser = new SQLParser();
		Query query = parser.parse(sql);

		System.out.println(query);
	}
}
