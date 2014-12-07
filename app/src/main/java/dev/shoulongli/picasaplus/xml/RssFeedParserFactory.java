package dev.shoulongli.picasaplus.xml;

public class RssFeedParserFactory 
{
	public static RssFeedParser getParser()
	{
		return getParser(RssFeedParserType.SAX);
	}
	public static RssFeedParser getParser(RssFeedParserType type)
	{
		switch(type)
		{
		case SAX:
			return new SaxRssFeedParser();
//		case ANDROID_SAX:
//			return new AndroidSaxRssFeedParser();
//		case DOM:
//			return new DomRssFeedParser();
//		case XML_PULL:
//			return new XmlPullRssFeedParser();
		}
		return null;
	}
}
