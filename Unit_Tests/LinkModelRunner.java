package com.aviva.aem.test.models;

import org.apache.sling.api.resource.Resource;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class LinkModelRunner {
    public static void main(String[] args) {
        try {
            // Create an instance of LinkModel
            LinkModel linkModel = new LinkModel();

            // Inject mock dependencies
            Resource resource = createMockResource();
            PageManager pageManager = createMockPageManager();

            injectPrivateField(linkModel, "resource", resource);
            injectPrivateField(linkModel, "pageManager", pageManager);
            injectPrivateField(linkModel, "title", "Injected Title"); // Set fallback title

            // Initialize the model and print the results
            linkModel.init(); // Initialize the link field
            System.out.println("Mock Resource Path: " + resource.getPath());
            System.out.println("Link After Init: " + linkModel.getLink());
            System.out.println("Mock Page: " + (pageManager.getContainingPage(linkModel.getLink()) != null ? "Exists" : "Null"));
            System.out.println("Navigation Title: " + (pageManager.getContainingPage(linkModel.getLink()) != null ? pageManager.getContainingPage(linkModel.getLink()).getNavigationTitle() : "Null"));
            System.out.println("Page Title: " + (pageManager.getContainingPage(linkModel.getLink()) != null ? pageManager.getContainingPage(linkModel.getLink()).getTitle() : "Null"));

            System.out.println("Link: " + linkModel.getLink());
            System.out.println("Title: " + linkModel.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Resource createMockResource() {
        Resource mockResource = mock(Resource.class);
        when(mockResource.getPath()).thenReturn("/content/mypage");
        return mockResource;
    }

    private static PageManager createMockPageManager() {
        PageManager mockPageManager = mock(PageManager.class);
        Page mockPage = createMockPage();

        // Stub for both pre-extension and post-extension cases
        when(mockPageManager.getContainingPage("/content/mypage")).thenReturn(mockPage);
        when(mockPageManager.getContainingPage("/content/mypage.html")).thenReturn(mockPage);

        return mockPageManager;
    }

    private static Page createMockPage() {
        Page mockPage = mock(Page.class);
        when(mockPage.getNavigationTitle()).thenReturn("Mock Navigation Title");
        when(mockPage.getTitle()).thenReturn("Mock Page Title");
        return mockPage;
    }

    private static void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
