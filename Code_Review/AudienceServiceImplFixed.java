//////code fixes/////

package com.aviva.aem.test;

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


import com.aviva.aem.test.AudienceService;

@Component(label = "Audience Service", description = "Get Configurations",
    immediate = true)
@Service(AudienceService.class)
@Properties({@Property(name = "rootPath", label = "Root Path",
    value = DEFAULT_PATH)})
public class AudienceServiceImpl implements AudienceService {

    public static final String DEFAULT_PATH = "/etc/default"; 

    private String rootPath = DEFAULT_PATH;

    public List<Option> getAudienceAsOptions(SlingHttpServletRequest request) {  
       List<Option> list = new ArrayList<>(); 
        Resource resource = request.getResourceResolver().getResource(rootPath);
        if (resource != null) {
            List<String[]> audienceList = getAudiences(resource);  
            for (String[] audience : audienceList) {
                list.add(new Option(audience[0], audience[1]));
            }
        }
        return list;
    }

    /**
     * Returns resource name and page title of the childNodes of a specific resource.
     *
     * @param resource
     */
    private static List<String[]> getAudiences(Resource resource) {
        List<String[]> list = new ArrayList<>();  
        Iterator<Resource> childNodes = resource.listChildren();
        while (childNodes.hasNext()) {
            Resource child = childNodes.next();
            list.add(new String[]{child.getName(), child.getTitle()}); 
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Activate
    public void activate(Map<String, Object> properties) {
        rootPath = PropertiesUtil.toString(properties.get("rootPath"), DEFAULT_PATH);
    }
}