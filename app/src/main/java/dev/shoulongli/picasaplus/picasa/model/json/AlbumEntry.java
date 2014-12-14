package dev.shoulongli.picasaplus.picasa.model.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shoulongli on 12/13/14.
 */
/*
About JSON feeds and XML feeds

A Google Data service creates a JSON-format feed by converting the XML feed using the following rules:

Basic

The feed is represented as a JSON object; each nested element or attribute is represented as a name/value property of the object.
Attributes are converted to String properties.
Child elements are converted to Object properties.
Elements that may appear more than once are converted to Array properties.
Text values of tags are converted to $t properties.
Namespace

If an element has a namespace alias, the alias and element are concatenated using "$". For example, ns:element becomes ns$element.

<entry>
        <id>https://picasaweb.google.com/data/entry/api/user/116633710550156986148/albumid/1000000456986148</id>
        <published>2013-06-01T23:31:02.000Z</published>
        <updated>2013-06-02T02:51:30.299Z</updated>
        <category scheme='http://schemas.google.com/g/2005#kind' term='http://schemas.google.com/photos/2007#album'/>
        <title type='text'>Auto Backup</title>
        <summary type='text'/>
        <rights type='text'>protected</rights>
        <link rel='http://schemas.google.com/g/2005#feed' type='application/atom+xml' href='https://picasaweb.google.com/data/feed/api/user/116633710550156986148/albumid/1000000456986148'/>
        <link rel='alternate' type='text/html' href='https://picasaweb.google.com/116633710550156986148?alabel=small_instant_upload'/>
        <link rel='self' type='application/atom+xml' href='https://picasaweb.google.com/data/entry/api/user/116633710550156986148/albumid/1000000456986148'/>
        <author>
            <name>shoulong li</name>
            <uri>https://picasaweb.google.com/116633710550156986148</uri>
        </author>
        <gphoto:id>1000000456986148</gphoto:id>
        <gphoto:name>InstantUpload</gphoto:name>
        <gphoto:access>private</gphoto:access>
        <gphoto:timestamp>1370129462000</gphoto:timestamp>
        <gphoto:numphotos>242</gphoto:numphotos>
        <gphoto:user>116633710550156986148</gphoto:user>
        <gphoto:nickname>shoulong li</gphoto:nickname>
        <gphoto:albumType>InstantUpload</gphoto:albumType>
        <media:group>
            <media:content url='https://lh3.googleusercontent.com/-swKUI377tX0/UaqjPT-MqxE/AAAAAAAAFjE/WOdZXckG3Rc/20130601.jpg' type='image/jpeg' medium='image'/>
            <media:credit>shoulong li</media:credit>
            <media:description type='plain'>Auto Backup</media:description>
            <media:keywords/>
            <media:thumbnail url='https://lh3.googleusercontent.com/-swKUI377tX0/UaqjPT-MqxE/AAAAAAAAFjE/WOdZXckG3Rc/s160-c/20130601.jpg' height='160' width='160'/>
            <media:title type='plain'>Auto Backup</media:title>
        </media:group>
    </entry>
 */
public class AlbumEntry {
    public List<Category> category;
    public Element title;
    public Element summary;

    public List<Link> links;

    @SerializedName("gphoto$numphotos")
    public Element numphotos;




}
