package web_scraping;


import java.io.FileWriter;
import java.io.PrintWriter;

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
		Movie[] movies = new Movie[movieIds.length];
		int notFound = 0;
		for(int i = 0; i<movieIds.length; ++i) {
			String movieId = movieIds[i];

			if(movieId.compareTo("0") == 0) {
				notFound++;
				continue;
			}

			String movieLink = link + movieId + "/";
			String keywordsLink = movieLink + "keywords";
			String creditsLink = movieLink + "fullcredits";
			
			String title = getTitle(movieLink);
			int year = Integer.parseInt(getYear(movieLink));
			String genre = getGenre(movieLink);
			float rating = Float.parseFloat(getRating(movieLink));
			String director = getDirector(movieLink);
			String summary = getSummaryText(movieLink);
			String keywords = getKeywords(keywordsLink);
			String cast = getCast(creditsLink);
			movies[i] = new Movie(title, year, genre, rating, director, summary, keywords, cast);
			
			Gson gson = new Gson();
			String json = gson.toJson(movies[i]);
			String movieTitle = movies[i].getTitle();
			FileWriter fw = new FileWriter("json/"+movieTitle+".json");
			PrintWriter pw = new PrintWriter(fw);
			
			pw.print(json);
			pw.close();
		}
		System.out.println(notFound);

		
	}

	public static String getTitle(String url) throws Exception {
		Document doc = Jsoup.connect(url).get();
		System.out.println("Title from: " + doc.title());
		Elements elems = doc.select("h1");
		for(Element el: elems) {
			return el.ownText();
		}
		return null;
	}
	
	public static String getYear(String url) throws Exception {
		Document doc = Jsoup.connect(url).get();
		System.out.println("Year from: " + doc.title());
		Elements elems = doc.select("h1 a");
		for(Element el: elems) {
			return el.ownText();
		}
		return null;
	}
	
	public static String getGenre(String url) throws Exception{
		Document doc = Jsoup.connect(url).get();
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
	
	public static String getRating(String url) throws Exception{
		Document doc = Jsoup.connect(url).get();
		System.out.println("Rating from: " + doc.title());
		Elements elems = doc.select("span[itemprop=ratingValue]");
		for(Element el: elems) {
			return el.ownText();
		}
		return null;
	}
	
	public static String getDirector(String url) throws Exception{
		Document doc = Jsoup.connect(url).maxBodySize(1024*1024*10)
        .timeout(0).ignoreContentType(true)
        .execute().parse();
		Elements elems = doc.select("div.credit_summary_item");
		return elems.get(0).text();
	}
	
	
	public static String getSummaryText(String url) throws Exception{
		Document doc = Jsoup.connect(url).maxBodySize(1024*1024*10)
	            .timeout(0).ignoreContentType(true)
	            .execute().parse();
		Elements elems = doc.select("div[class=summary_text]");
		System.out.println("Summary text from: " + doc.title());
		for(Element el: elems) {
			return el.text();
		}
		return null;
	}
	
	public static String getKeywords(String url) throws Exception{
		Document doc = Jsoup.connect(url).maxBodySize(1024*1024*10)
	            .timeout(0).ignoreContentType(true)
	            .execute().parse();
		Elements elems = doc.select("td[data-item-keyword]");
		String resultString = "";
		for (Element el: elems) {
			if(el != elems.last()) {
				resultString += (el.attr("data-item-keyword") + "|");
			} else {
				resultString += (el.attr("data-item-keyword"));
			}
		}
		return resultString;
	}
	
	public static String getCast(String url) throws Exception{
		Document doc = Jsoup.connect(url).maxBodySize(1024*1024*10)
	            .timeout(0).ignoreContentType(true)
	            .execute().parse();
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