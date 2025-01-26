//////////////fixed code///////////////

package com.aviva.aem.test;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;

import com.aviva.aem.test.OPtion;

/**
 * Audience service interface.
 */
@FunctionalInterface
public interface AudienceService {

    /**
     * Get the list of page owners
     *
     * @param request instance of SlingHttpServletRequest
     * @return List<OPtion> Collection of audience names
     */
    List<OPtion> getAudienceAsOptions(SlingHttpServletRequest request);
}
