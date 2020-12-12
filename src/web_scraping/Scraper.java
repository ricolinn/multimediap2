package web_scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	public static final String url = "https://www.imdb.com/title/tt0111161/";
	public static void main(String[] args) throws Exception{
		getWebpageTitle(url);
//		getAllLinks(url);
		getAllImages(url);
		getH4(url);
		getSummary(url);
		getSummaryText(url);
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
	
	public static void getSummary(String url) throws Exception{
		Document doc = Jsoup.connect(url).get();
		Elements elems = doc.select("div[class=credit_summary_item]");
		System.out.println("Summary text from: " + doc.title());
		for(Element el: elems) {
			System.out.println("\t:" + el.text());
		}
	}
	
	public static void getSummaryText(String url) throws Exception{
		Document doc = Jsoup.connect(url).get();
		Elements elems = doc.select("div[class=summary_text]");
		System.out.println("Summary from: " + doc.title());
		for(Element el: elems) {
			System.out.println("\t:" + el.text());
		}
	}
}