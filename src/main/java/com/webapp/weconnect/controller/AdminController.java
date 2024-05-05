package com.webapp.weconnect.controller;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.repository.CommunityRepository;
import com.webapp.weconnect.service.CommunityImageService;
import com.webapp.weconnect.service.CommunityService;
import com.webapp.weconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;



@Controller
public class AdminController {
    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;
    private final CommunityRepository communityRepository;
    private final CommunityImageService communityImageService;
    public AdminController(CommunityRepository communityRepository, CommunityImageService communityImageService) {
        this.communityRepository = communityRepository;
        this.communityImageService = communityImageService;
    }



    @Value("${file.storage.location}")
    private String fileStorageLocation;

    /**
     * Display the pending communities to the admin for approval.This way the admin has all the list at one place to review
     * @param model the model object used to pass data to the view layer.
     * @param session session object used to store community.
     * @return pending community admin page
     * */
    @GetMapping("/admin/pendingCommunities")
    public String showPendingCommunities(Model model, HttpSession session) {
        List<Community> allPendingCommunities = communityService.listPendingCommunities();

        List<Community> pendingCommunities = allPendingCommunities.stream()
                .filter(community -> Boolean.TRUE.equals(community.getIsLeader()))
                .collect(Collectors.toList());

        Map<Long, String> creatorNames = new HashMap<>();
        Map<String, List<String>> communityFiles = new HashMap<>();

        for (Community community : pendingCommunities) {
            String email = community.getCreatedBy();
            User user = userService.findByEmail(email);
            String fullName = user.getFirstName() + " " + user.getLastName();
            creatorNames.put(community.getId(), fullName);

            String directoryPath = Paths.get(fileStorageLocation, community.getName()).toString();
            Path dirPath = Paths.get(directoryPath);
            if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
                try {
                    List<String> filesInDirectory = Files.list(dirPath)
                            .map(Path::getFileName)
                            .map(Path::toString)
                            .collect(Collectors.toList());
                    communityFiles.put(community.getName(), filesInDirectory);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        session.setAttribute("communityFiles", communityFiles);

        model.addAttribute("pendingCommunities", pendingCommunities);
        model.addAttribute("creatorNames", creatorNames);
        return "adminPendingCommunities";
    }

    /**
     * Download the ID associated with community that was uploaded the by community leader as ID proof.
     * @param communityName the community for which the ID proof was uploaded
     * @param request the object representing the HTTP request.
     * @return response entity containing the ID file content as a byte array, along with appropriate header for downloading the file
     * */
    @GetMapping("/downloadIdFile")
    public ResponseEntity<byte[]> downloadIdFile(@RequestParam("communityName") String communityName, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, List<Map<String, Object>>> communityFiles =
                (Map<String, List<Map<String, Object>>>) session.getAttribute("communityFiles");

        if (communityFiles != null && communityFiles.containsKey(communityName)) {
            List<Map<String, Object>> files = communityFiles.get(communityName);
            if (!files.isEmpty()) {
                Map<String, Object> fileData = files.get(0);
                byte[] idFileContent = (byte[]) fileData.get("content");
                String idFileName = (String) fileData.get("name");

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + idFileName + "\"")
                        .body(idFileContent);
            }
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Approve the community after reviewing the ID proof. This way the admin approves and the community is now accessible to users.
     * @return id the ID
     * @return the pending approval page after redirect
     * */
    @PostMapping("/admin/approveCommunity/{id}")
    public String approveCommunity(@PathVariable Long id) {
        communityService.approveCommunity(id);
        return "redirect:/admin/pendingCommunities";
    }

    /**
     * In event if the admin rejects the community approval. This can be due to violation of laws or inappropriate content.
     * @param id the ID of the path community
     * @return pending community page for admin redirect
     * */
    @PostMapping("/admin/rejectCommunity/{id}")
    public String rejectCommunity(@PathVariable Long id) {
        communityService.rejectCommunity(id);
        return "redirect:/admin/pendingCommunities";
    }

    /**
     * Method to get the image for the community approval. This is because the admin must see the community image before making decision
     * @param communityName the name of community that needs approval
     * @return response entity with image, headers as well as status
     * */
    @GetMapping("/admin/getImage")
    public ResponseEntity<byte[]> getImage(@RequestParam("communityName") String communityName) {
        byte[] imageData = communityImageService.getImageStringByCommunityName(communityName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    /**
     * Download the ID proof that the community leader has uploaded. This makes it easier for admin to make his decision.
     * @param communityName the name of community
     * @param fileName the name of the ID proof file that was submitted
     * @return appropriate response entity
     * */
    @GetMapping("/admin/downloadIdFile")
    public ResponseEntity<Resource> downloadIdFile(@RequestParam("communityName") String communityName,
                                                   @RequestParam("fileName") String fileName) {
        Path filePath = Paths.get(fileStorageLocation).resolve(communityName).resolve(fileName).normalize();

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Render the admin home page upon successful login by the admin
     * @param model the model object that transfers the details
     * @return the admin home page
     * */
    @GetMapping("/admin")
    public String adminHome(Model model) {
        List<Community> approvedCommunities = communityService.listApprovedCommunities();
        model.addAttribute("communities", approvedCommunities);
        return "admin";
    }

    /**
     * Handle the logout event for the admin panel
     * @return upon successful logout, redirect the admin to the login page.
     * */
    @GetMapping("/admin/logout")
    public String adminLogout(){
        return "redirect:/login";
    }

    /**
     * Method to handle login process of the admin. This way the admin can access the services using the company credentials defined.
     * @param username the username of admin
     * @param password admin password
     * @param redirectAttributes object used to add attributes for redirection.
     * */
    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             RedirectAttributes redirectAttributes) {
        final String hardcodedAdminUsername = "weconnectadmin@gmail.com";
        final String hardcodedAdminPassword = "admin123456";

        if (hardcodedAdminUsername.equals(username) && hardcodedAdminPassword.equals(password)) {
            // Successful admin login
            return "redirect:/admin"; // Redirect to the admin homepage
        } else {
            // Failed admin login
            redirectAttributes.addFlashAttribute("error", "Invalid admin username or password!");
            return "redirect:/login"; // Redirect back to the login page with an error message
        }
    }
}