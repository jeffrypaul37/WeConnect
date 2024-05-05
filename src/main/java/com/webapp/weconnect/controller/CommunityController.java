package com.webapp.weconnect.controller;

import com.webapp.weconnect.model.*;
import com.webapp.weconnect.repository.*;
import com.webapp.weconnect.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import com.webapp.weconnect.service.CommunityService;

@Controller
public class CommunityController {

    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private EventMemberRepository eventMemberRepository;
    @Autowired
    private CommunityImageRepository communityImageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityImageService communityImageService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Value("${file.storage.location}")
    private String fileStorageLocation;

    @Autowired
    private DonationRepository donationRepository;

    /**
     * Handles HTTP POST requests to create a new post for a community.
     * So that a new post can be added to community.
     * @param request the HttpServletRequest containing post parameters
     * @return a redirection to the current page
     */
    @PostMapping("/create-post")
    public String createPost(HttpServletRequest request) {
        // Extract post parameters from the request
        String postTitle = request.getParameter("postTitle");
        String postContent = request.getParameter("postContent");
        MultipartFile postImage = ((MultipartHttpServletRequest) request).getFile("postImage");
        String communityName = request.getParameter("communityName");

        // Retrieve authentication information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail);
        Optional<Boolean> isLeaderOptional = communityRepository.getIsLeader(communityName, currentUser.getEmail());

        //check if the currently logged user is a community leader before posting
        if (isLeaderOptional.isPresent() && isLeaderOptional.get()) {
            // If the user is a leader, create the post
            postService.createPost(postTitle, postContent, postImage, communityName);
        }

        // Redirect to the current page
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    /**
     * Handles HTTP GET requests to retrieve posts for a specific community.
     * So that the users can see all the posts made by the leader.
     * @param communityName the name of the community to retrieve posts for
     * @return a list of messages (posts) for the specified community
     */
    @GetMapping("/get-posts")
    @ResponseBody
    public List<Message> getPosts(@RequestParam String communityName) {
        //retrieve posts stored in messageRepository to be displayed in the community page
        return messageRepository.findByCommunityName(communityName);
    }

    /**
     * Handles HTTP POST requests to create a new event.
     * So that users can see the events created for a community.
     * @param request the HttpServletRequest containing event parameters
     * @return a redirection to the current page
     */
    @PostMapping("/create-event")
    public String createEvent(HttpServletRequest request) {
        // Extract event parameters from the request
        String eventTitle = request.getParameter("eventTitle");
        String eventContent = request.getParameter("eventContent");
        MultipartFile eventImage = ((MultipartHttpServletRequest) request).getFile("eventImage");
        String communityName = request.getParameter("communityName");
        String eventLocation = request.getParameter("eventLocation");
        Date eventDate = null;
        try {
            eventDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("eventDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String eventTime = request.getParameter("eventTime");
        Integer maxCapacity = Integer.parseInt(request.getParameter("eventCapacity"));

        // Retrieve authentication information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail);
        Optional<Boolean> isLeaderOptional = communityRepository.getIsLeader(communityName, currentUser.getEmail());
        EventMember newEventMember = new EventMember();
        newEventMember.setEventName(eventTitle);
        newEventMember.setEventMember(currentUserEmail);
        newEventMember.setCommunityName(communityName);
        eventMemberRepository.save(newEventMember);

       //check if the currently logged user is a community leader before creating the event
        if (isLeaderOptional.isPresent() && isLeaderOptional.get()) {
            // If the user is a leader, create the event
            eventService.createEvent(
                    eventTitle, eventContent, eventImage, communityName, eventLocation, eventDate, eventTime, maxCapacity);
        }

        // Redirect back to the referring page
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    /**
     * Handles HTTP GET requests to show all communities.
     * So that, list of community can be shown to user for them to explore.
     * @param communityName the name of the community to filter by
     * @param model the Model object to add attributes to
     * @return the name of the view template to render
     */
    @GetMapping("/")
    public String showAllCommunities(@RequestParam(required = false)  String communityName,Model model) {
        // Retrieve authentication information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Retrieve all approved communities
        List<Community> allCommunities = communityService.listApprovedCommunities();
        List<String> uniqueCommunityNames = getAllUniqueCommunityNames(allCommunities);
        List<Community> joinedCommunities = communityRepository.findByCreatedBy(currentUserEmail);
        List<String> joinedCommunityNames = joinedCommunities.stream()
                .map(Community::getName)
                .collect(Collectors.toList());

        //display communities in reverse to show the latest communities at the top
        Collections.reverse(uniqueCommunityNames);
        model.addAttribute("communities", uniqueCommunityNames);
        model.addAttribute("joinedCommunityNames", joinedCommunityNames);
        model.addAttribute("communityName", communityName);
        return "index";
    }

    /**
     * Retrieves unique community names from a list of communities.
     * So that only one instance of it is displayed.
     * @param communities the list of communities
     * @return a list of unique community names
     */
    private List<String> getAllUniqueCommunityNames(List<Community> communities) {
        return communities.stream()
                .map(Community::getName)
                .distinct()
                .collect(Collectors.toList());
    }


    /**
     * Handles HTTP GET requests to show communities joined by the current user.
     * So, only the communities which the user is a part of are shown.
     * @param model the Model object to add attributes to
     * @return the name of the view template to render
     */
    @GetMapping("/communities")
    public String showCommunity(Model model){
        // Retrieve authentication information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Retrieve communities joined by the current user that are approved
        List<Community> userCommunities = communityRepository.findByCreatedBy(currentUserEmail);
        List<Community> approvedCommunities = communityService.listApprovedCommunities();

        // Get a set of approved communities
        Set<String> approvedCommunityNames = approvedCommunities.stream()
                .map(Community::getName)
                .collect(Collectors.toSet());

        // Filter joined communities to include only those that are approved
        List<Community> userApprovedCommunities = userCommunities.stream()
                .filter(community -> approvedCommunityNames.contains(community.getName()))
                .collect(Collectors.toList());

        //display communities in reverse to show the latest communities at the top
        Collections.reverse(userApprovedCommunities);
        model.addAttribute("communities", userApprovedCommunities);
        return "communities";
    }

    /**
     * Handles HTTP GET requests to view a community.
     * So, that when user clicks on a community, they can see all the details of that community.
     * @param communityName the name of the community to view
     * @param model the Model object to add attributes to
     * @return the name of the view template to render
     */
    @GetMapping("/viewCommunity")
    public String viewCommunity(@RequestParam("communityName") String communityName,
                                Model model) {
        // Retrieve members of the community
        List<String> members = communityRepository.findMembersByCommunityName(communityName);

        // Retrieve authentication information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Check if the current user is a leader of the community
        Optional<Boolean> isLeaderOptional = communityRepository.getIsLeader(communityName, currentUserEmail);
        boolean isLeader = isLeaderOptional.orElse(false);

        // Retrieve communities joined by the current user
        List<Community> joinedCommunities = communityRepository.findByCreatedBy(currentUserEmail);
        List<String> joinedCommunityNames = joinedCommunities.stream()
                .map(Community::getName)
                .collect(Collectors.toList());

        // Retrieve events joined by the current user in the community
        List<EventMember> joinedEvents = eventMemberRepository.findByEventMemberAndCommunityName(currentUserEmail, communityName);
        List<String> joinedEventNames = joinedEvents.stream()
                .map(EventMember::getEventName)
                .collect(Collectors.toList());

        // Add attributes to the model
        model.addAttribute("joinedCommunityNames", joinedCommunityNames);
        model.addAttribute("joinedEventNames", joinedEventNames);
        model.addAttribute("communityName", communityName);
        model.addAttribute("members", members);
        model.addAttribute("isLeader", isLeader);
        model.addAttribute("postService", postService);

        // Retrieve messages (posts) for the community
        List<Message> messages = messageRepository.findByCommunityName(communityName);
        Collections.reverse(messages);
        model.addAttribute("messages", messages);

        // Retrieve events for the community
        List<Event> events = eventRepository.findByCommunityName(communityName);
        Collections.reverse(events);
        model.addAttribute("events", events);

        // Fetch comments for each blog post
        Map<Long, List<Comment>> commentsMap = new HashMap<>();
        for (Message message : messages) {
            List<Comment> comments = commentRepository.findByMessageId(message.getId());
            commentsMap.put(message.getId(), comments);
        }
        model.addAttribute("commentsMap", commentsMap);

        // Fetching the profile photos of user who made a comment
        List<User> memberProfiles = new ArrayList<>();
        for(String member : members) {
            User user = userRepository.findByEmail(member);
            memberProfiles.add(user);
        }
        model.addAttribute("memberProfiles", memberProfiles);

        // Checking if user is a member of community
        boolean isMember = members.contains(currentUserEmail);
        model.addAttribute("isMember", isMember);
        User currentUser = userRepository.findByEmail(currentUserEmail);
        model.addAttribute("currentUser",currentUser);

        // Retrieve donations for the community
        List<Donation> donations = donationRepository.getDonationsByCommunityName(communityName);
        model.addAttribute("donations", donations);

        // Return the name of the view template to render
        return "viewCommunity";
    }

    /**
     * Handles HTTP POST requests to create a new community.
     * So that a new community is created and sent for approval to the admin before making it available to users.
     * @param request the HttpServletRequest containing community parameters
     * @param redirectAttributes the attributes for page redirection
     * @param model the Model object to add attributes to
     * @param idFile the MultipartFile for uploading ID file
     * @return a redirection to the communities page
     * @throws IOException if an I/O error occurs
     */
    @PostMapping("/createCommunity")
    public String createCommunity(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model,
                                  @RequestParam("idFile") MultipartFile idFile)throws IOException {
        // Fetch community parameters from the request
        String communityName = request.getParameter("communityName");
        String description = request.getParameter("description");
        MultipartFile uploadImage = ((MultipartHttpServletRequest) request).getFile("uploadImage");

        // Retrieve authentication information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        //ensure that the community names are unique upon creation
        if (communityRepository.existsByName(communityName) || communityImageRepository.existsByCommunityName(communityName)) {
            redirectAttributes.addAttribute("error", "A community with the same name already exists! Please choose another name");
            return "redirect:/communities";
        }

        // Upload ID file if provided
        if (!idFile.isEmpty()) {
            try {
                String uploadDir = Paths.get(fileStorageLocation, communityName).toString();
                Path uploadDirPath = Paths.get(uploadDir);
                if (Files.notExists(uploadDirPath)) {
                    Files.createDirectories(uploadDirPath);
                }
                String idFileName = idFile.getOriginalFilename();
                Path filePath = uploadDirPath.resolve(idFileName);

                try (InputStream inputStream = idFile.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                redirectAttributes.addFlashAttribute("message", "File uploaded successfully to community: " + communityName);
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "Failed to upload file!");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
        }

        // Create a new community object
        Community newCommunity = new Community();
        newCommunity.setName(communityName);
        newCommunity.setCreatedBy(currentUserName);
        newCommunity.setIsLeader(true);
        communityRepository.save(newCommunity);

        try {
            byte[] imageBytes = uploadImage.getBytes();
            CommunityImage newCommunityImage = new CommunityImage();
            newCommunityImage.setCommunityName(communityName);
            newCommunityImage.setCommunityImage(imageBytes);
            newCommunityImage.setCommunityDescription(description);
            communityImageRepository.save(newCommunityImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add attributes to the model
        model.addAttribute("communityName", communityName);
        model.addAttribute("description", description);
        model.addAttribute("uploadImage", uploadImage);

        // Redirect to the communities page
        return "redirect:/communities";
    }

    /**
     * Handles HTTP POST requests to join a community.
     * So that a user can be added to that community as a member of that community.
     * @param communityName the name of the community to join
     * @param request the HttpServletRequest containing request parameters
     * @param redirectAttributes the RedirectAttributes for page redirection
     * @return a redirection to the current page
     */
    @PostMapping("/joinCommunity")
    public String joinCommunity(@RequestParam String communityName, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Retrieve authentication information
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();

            // Create a new community object
            Community newCommunity = new Community();
            newCommunity.setName(communityName);
            newCommunity.setCreatedBy(currentUserName);

            // Save details of member and the community they joined
            communityRepository.save(newCommunity);

            // Redirect back to the current page
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        } catch (DataIntegrityViolationException e) {
            // Handle the case where the user is already a member of the community
            redirectAttributes.addAttribute("error", "You are already a member of this community!");
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
    }

    /**
     * Handles HTTP POST requests to leave a community.
     * So, that if a user wants to leave a community, then he can leave it easily.
     * @param communityName the name of the community to leave
     * @param request the HttpServletRequest containing request parameters
     * @param redirectAttributes the RedirectAttributes for page redirection
     * @return a redirection to the current page
     */
    @PostMapping("/leaveCommunity")
    public String leaveCommunity(@RequestParam String communityName, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Retrieve authentication information
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();

            // Delete details of the member and their associated community
            communityRepository.deleteByCommunityNameAndCreatedBy(communityName,currentUserName);

            // Redirect back to the referring page
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        } catch (DataIntegrityViolationException e) {
            // Handle the case where there is an error in leaving the community
            redirectAttributes.addAttribute("error", "Unable to leave community!");
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
    }

    /**
     * Handles HTTP POST requests to join an event.
     * So that user can register for an event that is about to happen, which is organized by that community.
     * @param request the HttpServletRequest containing event parameters
     * @param redirectAttributes the RedirectAttributes for page redirection
     * @return a redirection to the current page
     */
    @PostMapping("/joinEvent")
    public String joinEvent(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Retrieve authentication information
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();

            // Retrieve community and event parameters from the request
            String communityName = request.getParameter("communityName");
            String eventName = request.getParameter("eventName");
            Long eventId = Long.parseLong(request.getParameter("eventId"));
            Date eventDate = new SimpleDateFormat("MMM dd, yyyy").parse(request.getParameter("eventDate"));
            String eventTime = request.getParameter("eventTime");
            String eventLocation = request.getParameter("eventLocation");

            // Create a new event member and set its details
            EventMember newEventMember = new EventMember();
            newEventMember.setEventMember(currentUserName);
            newEventMember.setCommunityName(communityName);
            newEventMember.setEventName(eventName);
            eventMemberRepository.save(newEventMember);

            // Update event capacity and send confirmation email
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            if (optionalEvent.isPresent()) {
                Event event = optionalEvent.get();
                int updatedCapacity = event.getMaxCapacity() - 1;
                event.setMaxCapacity(updatedCapacity);
                eventRepository.save(event);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                String formattedDate = dateFormat.format(eventDate);
                String subject = "Event Registration Confirmation";
                String message = "You have successfully registered for " + eventName
                        + " on " + formattedDate + " " + eventTime + " at " + eventLocation + ".";
                emailService.sendEmail(currentUserName, subject, message);
            }

            // Redirect back to the current page
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        } catch (ParseException | DataIntegrityViolationException e) {
            // Handle errors when registering for an event
            redirectAttributes.addAttribute("error", "An error occurred while processing your request.");
            return "redirect:/";
        }
    }

    /**
     * Handles HTTP GET requests to retrieve the image associated with a community.
     * So that the community image can be displayed to users on the community page.
     * @param communityName the name of the community to retrieve the image for
     * @return a ResponseEntity containing the image data, content type, and HTTP status
     */
    @GetMapping("/getImage")
    public ResponseEntity<byte[]> getImage(@RequestParam("communityName") String communityName) {
        // Retrieve image data for the community
        byte[] imageData = communityImageService.getImageStringByCommunityName(communityName);

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        // Return ResponseEntity with image data, content type, and HTTP status
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    /**
     * Handles HTTP GET requests to retrieve the description associated with a community.
     * So that the users can have an idea of what the community is about and what things they expect from their users.
     * @param communityName the name of the community to retrieve the description for
     * @return the description of the community as a string
     */
    @GetMapping("/getDescription")
    @ResponseBody
    public String getDescription(@RequestParam("communityName") String communityName) {
        // Retrieve description for the community
        String description = communityImageService.getDescriptionByCommunityName(communityName);
        return description;
    }

    /**
     * Handles HTTP GET requests to retrieve the image associated with a post.
     * Sothat the Blog/Post image can be obtained to be viewed on page.
     * @param id the ID of the post to retrieve the image for
     * @return a ResponseEntity containing the image data, content type, and HTTP status
     */
    @GetMapping("/getPostImage")
    public ResponseEntity<byte[]> getPostImage(@RequestParam("id") Long id) {
        // Retrieve image data for the post
        byte[] imageData = postService.getPostImageById(id);

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        // Return ResponseEntity with image data, content type, and HTTP status
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    /**
     * Handles HTTP GET requests to retrieve the image associated with an event.
     * So that it can displayed on the events page to be viewed by the members of that community.
     * @param id the ID of the event to retrieve the image for
     * @return a ResponseEntity containing the image data, content type, and HTTP status
     */
    @GetMapping("/getEventImage")
    public ResponseEntity<byte[]> getEventImage(@RequestParam("id") Long id) {
        // Retrieve image data for the event
        byte[] imageData = eventService.getEventImageById(id);

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        // Return ResponseEntity with image data, content type, and HTTP status
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    /**
     * Handles HTTP POST requests to edit a post.
     * So that the community leader can can make changes to the post in the future, if any.
     * @param request the HttpServletRequest containing post parameters
     * @param redirectAttributes the RedirectAttributes for page redirection
     * @return a redirection to the view community page
     */
    @PostMapping("/editPost")
    public String editPost(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Retrieve post parameters from the request
        Long postId = Long.parseLong(request.getParameter("postId"));
        String newTitle = request.getParameter("newTitle");
        String newContent = request.getParameter("newContent");
        MultipartFile postImage = null;
        if (request instanceof MultipartHttpServletRequest) {
            postImage = ((MultipartHttpServletRequest) request).getFile("postImage");
        }
        String[] deleteImageValues = request.getParameterValues("deleteImage");
        boolean deleteImage = deleteImageValues != null && deleteImageValues.length > 0;

        // Retrieve the post from the database
        Optional<Message> optionalMessage = messageRepository.findById(postId);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();

            // Update post details
            message.setTitle(newTitle);
            message.setContent(newContent);
            if (deleteImage) {
                // Delete post image
                message.setPostImage(null);
            } else if (postImage != null && !postImage.isEmpty()) {
                // Update post image
                try {
                    byte[] imageData = postImage.getBytes();
                    message.setPostImage(imageData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Save the updated post
            messageRepository.save(message);

            // Redirect to the view community page
            redirectAttributes.addAttribute("communityName", message.getCommunityname());
            return "redirect:/viewCommunity";
        } else {
            return "redirect:/error";
        }
    }

    /**
     * Handles HTTP POST requests to delete a post.
     * So that a post can be deleted by the leader if he feels that it is no longer relevant to the community.
     * @param postId the ID of the post to delete
     * @return a redirection to the view community page
     */
    @PostMapping("/deletePost")
    public String deletePost(@RequestParam("postId") Long postId) {
        // Retrieve the post by its ID
        Optional<Message> optionalMessage = messageRepository.findById(postId);
        if (optionalMessage.isPresent()) {
            // Delete the retrieved post
            Message message = optionalMessage.get();
            messageRepository.delete(message);

            // Redirect to the view community page
            return "redirect:/viewCommunity?communityName=" + message.getCommunityname();
        } else {
            return "redirect:/error";
        }
    }

    /**
     * Handles HTTP POST requests to edit an event.
     * So that the leader can edit an event that is already hosted, in case if there are any last minutes changes.
     * @param request the HttpServletRequest containing event parameters
     * @param redirectAttributes the RedirectAttributes for page redirection
     * @return a redirection to the view community page
     */
    @PostMapping("/editEvent")
    public String editEvent(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Retrieve event parameters from the request
        Long eventId = Long.parseLong(request.getParameter("eventId"));
        String newTitle = request.getParameter("newTitle");
        String newContent = request.getParameter("newContent");

        // Retrieve the image associated with the event
        MultipartFile eventImage = null;
        if (request instanceof MultipartHttpServletRequest) {
            eventImage = ((MultipartHttpServletRequest) request).getFile("eventImage");
        }
        String[] deleteImageValues = request.getParameterValues("deleteEventImage");
        boolean deleteImage = deleteImageValues != null && deleteImageValues.length > 0;

        // Retrieve the event by its ID
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            // Update the event details
            Event event = optionalEvent.get();
            event.setTitle(newTitle);
            event.setContent(newContent);
            if (deleteImage) {
                event.setEventImage(null);
            } else if (eventImage != null && !eventImage.isEmpty()) {
                try {
                    byte[] imageData = eventImage.getBytes();
                    event.setEventImage(imageData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Save the updated event
            eventRepository.save(event);
            redirectAttributes.addAttribute("communityName", event.getCommunityname());
            return "redirect:/viewCommunity";
        } else {
            return "redirect:/error";
        }
    }

    /**
     * Handles HTTP POST requests to delete an event.
     * So that the event can be deleted in an event of some happenings.
     * @param eventId the ID of the event to delete
     * @return a redirection to the view community page
     */
    @PostMapping("/deleteEvent")
    public String deleteEvent(@RequestParam("eventId") Long eventId) {
        // Retrieve the event by its ID
        Optional<Event> optionalMessage = eventRepository.findById(eventId);
        if (optionalMessage.isPresent()) {
            // Delete the event
            Event event = optionalMessage.get();
            eventRepository.delete(event);

            // Redirect to the view community page
            return "redirect:/viewCommunity?communityName=" + event.getCommunityname();
        } else {
            return "redirect:/error";
        }
    }

    /**
     * Method to handle the creation of comments when a user adds a comment on blog
     * So that a user is able to post a new comment for a blog which they whey want to interact with.
     * @param messageId the ID of the blog post
     * @param content the comment message
     * @param communityName the name of the community
     * @return view_frontend
     * */
    @PostMapping("/create-comment")
    public String createComment(@RequestParam("messageId") Long messageId,
                                @RequestParam("content") String content,
                                @RequestParam("communityName") String communityName,
                                Model model) {
        try {
            // Authenticating the user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserId = authentication.getName(); // Assuming user ID is stored as username
            User currentUser = userRepository.findByEmail(currentUserId);

            Optional<Message> optionalMessage = messageRepository.findById(messageId);
            // Validating the parameters
            if (optionalMessage.isPresent()) {
                Message message = optionalMessage.get();
                Comment comment = new Comment();
                if (!content.isEmpty()) {
                    comment.setContent(content);
                }
                if (message != null) {
                    comment.setMessage(message);
                }
                comment.setUser(currentUser);
                comment.setCreatedAt(LocalDateTime.now());
                message.getComments().add(comment);
                commentService.saveComment(comment);
            }
            // Return the community page
            return "redirect:/viewCommunity?communityName=" + communityName;
        } catch (IllegalArgumentException e) {
            // Handle invalid input parameters
            model.addAttribute("errorMessage", "Invalid input parameters: " + e.getMessage());
            return "redirect:/viewCommunity?communityName=" + communityName;
        } catch (Exception e) {
            // Handle other unexpected exceptions
            model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            return "redirect:/viewCommunity?communityName=" + communityName;
        }
    }

}
