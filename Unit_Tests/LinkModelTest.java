package com.aviva.aem.test.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import java.lang.reflect.Field;

class LinkModelTest {

    @InjectMocks
    private LinkModel linkModel;

    @Mock
    private Resource resource;

    @Mock
    private PageManager pageManager;

    @Mock
    private Page page;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Create the LinkModel instance
        linkModel = new LinkModel();

        // Inject mocks into private fields
        injectPrivateField(linkModel, "resource", resource);
        injectPrivateField(linkModel, "pageManager", pageManager);

        // Console log for setup
        System.out.println("Setup completed: Mocks injected into LinkModel");
    }

    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Allow access to private fields
        field.set(target, value);  // Inject the value
    }

    @Test
    void testInit_WithPage() throws Exception {
        when(resource.getPath()).thenReturn("/content/mypage");
        when(pageManager.getContainingPage("/content/mypage")).thenReturn(page);

        // Initialize the model
        linkModel.init();

        // Verify the link is set correctly
        System.out.println("Link after init (with page): " + linkModel.getLink());
        assertEquals("/content/mypage.html", linkModel.getLink());
    }

    @Test
    void testInit_WithoutPage() throws Exception {
        when(resource.getPath()).thenReturn("/content/mypage");
        when(pageManager.getContainingPage("/content/mypage")).thenReturn(null);

        // Initialize the model
        linkModel.init();

        // Verify the link remains unchanged
        System.out.println("Link after init (without page): " + linkModel.getLink());
        assertEquals("/content/mypage", linkModel.getLink());
    }

    @Test
    void testGetTitle_WithNavigationTitle() throws Exception {
        when(resource.getPath()).thenReturn("/content/mypage");
        when(pageManager.getContainingPage("/content/mypage")).thenReturn(page);
        when(pageManager.getContainingPage("/content/mypage.html")).thenReturn(page);
        when(page.getNavigationTitle()).thenReturn("Mock Navigation Title");

        injectPrivateField(linkModel, "title", "Fallback Title");

        linkModel.init();
        System.out.println("Path used in getContainingPage: " + linkModel.getLink());
        System.out.println("Navigation title from Page: " + page.getNavigationTitle());
        assertEquals("Mock Navigation Title", linkModel.getTitle());
    }

    @Test
    void testGetTitle_WithPageTitleOnly() throws Exception {
        when(resource.getPath()).thenReturn("/content/mypage");
        when(pageManager.getContainingPage("/content/mypage")).thenReturn(page);
        when(pageManager.getContainingPage("/content/mypage.html")).thenReturn(page);
        when(page.getNavigationTitle()).thenReturn(null);
        when(page.getTitle()).thenReturn("Mock Page Title");

        injectPrivateField(linkModel, "title", "Fallback Title");

        linkModel.init();
        System.out.println("Path used in getContainingPage: " + linkModel.getLink());
        System.out.println("Page title from Page: " + page.getTitle());
        assertEquals("Mock Page Title", linkModel.getTitle());
    }

    @Test
    void testGetTitle_WithoutPage() throws Exception {
        when(resource.getPath()).thenReturn("/content/mypage");
        when(pageManager.getContainingPage("/content/mypage.html")).thenReturn(null);

        injectPrivateField(linkModel, "title", "Fallback Title");

        linkModel.init();
        System.out.println("Path used in getContainingPage: " + linkModel.getLink());
        System.out.println("Fallback title used: " + linkModel.getTitle());
        assertEquals("Fallback Title", linkModel.getTitle());
    }

    @Test
    void testGetTitle_NullNavigationAndPageTitle() throws Exception {
        when(resource.getPath()).thenReturn("/content/mypage");
        when(pageManager.getContainingPage("/content/mypage.html")).thenReturn(page);
        when(page.getNavigationTitle()).thenReturn(null);
        when(page.getTitle()).thenReturn(null);

        // Inject the fallback title
        injectPrivateField(linkModel, "title", "Fallback Title");

        // Initialize and verify
        linkModel.init();
        System.out.println("Title retrieved (null navigation and page title): " + linkModel.getTitle());
        assertEquals("Fallback Title", linkModel.getTitle());
    }

    @Test
    void testAppendExtensionToLink() throws Exception {
        when(resource.getPath()).thenReturn("/content/mypage");
        when(pageManager.getContainingPage("/content/mypage")).thenReturn(page);

        // Call init to trigger appendExtensionToLink()
        linkModel.init();

        System.out.println("Link after appendExtensionToLink: " + linkModel.getLink());
        assertEquals("/content/mypage.html", linkModel.getLink());
    }

    @Test
    void testGetLink_BeforeInit() {
        // Verify the link is null before initialization
        System.out.println("Link before init: " + linkModel.getLink());
        assertNull(linkModel.getLink());
    }

    @Test
    void testGetTitle_BeforeInit() throws Exception {
        // Inject the fallback title
        injectPrivateField(linkModel, "title", "Fallback Title");

        // Verify the title is the fallback title before initialization
        System.out.println("Title before init: " + linkModel.getTitle());
        assertEquals("Fallback Title", linkModel.getTitle());
    }
}
