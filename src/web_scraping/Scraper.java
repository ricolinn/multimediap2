	package web_scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
//	public static final String url = "https://www.imdb.com/title/tt0111161/";
	public static void main(String[] args) throws Exception{
		try {
			System.out.println("*****STARTING PARSING*****");
			ExcelParser ep = new ExcelParser();
			Movie[] movies = ep.readExcel();
			for(int i = 0; i < movies.length; ++i) {
				String link = movies[i].getLink();
				link += "/";
				Document mainDoc = Jsoup.connect(link).maxBodySize(1024*1024*10)
			            .timeout(0).ignoreContentType(true)
			            .execute().parse();
				Document keywordsDoc = Jsoup.connect(link + "keywords").maxBodySize(1024*1024*10)
			            .timeout(0).ignoreContentType(true)
			            .execute().parse();
				
				Document creditsDoc = Jsoup.connect(link + "fullcredits").maxBodySize(1024*1024*10)
		            .timeout(0).ignoreContentType(true)
		            .execute().parse();
				String director = getDirector(mainDoc);
				String summary = getSummaryText(mainDoc);
				
				String keywords = getKeywords(keywordsDoc);
				String cast = getCast(creditsDoc);
				
				movies[i].setDirector(director);
				movies[i].setSummary(summary);
				movies[i].setKeywords(keywords);
				movies[i].setCast(cast);
			}
			System.out.println("*****FINISHED*****");
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	public static void getWebpageTitle(String url) throws Exception {
		Document doc = Jsoup.connect(url).get();
		System.out.println("Title from: " + doc.title());
		String title = doc.title();
		System.out.println(title);
	}
	
	public static void getAllLinks(String url) throws Exception{
		Document doc = Jsoup.connect(url).get();
		Elements elems = doc.select("a[href]");
		System.out.println("Links from: " + doc.title());
		for(Element el: elems) {
			System.out.println("\t:" + elems.text());
		}
	}
	
	public static void getAllImages(String url) throws Exception{
		Document doc = Jsoup.connect(url).get();
		Elements elems = doc.select("img[src~=[\\w\\d\\W]log[\\w\\d\\W]]");
		System.out.println("Images from: " + doc.title());
		for(Element el: elems) {
			System.out.println("\t:" + elems.attr("src"));
		}
	}
	
	public static void getH4(String url) throws Exception{
		Document doc = Jsoup.connect(url).get();
		Elements elems = doc.select("h4");
		System.out.println("H4 from: " + doc.title());
		for (Element el: elems) {
			System.out.println("\t:" + el.childNode(0).toString());
		}
	}
	
	public static void getSummary(Document doc) throws Exception{
		Elements elems = doc.select("div[class=credit_summary_item]");
		System.out.println("Summary text from: " + doc.title());
		for(Element el: elems) {
			System.out.println("\t:" + el.text());
		}
	}
	
	public static String getSummaryText(Document doc) throws Exception{
		Elements elems = doc.select("div[class=summary_text]");
		System.out.println("Summary from: " + doc.title());
		for(Element el: elems) {
			return el.text();
		}
		return null;
	}
	
	public static String getDirector(Document doc) throws Exception{
		Elements elems = doc.select("div.credit_summary_item");
		return elems.get(0).text();
	}
	
	public static String getKeywords(Document doc) throws Exception{
		Elements elems = doc.select("td[data-item-keyword]");
		String resultString = "";
		for (Element el: elems) {
			if(el != elems.last()) {
				resultString += (el.attr("data-item-keyword") + " ");
			} else {
				resultString += (el.attr("data-item-keyword"));
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
					resultString += (text + " ");
				} else {
					resultString += text;
				}				
			}
		}
		return resultString;
	}
	
	
}