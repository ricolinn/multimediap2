package web_scraping;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class Scraper {
//	public static final String url = "https://www.imdb.com/title/tt0111161/";
	public static void main(String[] args) throws Exception{
		ExcelParser ep = new ExcelParser();
		String movieIds[] = ep.getIds();
		String link = "https://www.imdb.com/title/tt00";
		
		int notFound = 0;
		for(int i = 0; i<movieIds.length; ++i) {
			JSONObject movie = new JSONObject();
			String movieId = movieIds[i];

			if(movieId.compareTo("0") == 0) {
				notFound++;
				continue;
			}
			
			String movieLink = link + movieId + "/";
			Document doc = Jsoup.connect(movieLink).maxBodySize(1024*1024*10)
		            .timeout(0).ignoreContentType(true)
		            .execute().parse();
			

			String creditsLink = movieLink + "fullcredits";
			Document creditsDoc = Jsoup.connect(creditsLink).maxBodySize(1024*1024*10)
		            .timeout(0).ignoreContentType(true)
		            .execute().parse();
			String title = getTitle(doc);
			int year = Integer.parseInt(getYear(doc));
			String genre = getGenre(doc);
			float rating = Float.parseFloat(getRating(doc));
			String director = getDirector(doc);
			String summary = getSummaryText(doc);
			String keywords = getKeywords(doc);
			String cast = getCast(creditsDoc);
			
			movie.put("title", title);
			movie.put("year", year);
			movie.put("genre", genre);
			movie.put("rating", rating);
			movie.put("director", director);
			movie.put("summary", summary);
			movie.put("keywords", keywords);
			movie.put("cast", cast);
			
			String path = "json/" + i + ".json";
			FileWriter file = new FileWriter(path);
			try {
			file.write(movie.toJSONString());
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				file.flush();
				file.close();
			}
			
		}
		System.out.println(notFound);
		
		
	}

	public static String getTitle(Document doc) throws Exception {

		System.out.println("Title from: " + doc.title());
		Elements elems = doc.select("h1");
		for(Element el: elems) {
			return el.ownText();
		}
		return null;
	}
	
	public static String getYear(Document doc) throws Exception {

		System.out.println("Year from: " + doc.title());
		Elements elems = doc.select("h1 a");
		for(Element el: elems) {
			return el.ownText();
		}
		return null;
	}
	
	public static String getGenre(Document doc) throws Exception{

		System.out.println("Genre from: " + doc.title());
		Elements elems = doc.select("div[class=see-more inline canwrap] a[href*=/search/title?genres]");
		String result = "";
		for(Element el: elems) {
			if (el != elems.last()) {
				result += el.ownText() + "|";
			} else {
				result += el.ownText();
			}
			
		}
		return result;
	}
	
	public static String getRating(Document doc) throws Exception{

		System.out.println("Rating from: " + doc.title());
		Elements elems = doc.select("span[itemprop=ratingValue]");
		for(Element el: elems) {
			return el.ownText();
		}
		return null;
	}
	
	public static String getDirector(Document doc) throws Exception{

		Elements elems = doc.select("div.credit_summary_item");
		return elems.get(0).text();
	}
	
	
	public static String getSummaryText(Document doc) throws Exception{

		Elements elems = doc.select("div[class=summary_text]");
		System.out.println("Summary text from: " + doc.title());
		for(Element el: elems) {
			return el.text();
		}
		return null;
	}
	
	public static String getKeywords(Document doc) throws Exception{

		Elements elems = doc.select("div[class=see-more inline canwrap] a[href*=/search/keyword] span[class=itemprop]");
		String resultString = "";
		for (Element el: elems) {
			if(el != elems.last()) {
				resultString += (el.ownText() + "|");
			} else {
				resultString += (el.ownText());
			}
		}
		return resultString;
	}
	
	public static String getCast(Document doc) throws Exception{

		Elements elems = doc.select(".cast_list a[href*=/name]");
		String resultString = "";
		for (Element el: elems) {
			String text = el.text();
			if(text.compareTo("") != 0) {
				if(el != elems.last()) {
					resultString += (text + "|");
				} else {
					resultString += text;
				}				
			}
		}
		return resultString;
	}
	
	
}