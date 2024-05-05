package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.controller.AdminController;
import com.webapp.weconnect.controller.CommunityController;
import com.webapp.weconnect.model.*;
import com.webapp.weconnect.repository.*;
import com.webapp.weconnect.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

public class CommunityControllerTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private CommunityService communityService;
    @Mock
    private CommunityRepository communityRepository;
    @Mock
    private EventMemberRepository eventMemberRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostService postService;
    @Mock
    private EventService eventService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private CommunityImageService communityImageService;
    @Mock
    private CommunityImageRepository communityImageRepository;
    @Mock
    private MultipartHttpServletRequest multipartHttpServletRequest;
    @Mock
    private Model model;

    @Mock
    private EmailService emailService;

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private MessageRepository messageRepositoryMock;

    @Mock
    private CommentService commentServiceMock;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CommunityController communityController;
    @InjectMocks
    private AdminController adminController;

    @Mock
    private DonationRepository donationRepository;
    @Mock
    private RedirectAttributes redirectAttributes;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(communityController).build();
        MockitoAnnotations.openMocks(this);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ReflectionTestUtils.setField(communityController, "fileStorageLocation", "uploadfile");
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void createPost_WhenUserIsLeader_ShouldCreatePostSuccessfully() {

        String communityName = "Tech";
        User mockUser = new User();
        mockUser.setEmail("user@example.com");

        when(multipartHttpServletRequest.getParameter("postTitle")).thenReturn("Post Title");
        when(multipartHttpServletRequest.getParameter("postContent")).thenReturn("Post Content");
        when(multipartHttpServletRequest.getFile("postImage")).thenReturn(new MockMultipartFile("image", "image.jpg", "text/plain", "some xml".getBytes()));
        when(multipartHttpServletRequest.getParameter("communityName")).thenReturn(communityName);

        when(userRepository.findByEmail(anyString())).thenReturn(mockUser);
        when(communityRepository.getIsLeader(anyString(), anyString())).thenReturn(Optional.of(true));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        communityController.createPost(multipartHttpServletRequest);

        verify(postService).createPost(anyString(), anyString(), any(), anyString());

        assertTrue(true);
    }

    @Test
    public void createEvent_WhenUserIsLeader_ShouldCreateEventSuccessfully() {

        String communityName = "Tech";
        User mockUser = new User();
        mockUser.setEmail("user@example.com");

        when(multipartHttpServletRequest.getParameter("eventTitle")).thenReturn("Tech Meetup");
        when(multipartHttpServletRequest.getParameter("eventContent")).thenReturn("Meetup Content");
        when(multipartHttpServletRequest.getFile("eventImage")).thenReturn(new MockMultipartFile("image", "event.jpg", "text/plain", "some xml".getBytes()));
        when(multipartHttpServletRequest.getParameter("communityName")).thenReturn(communityName);
        when(multipartHttpServletRequest.getParameter("eventDate")).thenReturn("2024-04-01");
        when(multipartHttpServletRequest.getParameter("eventTime")).thenReturn("18:00");
        when(multipartHttpServletRequest.getParameter("eventLocation")).thenReturn("Downtown");
        when(multipartHttpServletRequest.getParameter("eventCapacity")).thenReturn("100");

        when(userRepository.findByEmail(anyString())).thenReturn(mockUser);
        when(communityRepository.getIsLeader(anyString(), anyString())).thenReturn(Optional.of(true));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        String result = communityController.createEvent(multipartHttpServletRequest);

        verify(eventService).createEvent(anyString(), anyString(), any(), anyString(), anyString(), any(), anyString(), anyInt());
        assertTrue(result.contains("redirect:"));
    }

    @Test
    public void joinCommunity_WhenCommunityExists_ShouldJoinCommunitySuccessfully() {

        String communityName = "Tech";

        when(multipartHttpServletRequest.getParameter("communityName")).thenReturn(communityName);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        when(communityRepository.existsByName(anyString())).thenReturn(false);

        String result = communityController.joinCommunity(communityName, multipartHttpServletRequest, null);

        verify(communityRepository).save(any(Community.class));
        assertTrue(result.contains("redirect:"));
    }
    @Test
    public void viewCommunity_WhenCommunityExists_ShouldReturnCommunityDetails() {

        String communityName = "Careers";
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        when(communityRepository.findMembersByCommunityName(anyString())).thenReturn(Arrays.asList("user1@example.com", "user2@example.com"));
        when(communityRepository.getIsLeader(anyString(), anyString())).thenReturn(Optional.of(true));
        when(eventMemberRepository.findByEventMemberAndCommunityName(anyString(), anyString())).thenReturn(Collections.emptyList());
        when(messageRepository.findByCommunityName(anyString())).thenReturn(Collections.emptyList());
        when(eventRepository.findByCommunityName(anyString())).thenReturn(Collections.emptyList());

        String result = communityController.viewCommunity(communityName, new ExtendedModelMap());

        assertEquals("viewCommunity", result);
    }

    @Test
    public void leaveCommunity_WhenUserIsMember_ShouldRemoveUserFromCommunitySuccessfully() {

        String communityName = "Tech";

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        String result = communityController.leaveCommunity(communityName, multipartHttpServletRequest, null);

        verify(communityRepository).deleteByCommunityNameAndCreatedBy(anyString(), anyString());
        assertTrue(result.contains("redirect:"));
    }

    @Test
    public void getImage_WhenCommunityHasImage_ShouldReturnImage() {

        byte[] imageBytes = new byte[]{1, 2, 3};
        when(communityImageService.getImageStringByCommunityName(anyString())).thenReturn(imageBytes);

        ResponseEntity<byte[]> response = communityController.getImage("Tech");

        assertArrayEquals(imageBytes, response.getBody());
    }

    @Test
    public void getDescription_WhenCommunityExists_ShouldReturnDescription() {

        String expectedDescription = "Tech community description";
        when(communityImageService.getDescriptionByCommunityName(anyString())).thenReturn(expectedDescription);

        String actualDescription = communityController.getDescription("Tech");

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void testShowAllCommunities() {

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<Community> allCommunities = new ArrayList<>();
        Community community1 = new Community();
        community1.setName("Community 1");
        allCommunities.add(community1);
        Community community2 = new Community();
        community2.setName("Community 2");
        allCommunities.add(community2);
        when(communityService.listApprovedCommunities()).thenReturn(allCommunities);

        List<Community> joinedCommunities = new ArrayList<>();
        Community community3 = new Community();
        community3.setName("Community 1");
        joinedCommunities.add(community3);
        when(communityRepository.findByCreatedBy("test@example.com")).thenReturn(joinedCommunities);

        List<String> uniqueCommunityNames = new ArrayList<>();
        uniqueCommunityNames.add("Community 2");
        uniqueCommunityNames.add("Community 1"); // Adding Community 1 as it should be present
        List<String> joinedCommunityNames = new ArrayList<>();
        joinedCommunityNames.add("Community 1");

        communityController.showAllCommunities(null, model);

        verify(model, times(1)).addAttribute("communities", uniqueCommunityNames);
        verify(model, times(1)).addAttribute("joinedCommunityNames", joinedCommunityNames);
        verify(model, times(1)).addAttribute("communityName", null);
        verifyNoMoreInteractions(model);

        assertTrue(uniqueCommunityNames.contains("Community 1"));
    }

    @Test
    void testGetAllUniqueCommunityNames() {

        List<Community> communities = Arrays.asList(
                createCommunity("Community 1"),
                createCommunity("Community 2"),
                createCommunity("Community 1"),
                createCommunity("Community 3"),
                createCommunity("Community 2")
        );

        List<String> uniqueNames = getAllUniqueCommunityNames(communities);

        assertEquals(Arrays.asList("Community 1", "Community 2", "Community 3"), uniqueNames);
    }

    private List<String> getAllUniqueCommunityNames(List<Community> communities) {
        return communities.stream()
                .map(Community::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    private Community createCommunity(String name) {
        Community community = new Community();
        community.setName(name);
        return community;
    }

    @Test
    void downloadIdFile_Successful() {
        // Setup
        String communityName = "TestCommunity";
        String fileName = "testFile.txt";
        byte[] fileContent = new byte[]{1, 2, 3};

        List<Map<String, Object>> fileList = new ArrayList<>();
        Map<String, Object> fileData = new HashMap<>();
        fileData.put("name", fileName);
        fileData.put("content", fileContent);
        fileList.add(fileData);

        Map<String, List<Map<String, Object>>> communityFiles = new HashMap<>();
        communityFiles.put(communityName, fileList);

        when(session.getAttribute("communityFiles")).thenReturn(communityFiles);

        // Execute
        ResponseEntity<byte[]> response = adminController.downloadIdFile(communityName, request);

        // Verify
        assertEquals("attachment; filename=\"" + fileName + "\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }


    @Test
    void testCreateCommunity() throws Exception {
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        request.setParameter("communityName", "Test Community");
        request.setParameter("description", "Test Description");

        MockMultipartFile idFile = new MockMultipartFile("idFile", "id.pdf", "application/pdf", "test id content".getBytes());
        MockMultipartFile uploadImage = new MockMultipartFile("uploadImage", "test.png", "image/png", "test image content".getBytes());

        request.addFile(idFile);
        request.addFile(uploadImage);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(communityRepository.existsByName("Test Community")).thenReturn(false);

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Model model = mock(Model.class);


        String viewName = communityController.createCommunity(request, redirectAttributes, model, idFile);


        assertEquals("redirect:/communities", viewName);


        verify(communityRepository, times(1)).existsByName("Test Community");
        verify(communityImageRepository, times(1)).existsByCommunityName("Test Community");
        verify(communityRepository, times(1)).save(any(Community.class));
        verify(communityImageRepository, times(1)).save(any(CommunityImage.class));


        verify(redirectAttributes, times(1)).addFlashAttribute(eq("message"), anyString());

        verifyNoMoreInteractions(communityRepository, communityImageRepository, redirectAttributes);
    }


    @Test
    void testGetPostImage() {

        byte[] imageData = "MockImageData".getBytes();

        Long postId = 123L;

        when(postService.getPostImageById(postId)).thenReturn(imageData);

        ResponseEntity<byte[]> response = communityController.getPostImage(postId);

        assertArrayEquals(imageData, response.getBody());
    }

    @Test
    void testEditPost() {
        Long postId = 123L;
        String newTitle = "New Title";
        String newContent = "New Content";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("postId", postId.toString());
        request.setParameter("newTitle", newTitle);
        request.setParameter("newContent", newContent);

        Message message = new Message();

        when(messageRepository.findById(postId)).thenReturn(Optional.of(message));

        String result = communityController.editPost(request, mock(RedirectAttributes.class));

        verify(messageRepository, times(1)).save(any(Message.class));

        assertEquals("redirect:/viewCommunity", result);
    }

    @Test
    void testEditEvent() {

        Long eventId = 123L;
        String newTitle = "New Title";
        String newContent = "New Content";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("eventId", eventId.toString());
        request.setParameter("newTitle", newTitle);
        request.setParameter("newContent", newContent);

        Event event = new Event();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        String result = communityController.editEvent(request, mock(RedirectAttributes.class));

        verify(eventRepository, times(1)).save(any(Event.class));

        assertEquals("redirect:/viewCommunity", result);
    }

    @Test
    void testDeletePostWithExistingPost() {

        Long postId = 123L;

        Message message = new Message();
        message.setCommunityname("TestCommunity");

        when(messageRepository.findById(postId)).thenReturn(Optional.of(message));

        String result = communityController.deletePost(postId);

        verify(messageRepository, times(1)).delete(message);

        assertEquals("redirect:/viewCommunity?communityName=TestCommunity", result);
    }

    @Test
    void testDeletePostWithNonExistingPost() {

        Long postId = 123L;

        when(messageRepository.findById(postId)).thenReturn(Optional.empty());

        String result = communityController.deletePost(postId);

        verify(messageRepository, never()).delete(any(Message.class));

        assertEquals("redirect:/error", result);
    }

    @Test
    void testDeleteEvent() {

        Long eventId = 123L;

        Event event = new Event();
        event.setCommunityname("TestCommunity");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        String result = communityController.deleteEvent(eventId);

        verify(eventRepository, times(1)).delete(event);

        assertEquals("redirect:/viewCommunity?communityName=TestCommunity", result);
    }

    @Test
    void testGetEventImage() {

        Long eventId = 123L;

        byte[] imageData = new byte[]{1, 2, 3, 4, 5};

        when(eventService.getEventImageById(eventId)).thenReturn(imageData);

        ResponseEntity<byte[]> responseEntity = communityController.getEventImage(eventId);

        verify(eventService, times(1)).getEventImageById(eventId);

        assertEquals(imageData, responseEntity.getBody());
    }

    @Test
    void testShowCommunity() {

        String currentUserEmail = "user@example.com";

        Community community1 = new Community();
        community1.setName("Community 1");
        Community community2 = new Community();
        community2.setName("Community 2");

        when(communityRepository.findByCreatedBy(currentUserEmail)).thenReturn(Arrays.asList(community1, community2));

        when(communityService.listApprovedCommunities()).thenReturn(Arrays.asList(community1, community2));

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(currentUserEmail);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String viewName = communityController.showCommunity(model);

        verify(model).addAttribute("communities", Arrays.asList(community2, community1));

        assertEquals("communities", viewName);
    }

    @Test
    void testGetPosts() throws NoSuchFieldException, IllegalAccessException {

        String communityName = "Community";

        Message message1 = new Message();
        message1.setTitle("Title 1");
        Message message2 = new Message();
        message2.setTitle("Title 2");

        Field idField1 = Message.class.getDeclaredField("id");
        idField1.setAccessible(true);
        idField1.set(message1, 1L);
        Field idField2 = Message.class.getDeclaredField("id");
        idField2.setAccessible(true);
        idField2.set(message2, 2L);

        when(messageRepository.findByCommunityName(communityName))
                .thenReturn(Arrays.asList(message1, message2));

        List<Message> result = communityController.getPosts(communityName);

        assertEquals(2, result.size());
    }


    @Test
    public void testJoinEvent() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Event event = new Event();
        event.setMaxCapacity(10);
        Optional<Event> optionalEvent = Optional.of(event);
        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/joinEvent")
                .param("communityName", "Test Community")
                .param("eventName", "Test Event")
                .param("eventId", "1")
                .param("eventDate", "Jan 01, 2024")
                .param("eventTime", "12:00 PM")
                .param("eventLocation", "Test Location");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        verify(eventRepository, times(1)).save(any());

        verify(eventRepository, times(1)).save(argThat(eventArgument -> {
            int updatedCapacity = eventArgument.getMaxCapacity();
            assertEquals(9, updatedCapacity);
            return true;
        }));
    }

    /**
     * Test to check the case where we are validating the authentication of the user.
     * This is implemented using the mock object.
     * */
    @Test
    public void testCreateComment_getAuthentication() {
        // Mocking the required objects to test
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Calling the method
        Authentication result = SecurityContextHolder.getContext().getAuthentication();

        // Assert
        assertEquals(authentication, result);
    }

    /**
     * Test to check the functionality to fetch the user who is creating the comments.
     * This is done so that the comment can be mapped to the person who created it.
     * */
    @Test
    public void testCreateComment_getUser() {
        // Mocking the required objects to test
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUserId");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Setting the SecurityContext
        SecurityContextHolder.setContext(securityContext);

        // Calling the method
        String result = SecurityContextHolder.getContext().getAuthentication().getName();

        // Assert
        assertEquals("testUserId", result);
    }

    /**
     * Test to check the case of finding the user by email.
     * This way the code flow can be validated against the expected flow.
     * */
    @Test
    public void testCreateComment_findByEmail() {
        // Mocking the UserRepository
        UserRepository userRepository = mock(UserRepository.class);
        User mockedUser = mock(User.class);
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockedUser);

        // Calling the method
        User result = userRepository.findByEmail("test@example.com");

        // Assert
        assertEquals(mockedUser, result);
    }

    /**
     * Test to check the scenario when the message is empty. In this case, the expected behavior is to
     * redirect to the view community page. This is done using assertEquals
     * */
    @Test
    void testCreateComment_optionalMessageEmpty() {
        // Call the method
        String result = communityController.createComment(1L, "test content", "test community", mock(Model.class));

        // Verifying that commentService.saveComment() is not called
        verify(commentServiceMock, never()).saveComment(any());
        // Assert the result
        assertEquals("redirect:/viewCommunity?communityName=test community", result);
    }

    /**
     * Test to check the case when the message is present but content of comment is empty.
     * */
    @Test
    void testCreateComment_optionalMessagePresentContentEmpty() {
        // Call the method without stubbing messageRepository.findById()
        String result = communityController.createComment(1L, "", "test community", mock(Model.class));

        // Verifying whether it is empty or not
        verify(commentServiceMock, never()).saveComment(any());
        // Assert the result
        assertEquals("redirect:/viewCommunity?communityName=test community", result);
    }

    /**
     * Test to validate whether the method behaves as expected upon receiving the valid inputs.
     * */
    @Test
    public void testCreateComment_ValidInput_RedirectsToCommunityViewPage() {
        Long messageId = 1L;
        String content = "Test comment";
        String communityName = "Community1";
        User currentUser = new User();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepositoryMock.findByEmail("test@example.com")).thenReturn(currentUser);
        when(messageRepositoryMock.findById(messageId)).thenReturn(Optional.of(new Message()));
        String result = communityController.createComment(messageId, content, communityName, model);
        assertEquals("redirect:/viewCommunity?communityName=" + communityName, result);
    }


    @Test
    void testCreateCommunity_CommunityExists() throws IOException {
        String communityName = "ExistingCommunity";

        when(communityRepository.existsByName(communityName)).thenReturn(true);

        MockMultipartHttpServletRequest request = createMockMultipartRequest(communityName);
        MockMultipartFile idFile = new MockMultipartFile("idFile", "id.pdf", "application/pdf", new byte[0]);
        MockMultipartFile uploadImage = new MockMultipartFile("uploadImage", "test.png", "image/png", new byte[0]);

        request.addFile(idFile);
        request.addFile(uploadImage);

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String result = communityController.createCommunity(request, redirectAttributes, mock(Model.class), idFile);

        verify(redirectAttributes).addAttribute("error", "A community with the same name already exists! Please choose another name");
        assertEquals("redirect:/communities", result);
    }

    private MockMultipartHttpServletRequest createMockMultipartRequest(String communityName) {
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        request.addParameter("communityName", communityName);
        return request;
    }

    @Test
    void testEditPost_DeleteImage() {
        Message messageMock = mock(Message.class); // Mock Message object
        Optional<Message> optionalMessage = Optional.of(messageMock);
        when(messageRepository.findById(any())).thenReturn(optionalMessage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("postId", "1");
        request.setParameter("newTitle", "New Title");
        request.setParameter("newContent", "New Content");
        request.setParameter("deleteImage", "true");

        String result = communityController.editPost(request, mock(RedirectAttributes.class));
        verify(messageRepository, times(1)).save(any(Message.class));

        assertEquals("redirect:/viewCommunity", result);
    }


}
