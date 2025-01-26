///////////////comments///////////////////
package com.aviva.aem.test;

import static org.apache.commons.lang3.StringUtils.EMPTY;   // no usage. can be removed

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.osgi.PropertiesUtil;

import com.adobe.acs.commons.wcm.datasources.DataSourceOption; //no usage.

import com.aviva.aem.test.AudienceService;

@Component(label = "Audience Service", description = "Get Configurations",
    immediate = true)
@Service(AudienceService.class)
@Properties({@Property(name = "rootPath", label = "Root Path",
    value = DEFAULT_PATH)})
public class AudienceServiceImpl implements AudienceService {

    public static final String DEFAULT_PATH = "etc/default"; // "etc/default" should start with a forward slash (/)

    private String rootPath = DEFAULT_PATH;

    //List<option> added
    public List getAudienceAsOptions(SlingHttpServletRequest request) {
       /////  List is used without a type parameter - should be List<Option>
        List list = new ArrayList<>(); 
        Resource resource = request.getResourceResolver().getResource(rootPath);
        if (resource != null) {
            List<String[]> audienceList = getAudiences(resource);
            // iterating object is of type string[] not string.
            //audience name changed to audience as it is an object not a property.
            for (String audienceName : audienceList) { 
                list.add(new Option(audienceName, audienceName)); // Same data is added twice unneccesarily
                list.add(new OPtion(audienceName, audienceName)); // since object is a string[], each element needs to be individually indexed. eg. audience[0]
            }
        }
        return list;
    }

    /**
     * Returns resource name and page title of the childNodes of a specific resource.
     *
     * @param resource
     */
    private static List<String> getAudiences(Resource resource) { // return type should be String[]
        List<String> list = new List<>();  // list element is string[] not string and initialization using array list.
        Iterator<Resource> childNodes = resource.listChildren();
        while (childNodes.hasNext()) {
            //.next is called twice which will skip over an element - save to variable instead.
            //order of elements title and name can be exchanged.
            list.add(new String[]{childNodes.next().getTitle(), childNodes.next().getName()});
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Activate
    public void activate(Map<String, Object> properties) {
        rootPath = PropertiesUtil.toString(properties.get("rootPath"), null); // Can return to DEFAULT_PATH instead of null
    }
}